package com.shawn_duan.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.shawn_duan.simpletodo.models.TodoItem;

import io.realm.Realm;

/**
 * Created by Shawn Duan on 9/22/16.
 */

public class EditActivity extends AppCompatActivity {
    private final static String TAG = "EditActivity";

    private Realm mRealm;
    private EditText etItemTitle;
    private EditText etItemContent;

    private Button btSave;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItemTitle = (EditText) findViewById(R.id.etTitle);
        etItemContent = (EditText) findViewById(R.id.etContent);
        final long itemTimestamp = getIntent().getExtras().getLong("itemTimestamp");

        mRealm = Realm.getDefaultInstance();
        final TodoItem currentItem = mRealm.where(TodoItem.class).equalTo("mTimestamp", itemTimestamp).findFirst();
        etItemTitle.append(currentItem.getTitle());
        etItemContent.append(currentItem.getContent());

        btSave = (Button) findViewById(R.id.buttonSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRealm.beginTransaction();
                currentItem.setTitle(etItemTitle.getText().toString());
                currentItem.setContent(etItemContent.getText().toString());
                mRealm.copyToRealmOrUpdate(currentItem);
                mRealm.commitTransaction();
                Intent data = new Intent();
                data.putExtra("position", getIntent().getIntExtra("position", 0));
                data.putExtra("timestamp", itemTimestamp);
                setResult(RESULT_OK, data);
                EditActivity.this.finish();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
