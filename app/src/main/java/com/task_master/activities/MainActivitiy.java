package com.task_master.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.task_master.R;

public class MainActivitiy  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    goTooAddTaskBtn();
    goToAllTaskBtn();

    }
        public void goTooAddTaskBtn(){
            Button addTaskButton = MainActivitiy.this.findViewById(R.id.addTaskButton);
            addTaskButton.setOnClickListener(view ->{
              Intent goToAddTaskFromIntent = new Intent(MainActivitiy.this, AddTask.class );
              startActivity(goToAddTaskFromIntent);
            });
        }


        public void goToAllTaskBtn(){
        Button allTaskButton = MainActivitiy.this.findViewById(R.id.allTaskButton);
        allTaskButton.setOnClickListener(view -> {
            Intent goToAllTaskFromIntent = new Intent(MainActivitiy.this, AllTask.class);
        });
        }
}
