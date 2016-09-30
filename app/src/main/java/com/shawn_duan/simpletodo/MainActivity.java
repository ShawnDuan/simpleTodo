package com.shawn_duan.simpletodo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
    FloatingActionButton fabAddItem;

    public final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealm = Realm.getDefaultInstance();
        mAllTodoItems = mRealm.where(TodoItem.class).findAll();

        setContentView(R.layout.activity_main);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        mItemsAdapter = new TodoListAdapter(MainActivity.this, mAllTodoItems, "timestamp");
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(mItemsAdapter);
        fabAddItem = (FloatingActionButton) findViewById(R.id.fabAddItem);
        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                CreateItemDialogFragment createItemDialogFragment = new CreateItemDialogFragment();
                createItemDialogFragment.show(fm, "fragment_create_todo");
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            int position = data.getExtras().getInt("position");
            long timestamp = data.getExtras().getLong("timestamp");
            final TodoItem todoItem = mRealm.where(TodoItem.class).equalTo("timestamp", timestamp).findFirst();
            updateItem(position, todoItem);
        }
    }

    private void updateItem(int position, final TodoItem item) {
        Log.d(TAG, "position/content:" + position + "/" + item);
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(item);
            }
        });
        mItemsAdapter.notifyItemChanged(position);
    }
}
