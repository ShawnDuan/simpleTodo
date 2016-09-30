package com.shawn_duan.simpletodo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.shawn_duan.simpletodo.models.TodoItem;

import java.util.zip.Inflater;

import io.realm.Realm;

/**
 * Created by sduan on 9/29/16.
 */

public class CreateItemDialogFragment extends DialogFragment {

    private Realm mRealm;
    private EditText etTitle;
    private EditText etContent;
    private Button btSave, btCancel;
    private NumberPicker npEstimatedTime;

    public CreateItemDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_item, container);
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etContent = (EditText) view.findViewById(R.id.etContent);
        btSave = (Button) view.findViewById(R.id.btSave);
        btCancel = (Button) view.findViewById(R.id.btCancle);
        npEstimatedTime = (NumberPicker) view.findViewById(R.id.estimatedTime) ;

        etTitle.requestFocus();
        String[] timeLengthForChoose = new String[]{"Select one...", "15 mins", "30 mins", "1 hour", "2 hours"};
        npEstimatedTime.setMinValue(0);
        npEstimatedTime.setMaxValue(timeLengthForChoose.length - 1);
        npEstimatedTime.setDisplayedValues(timeLengthForChoose);
        npEstimatedTime.setWrapSelectorWheel(false);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on success
                String itemTitle = etTitle.getText().toString();
                if (itemTitle == null || itemTitle.length() == 0) {
                    Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String itemContent = etContent.getText().toString();
                int estimatedTime;
                switch (npEstimatedTime.getValue()) {
                    case 1:
                        estimatedTime = 15;
                        break;
                    case 2:
                        estimatedTime = 30;
                        break;
                    case 3:
                        estimatedTime = 60;
                        break;
                    case 4:
                        estimatedTime = 120;
                        break;
                    default:
                        estimatedTime = 0;
                }
                final TodoItem item = new TodoItem();
                item.setTimestamp(System.currentTimeMillis());
                item.setTitle(itemTitle);
                item.setContent(itemContent);
                item.setEstimateTimeInMin(estimatedTime);
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(item);
                    }
                });
                getDialog().dismiss();
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Create New Todo");
        return dialog;
    }
}
