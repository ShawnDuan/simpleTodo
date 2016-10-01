package com.shawn_duan.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.shawn_duan.simpletodo.models.TodoItem;
import com.shawn_duan.simpletodo.models.WhatTodoFragment;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements CreateOrEditItemDialogFragment.EditNameDialogListener {
    private final static String TAG = "MainActivity";
    private Realm mRealm;
    private RealmResults<TodoItem> mAllUndoneItems;
    TodoListAdapter mItemsAdapter;
    RecyclerView rvItems;
    FloatingActionButton fabAddItem;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealm = Realm.getDefaultInstance();
        mAllUndoneItems = mRealm.where(TodoItem.class).equalTo("isDone", false).findAll();

        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        mItemsAdapter = new TodoListAdapter(MainActivity.this, mAllUndoneItems, "timestamp");
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(mItemsAdapter);

        fabAddItem = (FloatingActionButton) findViewById(R.id.fabAddItem);
        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                CreateOrEditItemDialogFragment createOrEditItemDialogFragment = CreateOrEditItemDialogFragment.newInstance();     // new todo item
                createOrEditItemDialogFragment.show(fm, "fragment_create_todo");
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_what_to_do:
                FragmentManager fm = getSupportFragmentManager();
                WhatTodoFragment whatTodoFragment = new WhatTodoFragment();
                whatTodoFragment.show(fm, "fragment_what_to_do");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFinishEditDialog(int position) {
        mItemsAdapter.notifyItemChanged((position == -1) ? mAllUndoneItems.size() : position);
//        mItemsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mItemsAdapter.notifyDataSetChanged();
    }
}
