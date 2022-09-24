package com.task_master.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.task_master.R;

public class AppSettings extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String USER_NAME_TAG = "userName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = sharedPreferences.getString(USER_NAME_TAG, "");
        if (!userName.isEmpty()){
            EditText userNameEdited = findViewById(R.id.editTextTextPersonName);
            userNameEdited.setText(userName);
        }
        setUpSubmitButton();
    }

    private void setUpSubmitButton(){
        Button submitButton = findViewById(R.id.UserProfileSubmitButton);
        submitButton.setOnClickListener(view ->{
            SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
            String nameInput = ((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString();
            preferenceEditor.putString(USER_NAME_TAG, nameInput);
            preferenceEditor.apply();

            Toast.makeText(AppSettings.this, "Settings saved", Toast.LENGTH_SHORT).show();
        });
    }
}