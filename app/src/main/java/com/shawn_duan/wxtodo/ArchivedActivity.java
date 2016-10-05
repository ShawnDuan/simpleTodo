package com.shawn_duan.wxtodo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

import com.shawn_duan.wxtodo.models.TodoItem;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by sduan on 9/30/16.
 */

public class ArchivedActivity extends AppCompatActivity {
    private final static String TAG = "ArchivedActivity";
    private Realm mRealm;
    private RealmResults<TodoItem> mAllDoneItems;
    TodoListAdapter mItemsAdapter;
    RecyclerView rvItems;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealm = Realm.getDefaultInstance();
        mAllDoneItems = mRealm.where(TodoItem.class).equalTo("isDone", true).findAll();

        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Archived item");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddItem);
        fab.setVisibility(View.GONE);

        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        mItemsAdapter = new TodoListAdapter(ArchivedActivity.this, mAllDoneItems, "timestamp");
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(mItemsAdapter);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
