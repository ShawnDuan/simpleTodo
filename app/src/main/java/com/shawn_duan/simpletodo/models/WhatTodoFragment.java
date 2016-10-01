package com.shawn_duan.simpletodo.models;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.shawn_duan.simpletodo.ItemOngoingActivity;
import com.shawn_duan.simpletodo.R;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by sduan on 9/30/16.
 */

public class WhatTodoFragment extends DialogFragment {
    private final static String TAG = "WhatTodoFragment";

    private Realm mRealm;
    private NumberPicker npGivinTime;
    private Button btOK, btCancel;

    public WhatTodoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_what_to_do, container);
        npGivinTime = (NumberPicker) view.findViewById(R.id.time_you_got);
        btOK = (Button) view.findViewById(R.id.btOK);
        btCancel = (Button) view.findViewById(R.id.btCancle);

        String[] timeLengthForChoose = new String[]{"Select one...", "15 mins", "30 mins", "1 hour", "2 hours"};
        npGivinTime.setMinValue(0);
        npGivinTime.setMaxValue(timeLengthForChoose.length - 1);
        npGivinTime.setDisplayedValues(timeLengthForChoose);
        npGivinTime.setWrapSelectorWheel(false);

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int givenTime;
                switch (npGivinTime.getValue()) {
                    case 1:
                        givenTime = 15;
                        break;
                    case 2:
                        givenTime = 30;
                        break;
                    case 3:
                        givenTime = 60;
                        break;
                    case 4:
                        givenTime = 120;
                        break;
                    default:
                        givenTime = 0;
                        Toast.makeText(getActivity(), "Please select a time length.", Toast.LENGTH_SHORT).show();
                        return;
                }
                TodoItem itemChosen = getItemBasedOnEstimatedTime(givenTime);
                if (itemChosen == null) {
                    Toast.makeText(getActivity(), "The time is too short to finish anything in your list, sorry.", Toast.LENGTH_SHORT).show();
                } else {
                    getDialog().dismiss();

                    // go to ItemOngoing activity.
                    long timestamp = itemChosen.getTimestamp();
                    Intent intent = new Intent(getActivity(), ItemOngoingActivity.class);
                    intent.putExtra("timestamp", timestamp);
                    getActivity().startActivityForResult(intent, 0);
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout((6 * getResources().getDisplayMetrics().widthPixels)/7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    // TODO: more comprehensive logic can be performed here.
    private TodoItem getItemBasedOnEstimatedTime(int givenTime) {
        RealmResults<TodoItem> availableItems = mRealm.where(TodoItem.class)
                .equalTo("isDone", false)
                .lessThanOrEqualTo("estimateTimeInMin", givenTime)
                .findAll().sort("estimateTimeInMin", Sort.DESCENDING);
        TodoItem item = availableItems.where().findFirst();
        return item;
    }
}
