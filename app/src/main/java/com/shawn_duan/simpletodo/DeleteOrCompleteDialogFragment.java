package com.shawn_duan.simpletodo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.shawn_duan.simpletodo.models.TodoItem;

import io.realm.Realm;

/**
 * Created by sduan on 9/30/16.
 */

public class DeleteOrCompleteDialogFragment extends DialogFragment {
    private final static String TAG = "DeleteOrCompleteDialogFragment";
    private Realm mRealm;
    private Button btDelete, btComplete;
    private int mPosition;
    private long mTimestamp;

    public DeleteOrCompleteDialogFragment() {

    }

    public static DeleteOrCompleteDialogFragment newInstance(int position, long timestamp) {
        DeleteOrCompleteDialogFragment deleteOrCompleteDialogFragment = new DeleteOrCompleteDialogFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putLong("timestamp", timestamp);
        deleteOrCompleteDialogFragment.setArguments(args);
        return deleteOrCompleteDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();

        if (getArguments() != null) {
            mPosition = getArguments().getInt("position", -1);
            mTimestamp = getArguments().getLong("timestamp", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_or_compete, container);

        btComplete = (Button) view.findViewById(R.id.btComplete);
        btDelete = (Button) view.findViewById(R.id.btDelete);

        final TodoItem item = mRealm.where(TodoItem.class).equalTo("timestamp", mTimestamp).findFirst();

        btComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        item.setIsDone(true);
                    }
                });
                ((MainActivity) getActivity()).mItemsAdapter.notifyItemRemoved(mPosition);
                getDialog().dismiss();
            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        item.deleteFromRealm(); // Delete and remove object directly
                    }
                });
                ((MainActivity) getActivity()).mItemsAdapter.notifyItemRemoved(mPosition);
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
        dialog.setTitle("Delete or Complete this Item?");
        return dialog;
    }
}
