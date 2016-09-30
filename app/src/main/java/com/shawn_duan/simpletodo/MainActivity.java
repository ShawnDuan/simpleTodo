package com.shawn_duan.simpletodo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.shawn_duan.simpletodo.models.TodoItem;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements CreateOrEditItemDialogFragment.EditNameDialogListener {
    private final static String TAG = "MainActivity";
    private Realm mRealm;
    private RealmResults<TodoItem> mAllTodoItems;
    TodoListAdapter mItemsAdapter;
    RecyclerView rvItems;
    FloatingActionButton fabAddItem;

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
                CreateOrEditItemDialogFragment createOrEditItemDialogFragment = CreateOrEditItemDialogFragment.newInstance();     // new todo item
                createOrEditItemDialogFragment.show(fm, "fragment_create_todo");
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onFinishEditDialog(int position) {
        mItemsAdapter.notifyItemChanged((position == -1) ? mAllTodoItems.size() : position);
//        mItemsAdapter.notifyDataSetChanged();
    }

}
