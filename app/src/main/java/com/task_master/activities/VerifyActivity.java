package com.task_master.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.task_master.R;

public class VerifyActivity extends AppCompatActivity {
    public static final String Tag = "VerifyAccountActivity";
    public static final String VERIFY_ACCOUNT_EMAIL_TAG = "Verify_Account_Email_Tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
    }
}