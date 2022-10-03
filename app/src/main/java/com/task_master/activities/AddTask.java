package com.task_master.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.*;

import androidx.appcompat.app.AppCompatActivity;
import  com.amplifyframework.core.Amplify;
import com.task_master.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTask extends AppCompatActivity {
    public static  final String Tag = "AddTaskActivities";

    Spinner teamSpinner = null;
    CompletableFuture<List<Team>> teamFuture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setUpSubmitButton();
        setUpTypeSpinner();
    }
    private void setUpTeamSpinner(){
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(Tag, "Read team successfully");
                    ArrayList<String> teamNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    for (Team team : success.getData()){
                        teams.add(team);
                        teamNames.add(team.getName());
                    }
                    teamFuture.complete(teams);
                    runOnUiThread(() -> {
                        teamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_list_item,
                                teamNames));

                    });
                },
                failure -> {
                    teamFuture.complete(null);
                    Log.i(Tag, "Did not Read teams successfully");
                }
        );
    }
    private void setUpTypeSpinner(){

        Spinner addTaskTypeSpinner = findViewById(R.id.AddTaskSpinner);
        addTaskTypeSpinner.setAdapter(new ArrayAdapter<>(
            this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, StateEnum.values()
        ));
    }
       private void setUpSubmitButton(){
        String selectedTeamString = teamSpinner.getSelectedItem().toString();
        List<Team> teams = null;
        try{
            teams = teamFuture.get();
        } catch (InterruptedException ie){
            Log.e(Tag, "Interupted Exception while getting teams");
            Thread.currentThread().interrupt();
        } catch (ExecutionException ee){
            Log.e(Tag, "ExecutionException while getting teams" + ee.getMessage());
        }
        Team selectedTeam = teams.stream().filter(t -> t.getName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);

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
