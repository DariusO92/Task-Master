package com.task_master.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ImageView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskModel;
import com.task_master.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class TaskImage extends AppCompatActivity {
    public static final String Tag = "ImageTaskActivity";
    ActivityResultLauncher<Intent> activityResultLauncher;
    private String s3ImageKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_image);

        activityResultLauncher = getImagePickingActivityResultLauncher();

        SetupAddImageButton();
        setupSaveButton();
    }

    private void setupSaveButton(){
        findViewById(R.id.ImageTaskActivitySaveBttn).setOnClickListener(view ->{
            saveTask(s3ImageKey);
        });
    }

    private void saveTask(String s3key){

        TaskModel taskToSave = TaskModel.builder()
                .name("Trash")
                .productImageS3Key(s3key)
                .build();


        Amplify.API.mutate(
                ModelMutation.create(taskToSave),
                success -> Log.i(Tag, "Successfully created new trainer with s3Imagekey");
                failure -> Log.i(Tag, "Failed to create"+ failure.getMessage())
        );
    }

  private void LaunchImageSelectionIntent(){
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg","image/png" });

        activityResultLauncher.launch(imageFilePickingIntent);

    }

    private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher(){
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Uri pickedImageFileUri = result.getData().getData();
                        try {
                            InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                            String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                            uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename, pickedImageFileUri);

                        } catch (FileNotFoundException fnfe){
                            Log.e(Tag,"Could not get file from file picker!" + fnfe.getMessage(), fnfe);
                        }
                    }
                }
        );
        return imagePickingActivityResultLauncher;
    }

    private void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFileName, Uri pickedImageFileUri){
        Amplify.Storage.uploadInputStream(
                pickedImageFileName,
                pickedImageInputStream,
                success -> {
                    Log.i(Tag, "Success" + success.getKey());
                    ImageView taskImageView = findViewById(R.id.ImageTaskImageView);
                    InputStream pickedImageInputStreamCopy = null;
                    try {
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                    } catch (FileNotFoundException fnfe) {
                        Log.e(Tag, "Could not get file stream from Uri" + fnfe.getMessage(), fnfe);

                    }
                    taskImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStream));
                },
                failure -> Log.e (Tag, "Faliure" + pickedImageFileName + " with error:" + failure.getMessage())

        );
    }
    // Taken from https://stackoverflow.com/a/25005243/16889809
    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}