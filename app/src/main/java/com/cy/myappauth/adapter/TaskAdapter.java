package com.cy.myappauth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cy.myappauth.R;
import com.cy.myappauth.model.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final OnItemSelectedCallback callback;
    private List<Task> items = new ArrayList();
    private LayoutInflater inflater;

    public TaskAdapter(
            final Context context,
            final OnItemSelectedCallback callback) {

        this.callback = callback;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TaskViewHolder onCreateViewHolder(
            ViewGroup viewGroup,
            int i) {

        View v = inflater.inflate(R.layout.item_task, viewGroup, false);
        TaskViewHolder viewHolder = new TaskViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            TaskViewHolder viewHolder,
            int i) {
        Task task = items.get(i);
        viewHolder.setItem(task);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setItems(final List<Task> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface OnItemSelectedCallback {

        void onClick(final Task ride);

    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView description;

        TaskViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            callback.onClick((Task) v.getTag());
        }

        public void setItem(final Task task) {
            itemView.setTag(task);
            title.setText(task.getTitle());
            description.setText(task.getDescription());
        }
    }
}