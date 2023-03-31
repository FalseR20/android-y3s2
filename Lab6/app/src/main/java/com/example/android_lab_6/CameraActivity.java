package com.example.android_lab_6;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        SurfaceView preview = findViewById(R.id.surfaceViewCamera);
        SurfaceHolder surfaceHolder = preview.getHolder();
        surfaceHolder.addCallback(new MyCallback());

        ImageButton shotBtn = findViewById(R.id.shot_btn);
        shotBtn.setOnClickListener(view -> camera.autoFocus(new MyAutoFocusCallback()));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera = Camera.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.release();
    }

    private class MyCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        }
    }

    private class MyAutoFocusCallback implements Camera.AutoFocusCallback {
        @Override
        public void onAutoFocus(boolean b, Camera camera) {
            if (b) {
                camera.takePicture(null, null, null, new MyPictureCallback());
            }
        }
    }

    private class MyPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] paramArrayOfByte, Camera paramCamera) {
            String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Camera/CameraExamples";
            File myDir = new File(folder);
            myDir.mkdirs();

            String fileName = "Image-" + System.currentTimeMillis() + ".jpg";
            File file = new File(myDir, fileName);
            System.out.println(file.getAbsolutePath());
            if (file.exists()) file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                out.write(paramArrayOfByte);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            paramCamera.startPreview();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}