package com.task_master.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.task_master.Model.TaskModel;
import com.task_master.R;
import com.task_master.activities.MainActivity;
import java.util.List;


public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskListViewHolder> {
    List<TaskModel> tasks;
    Context callingActivity;

    public TaskRecyclerViewAdapter(List<TaskModel> tasks, Context callingActivity){
        this.tasks = tasks;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task, parent, false);
        return new TaskListViewHolder(taskFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecyclerViewAdapter.TaskListViewHolder holder, int position) {
        TextView taskFragmentNameTextView = holder.itemView.findViewById(R.id.TaskFragmentTextViewId);

    }

//    @Override
//    public void onBindViewHolder(@NonNull TaskRecyclerViewAdapter.TaskListViewHolder holder, int position) {
//    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class TaskListViewHolder extends RecyclerView.ViewHolder{
        public TaskListViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }
}
