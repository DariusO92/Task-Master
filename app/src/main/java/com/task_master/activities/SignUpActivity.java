package com.task_master.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.task_master.R;

public class SignUpActivity extends AppCompatActivity {
    public static final String Tag = "SignUpActivity";
    public static final String SignUp_Email_Tag = "SignUp_Tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void signupButton(){
        Button signupButton = findViewById(R.id.SignUpButtonid);
        signupButton.setOnClickListener( view ->{
            String email = ((EditText) findViewById(R.id.signupEmailId)).getText().toString();
            String userName = ((EditText) findViewById(R.id.signupUserNameId)).getText().toString();
            String password = ((EditText) findViewById(R.id.signupPasswodId)).getText().toString();

            Amplify.Auth.signUp(email, password, userName, AuthSignUpOptions.builder()
                            .AuthSignUpOptions.builder()
                            .userAttribute(AuthUserAttributeKey.email(),email)
                            .userAttribute(AuthUserAttributeKey.nickname(), userName)
                            .build(),
                    success -> {
                        Log.i(Tag, "Signup success!" + success);
                        Intent goToVerifyActivity = new Intent(SignUpActivity.this, VerifyActivity.class);
                        goToVerifyActivity.putExtra(SignUp_Email_Tag, email);
                        startActivity(goToVerifyActivity);
                    },
                    failure -> {
                        Log.i(Tag, "Signup failed with username" + email + "with message: " + failure);
                        runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Signup Failed!", Toast.LENGTH_SHORT).show());
                    }
            );
        });
    }
}