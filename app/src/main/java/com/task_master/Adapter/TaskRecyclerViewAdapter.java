package com.task_master.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.amplifyframework.datastore.generated.model.TaskModel;
//import com.task_master.Model.TaskModel;
import com.task_master.R;
import com.task_master.activities.AddTask;
import com.task_master.activities.MainActivity;
import com.task_master.activities.TaskDetails;

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

   // @Override
    /*public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        TextView taskFragmentNameTextView = holder.itemView.findViewById(R.id.TaskFragmentTextViewId);
        String taskTitle = tasks.get(position).getTitle();
        taskFragmentNameTextView.setText(position + ". "  + taskTitle);
        TextView taskFragTextViewType = holder.itemView.findViewById(R.id.TaskFragmentTextViewId);


        View taskListViewHolder = holder.itemView;
        taskListViewHolder.setOnClickListener(view -> {
            Intent goToAddTask = new Intent(callingActivity, AddTask.class);
            goToAddTask.putExtra(AddTask.Tag, taskTitle);
            callingActivity.startActivity(goToAddTask);
        });
    }*/

  @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        TextView taskFragmentTextView = holder.itemView.findViewById(R.id.TaskFragmentTextViewId);
        String task = tasks.get(position).getId();
        String taskName = tasks.get(position).getName();
        String taskDescription = tasks.get(position).getDescription();
        String taskState = tasks.get(position).getState().toString();
        taskFragmentTextView.setText(taskName + "\n" + taskState);

        View taskViewHolder = holder.itemView;
        taskViewHolder.setOnClickListener(view -> {
            Intent goToTaskDetail = new Intent(callingActivity, TaskDetails.class);
            goToTaskDetail.putExtra(MainActivity.TASK_NAME, taskName);
            goToTaskDetail.putExtra(MainActivity.TASK_DESCRIPTION, taskDescription);
            goToTaskDetail.putExtra(MainActivity.TASK_STATE, taskState);
            goToTaskDetail.putExtra(MainActivity.Task_ID, task );
            callingActivity.startActivity(goToTaskDetail);
        });
   }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskListViewHolder extends RecyclerView.ViewHolder{
        public TaskListViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }
}
