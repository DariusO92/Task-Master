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

public class VerifyActivity extends AppCompatActivity {
    public static final String Tag = "VerifyAccountActivity";
    public static final String VERIFY_ACCOUNT_EMAIL_TAG = "Verify_Account_Email_Tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        verifyButton();
    }


    private void verifyButton(){
        Button verifyButton = findViewById(R.id.VerifyButton);
        Intent callingIntent = getIntent();
        String userEmail = callingIntent.getStringExtra(SignUpActivity.SignUp_Email_Tag);
        findViewById(R.id.VerifyButton).setOnClickListener(view -> {
            String verificationCode = ((EditText) findViewById(R.id.VerifyCodeEditText)).getText().toString();

            Amplify.Auth.signIn(
                    userEmail,
                    verificationCode,
                    success -> {
                        Log.i(Tag, "Verification Succeeded: " + success.toString());
                        Intent goToLoginActivity = new Intent(VerifyActivity.this, LoginActivity.class);
                        goToLoginActivity.putExtra(VERIFY_ACCOUNT_EMAIL_TAG, userEmail);
                        startActivity(goToLoginActivity);
                    },
                    failure -> {
                        Log.i(Tag, "Verification failed with username: " + failure);
                        runOnUiThread(() -> Toast.makeText(VerifyActivity.this, "Verify account failed!", Toast.LENGTH_SHORT).show());

                    }
            );
        });
    }
}