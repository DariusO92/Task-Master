package com.task_master.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.task_master.R;

public class LoginActivity extends AppCompatActivity {
    public static final String Tag = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton();
    }

    private void loginButton(){
        Button loginButton = findViewById(R.id.LoginbuttonId);
        Intent callingIntent = getIntent();
        String userEmail = callingIntent.getStringExtra(VerifyActivity.VERIFY_ACCOUNT_EMAIL_TAG);
        EditText emailEditText = findViewById(R.id.LoginEmailId);
        emailEditText.setText(userEmail);
        findViewById(R.id.LoginbuttonId).setOnClickListener(view ->{
            String userSelectedEmail = emailEditText.getText().toString();
            String userPassword = ((EditText) findViewById(R.id.LoginPasswordEditText)).getText().toString();

            Amplify.Auth.signIn(
                    userSelectedEmail,
                    userPassword,
                    success -> {
                        Log.i(Tag, "Login Succeeded: " + success);
                        Intent goToTaskListIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(goToTaskListIntent);
                    },
                    failure -> {
                        Log.i(Tag, "Login failed: " + failure);
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show());

                    }
            );

        });

    }
}