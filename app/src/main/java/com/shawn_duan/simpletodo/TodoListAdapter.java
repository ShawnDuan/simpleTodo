package com.shawn_duan.simpletodo;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.shawn_duan.simpletodo.models.TodoItem;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by sduan on 9/27/16.
 */

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ItemViewHolder> {
    private MainActivity mActivity;
    private OrderedRealmCollection<TodoItem> mTodoItemSet;

    public TodoListAdapter(Activity activity, RealmResults<TodoItem> todoItemSet, String sortBy) {
        mActivity = (MainActivity) activity;
        mTodoItemSet = todoItemSet.sort(sortBy);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listed_item, parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        TextView title = (TextView) holder.itemView.findViewById(R.id.itemTitle);
        TextView timestamp = (TextView) holder.itemView.findViewById(R.id.itemTimestamp);
        TodoItem item = mTodoItemSet.get(position);
        title.setText(item.getTitle());
        timestamp.setText(convertTime(item.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return mTodoItemSet.size();
    }

    public void add(int position, TodoItem item) {
        mTodoItemSet.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(TodoItem item) {
        int position = mTodoItemSet.indexOf(item);
        mTodoItemSet.remove(position);
        notifyItemRemoved(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private AdapterView.OnItemClickListener onItemClickListener;
        public ItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    long timestamp = mTodoItemSet.get(position).getTimestamp();

                    FragmentManager fm = mActivity.getSupportFragmentManager();
                    CreateItemDialogFragment createItemDialogFragment =
                            CreateItemDialogFragment.newInstance(position, timestamp);     // edit existing todo item
                    createItemDialogFragment.show(fm, "fragment_edit_todo");
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    final TodoItem item = mTodoItemSet.get(position);
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            item.deleteFromRealm(); // Delete and remove object directly
                        }
                    });
                    notifyItemRemoved(position);
                    return true;
                }
            });
        }
    }

    private String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format.format(date);
    }
}
