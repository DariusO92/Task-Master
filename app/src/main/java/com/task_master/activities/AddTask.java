package com.task_master.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.*;

import androidx.appcompat.app.AppCompatActivity;
import  com.amplifyframework.core.Amplify;
import com.task_master.R;

import java.util.Date;

public class AddTask extends AppCompatActivity {
    public static  final String Tag = "AddTaskActivities";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setUpSubmitButton();
        setUpTypeSpinner();
    }

    private void setUpTypeSpinner(){
        Spinner addTaskTypeSpinner = findViewById(R.id.AddTaskSpinner);
        addTaskTypeSpinner.setAdapter(new ArrayAdapter<>(
            this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, StateEnum.values()
        ));
    }
       private void setUpSubmitButton(){
        Spinner addTaskTypeSpinner = findViewById(R.id.AddTaskSpinner);
        Button saveNewTaskButton = findViewById(R.id.addTaskButtonId);
        saveNewTaskButton.setOnClickListener(view -> {
            String taskName = ((EditText) findViewById(R.id.editTaskTextId)).getText().toString();
            String taskDescription = ((EditText) findViewById(R.id.editDescriptionId)).getText().toString();
            String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());

            TaskModel newAddTask = TaskModel.builder()
                    .name(taskName)
                    .description(taskDescription)
                    .dateCreated(new Temporal.DateTime(currentDateString))
                    .state((StateEnum)addTaskTypeSpinner.getSelectedItem())
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(newAddTask),
                    successResponse -> Log.i(Tag,"Task added successfully"),
                    failureResponse -> Log.i(Tag, "Task failed with this response: " + failureResponse)
            );

            Intent goToMainActivity = new Intent(AddTask.this, MainActivity.class);
            startActivity(goToMainActivity);

        });
       }

}
