package com.example.android_lab_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class GalleryActivity extends AppCompatActivity {

    private int currentImage = 0;
    private ArrayList<String> images;
    private ImageView imageView;
    private TextView nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Button nextBtn = findViewById(R.id.gallery_next_btn);
        Button prevBtn = findViewById(R.id.gallery_prev_btn);
        nameView = findViewById(R.id.gallery_text_view);

        nextBtn.setOnClickListener(this::onNext);
        prevBtn.setOnClickListener(this::onPrevious);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentImage = 0;
        images = new ArrayList<>();
        imageView = (findViewById(R.id.gallery_image_view));
        String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Camera/CameraExamples";
        try {
            File imagesDirectory = new File(folder);
            images = searchImage(imagesDirectory);
            updatePhoto(Uri.parse(images.get(currentImage)));
        } catch (Exception e) {
            nameView.setText(String.format("Error: Folder ’%s’ not found", folder));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        images.clear();
        System.out.println("onPause cI=" + currentImage);
    }

    private ArrayList<String> searchImage(File dir) {
        ArrayList<String> imagesFound = new ArrayList<>();
        for (File f : Objects.requireNonNull(dir.listFiles())) {
            if (!f.isDirectory()) {
                String fileExt = getFileExt(f.getAbsolutePath());
                if (fileExt.equals("png") || fileExt.equals("jpg") || fileExt.equals("jpeg")) {
                    System.out.println("File was founded " + f.getAbsolutePath());
                    imagesFound.add(f.getAbsolutePath());
                }
            }
        }
        return imagesFound;
    }

    public static String getFileExt(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public void updatePhoto(Uri uri) {
        try {
            nameView.setText((currentImage + 1) + "/" + images.size());
            imageView.setImageURI(uri);
        } catch (Exception e) {
            nameView.setText("Error while loading file");
        }
    }

    public void onNext(View v) {
        if (currentImage + 1 < images.size() && images.size() > 0) {
            currentImage++;
            updatePhoto(Uri.parse(images.get(currentImage)));
        }
    }

    public void onPrevious(View v) {
        if (currentImage > 0 && images.size() > 0) {
            currentImage--;
            updatePhoto(Uri.parse(images.get(currentImage)));
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