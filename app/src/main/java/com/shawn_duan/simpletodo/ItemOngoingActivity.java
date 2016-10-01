package com.shawn_duan.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shawn_duan.simpletodo.models.TodoItem;

import io.realm.Realm;

/**
 * Created by sduan on 9/30/16.
 */

public class ItemOngoingActivity extends AppCompatActivity {
    private final static String TAG = "ItemOngoingActivity";

    private Realm mRealm;
    private Toolbar mToolbar;
    private TextView tvTitle, tvContent, tvTime;
    private Button btFinished, btLater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealm = Realm.getDefaultInstance();

        setContentView(R.layout.activity_ongoing);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvTime = (TextView) findViewById(R.id.tvTime);
        btFinished = (Button) findViewById(R.id.btFinished);
        btLater = (Button) findViewById(R.id.btLater);

        final long itemTimestamp = getIntent().getExtras().getLong("timestamp");
        final TodoItem currentItem = mRealm.where(TodoItem.class).equalTo("timestamp", itemTimestamp).findFirst();

        tvTitle.setText(currentItem.getTitle());
        tvContent.setText(currentItem.getContent());
        String[] timeLengthForChoose = new String[]{"Select one...", "15 mins", "30 mins", "1 hour", "2 hours"};
        int index = 0;
        switch (currentItem.getEstimateTimeInMin()) {
            case 15:
                index = 1;
                break;
            case 30:
                index = 2;
                break;
            case 60:
                index = 3;
                break;
            case 120:
                index = 4;
        }
        tvTime.setText(timeLengthForChoose[index]);

        btFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        currentItem.setIsDone(true);
                        realm.copyToRealmOrUpdate(currentItem);
                    }
                });
                Intent data = new Intent();
                data.putExtra("timestamp", itemTimestamp);
                setResult(RESULT_OK, data);
                Toast.makeText(ItemOngoingActivity.this, "Great work!", Toast.LENGTH_SHORT).show();
                ItemOngoingActivity.this.finish();
            }
        });

        btLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemOngoingActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
