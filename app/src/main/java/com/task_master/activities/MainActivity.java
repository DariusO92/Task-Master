package com.task_master.activities;

import static com.task_master.activities.AppSettings.USER_NAME_TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.nfc.Tag;
import android.os.Bundle;



import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.preference.PreferenceManager;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.task_master.Adapter.TaskRecyclerViewAdapter;
import com.task_master.Model.TaskModel;
import com.task_master.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_TITlE = "task title";
    public static final String Tag = "AddTask";
    SharedPreferences preferences;

    List<TaskModel> tasks = null;
    TaskRecyclerViewAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpTaskRecyclerView();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tasks = new ArrayList<>();
        goTooAddTaskBtn();
        goToAllTaskBtn();
        goToAppSettingsImgButton();
    }

    private void setUpTaskRecyclerView(){
        RecyclerView taskRecyclerView = findViewById(R.id.TaskRecyclerViewID);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskRecyclerView.setLayoutManager(layoutManager);

//        List<TaskModel> tasks = new ArrayList<>();
//
//        tasks.add(new TaskModel());
//        tasks.add(new TaskModel());
//        tasks.add(new TaskModel());
//        tasks.add(new TaskModel());
//        tasks.add(new TaskModel());

         adapter = new TaskRecyclerViewAdapter(tasks, this);
        taskRecyclerView.setAdapter(adapter);
    }

    @Override
    protected  void onResume(){
        super.onResume();
        Amplify.API.query(
                ModelQuery.list(com.amplifyframework.datastore.generated.model.TaskModel.class),
                successResponse -> {
                    Log.i(Tag, "Read Tasks Successfully!");
                    tasks.clear();
                    for (com.amplifyframework.datastore.generated.model.TaskModel tasks : successResponse.getData()){
                        //tasks.add(tasks);
                    }
                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });
                },
                failureResponse -> Log.i(Tag, "Did not Read Task Successfully")
        );

//        String userName = preferences.getString(USER_NAME_TAG, "No name");
//        TextView userNameDisplay = findViewById(R.id.userNameId);
//        userNameDisplay.setText(userName);
    }
    public void goTooAddTaskBtn(){
        Button addTaskButton = MainActivity .this.findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(view ->{
            Intent goToAddTaskFromIntent = new Intent(MainActivity.this, AddTask.class );
            startActivity(goToAddTaskFromIntent);
        });
    }


    public void goToAllTaskBtn(){
        Button allTaskButton = MainActivity.this.findViewById(R.id.allTaskButton);
        allTaskButton.setOnClickListener(view -> {
            Intent goToAllTaskFromIntent = new Intent(MainActivity.this, AllTask.class);
            startActivity(goToAllTaskFromIntent);

        });
    }

    public void goToAppSettingsImgButton(){
        ImageView appImage = MainActivity.this.findViewById(R.id.imageButtonid);
        appImage.setOnClickListener(view ->{
            Intent goToAppSettingsFromIntent = new Intent(MainActivity.this, AppSettings.class);
            startActivity(goToAppSettingsFromIntent);
        });
    }
}
