package com.shawn_duan.wxtodo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.shawn_duan.wxtodo.models.TodoItem;

import io.realm.Realm;

/**
 * Created by sduan on 9/29/16.
 */

public class CreateOrEditItemDialogFragment extends DialogFragment implements TextView.OnEditorActionListener{

    private final static String TAG = "CreateOrEditItemDialogFragment";
    private final static int MODE_CREATE = 0;
    private final static int MODE_EDIT = 1;
    private Realm mRealm;
    private EditText etTitle;
    private EditText etContent;
    private Button btSave, btCancel;
    private NumberPicker npEstimatedTime;
    private int mPosition;
    private long mTimestamp;
    private int mMode;

    public CreateOrEditItemDialogFragment() {

    }

    @Override
    public boolean onEditorAction(TextView textView, int acttionId, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == acttionId) {
            EditNameDialogListener listener = (EditNameDialogListener) getActivity();
            listener.onFinishEditDialog((mMode == MODE_CREATE) ? -1 : mPosition);
        }
        return false;
    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(int position);
    }

    public static CreateOrEditItemDialogFragment newInstance() {
        return new CreateOrEditItemDialogFragment();
    }

    public static CreateOrEditItemDialogFragment newInstance(int position, long timestamp) {
        CreateOrEditItemDialogFragment createOrEditItemDialogFragment = new CreateOrEditItemDialogFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putLong("timestamp", timestamp);
        createOrEditItemDialogFragment.setArguments(args);
        return createOrEditItemDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();

        if (getArguments() == null) {
            mMode = MODE_CREATE;
        } else {
            mMode = MODE_EDIT;
            mPosition = getArguments().getInt("position", -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_item, container);
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etContent = (EditText) view.findViewById(R.id.etContent);
        btSave = (Button) view.findViewById(R.id.btSave);
        btCancel = (Button) view.findViewById(R.id.btCancle);
        npEstimatedTime = (NumberPicker) view.findViewById(R.id.estimatedTime);

        String[] timeLengthForChoose = new String[]{"Select one...", "15 mins", "30 mins", "1 hour", "2 hours"};
        npEstimatedTime.setMinValue(0);
        npEstimatedTime.setMaxValue(timeLengthForChoose.length - 1);
        npEstimatedTime.setDisplayedValues(timeLengthForChoose);
        npEstimatedTime.setWrapSelectorWheel(false);

        etTitle.requestFocus();
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on success
                final String itemTitle = etTitle.getText().toString();
                if (itemTitle == null || itemTitle.length() == 0) {
                    Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String itemContent = etContent.getText().toString();
                final int estimatedTime;
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

                final TodoItem item = (mMode == MODE_CREATE) ?
                        new TodoItem() :
                        mRealm.where(TodoItem.class).equalTo("timestamp", mTimestamp).findFirst();
                if (mMode == MODE_CREATE) {
                    item.setTimestamp(System.currentTimeMillis());
                }

                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        item.setTitle(itemTitle);
                        item.setContent(itemContent);
                        item.setEstimateTimeInMin(estimatedTime);
                        realm.copyToRealmOrUpdate(item);
                    }
                });

                EditNameDialogListener listener = (EditNameDialogListener) getActivity();
                listener.onFinishEditDialog((mMode == MODE_CREATE) ? -1 : mPosition);

                getDialog().dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        // initialization for edit mode only
        if (mMode == MODE_EDIT) {
            mTimestamp = getArguments().getLong("timestamp", 0);
            final TodoItem currentItem = mRealm.where(TodoItem.class).equalTo("timestamp", mTimestamp).findFirst();
            if (currentItem != null) {
                etTitle.append(currentItem.getTitle());
                etContent.append((currentItem.getContent() != null) ? currentItem.getContent() : "");

                int estimatedTime = currentItem.getEstimateTimeInMin();
                switch (estimatedTime) {
                    case 0:
                        npEstimatedTime.setValue(0);
                        break;
                    case 15:
                        npEstimatedTime.setValue(1);
                        break;
                    case 30:
                        npEstimatedTime.setValue(2);
                        break;
                    case 60:
                        npEstimatedTime.setValue(3);
                        break;
                    case 120:
                        npEstimatedTime.setValue(4);
                }
            }
        }

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
        dialog.setTitle((mMode == MODE_CREATE) ? "Create New Todo" : "Edit Existing Todo");
        return dialog;
    }
}
