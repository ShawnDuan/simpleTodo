package com.shawn_duan.simpletodo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.shawn_duan.simpletodo.models.TodoItem;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private Realm mRealm;
    private RealmResults<TodoItem> mAllTodoItems;
    TodoListAdapter mItemsAdapter;
    RecyclerView rvItems;
    EditText etNewItem;

    public final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);

        mRealm = Realm.getDefaultInstance();
        mAllTodoItems = mRealm.where(TodoItem.class).findAll();
        mItemsAdapter = new TodoListAdapter(MainActivity.this, mAllTodoItems, "mTimestamp");
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(mItemsAdapter);


//        rvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                removeItem(position);
//                return true;
//            }
//        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            int position = data.getExtras().getInt("position");
            long timestamp = data.getExtras().getLong("timestamp");
            TodoItem todoItem = mRealm.where(TodoItem.class).equalTo("mTimestamp", timestamp).findFirst();
            updateItem(position, todoItem);
        }
    }

    public void onAddItem(View view) {
        String itemText = etNewItem.getText().toString();
        TodoItem item = new TodoItem();
        item.setTimestamp(System.currentTimeMillis());
        item.setTitle(itemText);
        mRealm.beginTransaction();
        mRealm.copyToRealm(item);
        mRealm.commitTransaction();

        etNewItem.setText("");
        dismissKeyboard(etNewItem);
    }

    private void updateItem(int position, TodoItem item) {
        Log.d(TAG, "position/content:" + position + "/" + item);
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(item);
        mRealm.commitTransaction();
        mItemsAdapter.notifyItemChanged(position);
    }

    private void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
