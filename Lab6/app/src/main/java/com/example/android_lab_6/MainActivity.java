package com.example.android_lab_6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton cameraBtn = findViewById(R.id.camera_btn);
        ImageButton playBtn = findViewById(R.id.play_btn);
        ImageButton galleryBtn = findViewById(R.id.gallery_btn);

        cameraBtn.setOnClickListener(view -> {
            Intent i = new Intent(this, CameraActivity.class);
            startActivity(i);
        });

        playBtn.setOnClickListener(view -> {
            Intent i = new Intent(this, MediaActivity.class);
            startActivity(i);
        });

        galleryBtn.setOnClickListener(view -> {
            Intent i = new Intent(this, GalleryActivity.class);
            startActivity(i);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            Intent i = new Intent(this, InfoActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}