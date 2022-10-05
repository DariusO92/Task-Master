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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.preference.PreferenceManager;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
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
    public AuthUser currentUser = null;

    List<TaskModel> tasks = null;
    TaskRecyclerViewAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpTaskRecyclerView();
        currentUser = Amplify.Auth.getCurrentUser();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tasks = new ArrayList<>();
        /*Amplify.Auth.fetchAuthSession(
                result -> Log.i("AmplifyQuickstart", result.toString()),
                error -> Log.e("AmplifyQuickstart", error.toString())
        );
        Amplify.Auth.signUp("darius.owens.j@gmail.com",
                "p@ssw0rd", AuthSignUpOptions.builder()
                        .userAttribute(AuthUserAttributeKey.email(),"darius.owens.j@gmail.com")
                        .userAttribute(AuthUserAttributeKey.nickname(),"BigO")
                        .build(),
        success -> Log.i(Tag, "Signup success!" + success),
        failure -> Log.i(Tag, "Signup failed with username" + "darius.owens.j@gmail.com" + "with message: " + failure)
                    );

        Amplify.Auth.confirmSignUp("darius.owens.j@gmail.com", "147600",
                success -> Log.i(Tag, "Verification succeeded: " + success),
                failure -> Log.i(Tag, "Verification Failed: " + failure)
                    );

        Amplify.Auth.signIn("darius.owens.j@gmail.com",
                       "p@ssw0rd",
                    success -> Log.i(Tag, "Login succeeded: " + success.toString()),
                    failure -> Log.i(Tag, "Login failed: " + failure.toString())
                    );
        Amplify.Auth.fetchAuthSession(
                result -> Log.i("AmplifyQuickstart", result.toString()),
                error -> Log.e("AmplifyQuickstart", error.toString())
        );


        Amplify.Auth.signOut(
                () ->
                {
                    Log.i(Tag, "Logout succeeded!");
                },
                failure ->
                {
                    Log.i(Tag, "Logout failed: " + failure.toString());
                }
        );*/

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

    private void setUpLoginSignUpButton(){
        Button loginButton = findViewById(R.id.MainLoginButtonId);
        Button signupButton = findViewById(R.id.MainSigUpButtonId);

        if (currentUser == null){
            loginButton.setVisibility(View.VISIBLE);
            signupButton.setVisibility(View.VISIBLE);
        } else {
            loginButton.setVisibility(View.INVISIBLE);
            signupButton.setVisibility(View.INVISIBLE);
        }
        loginButton.setOnClickListener(view -> {
            Intent goToLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToLoginActivity);
        });
        signupButton.setOnClickListener(view -> {
            Intent goToSignUpActivity = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(goToSignUpActivity);
        });
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
