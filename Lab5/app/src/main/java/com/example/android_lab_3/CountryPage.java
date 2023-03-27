package com.example.android_lab_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class CountryPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_page);

        TextView countryTitleView = findViewById(R.id.country_title);
        ImageView countryImageView = findViewById(R.id.country_image);

        Intent intent = getIntent();
        String countryTitle = intent.getStringExtra("title");
        String countryVideoUrl = intent.getStringExtra("videoUrl");
        int pictureId = intent.getIntExtra("pictureId", R.drawable.placeholder);

        countryTitleView.setText(countryTitle);
        countryImageView.setImageResource(pictureId);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.country_youtube_player);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = countryVideoUrl.substring(countryVideoUrl.indexOf("v=") + 2);
                youTubePlayer.loadVideo(videoId, 0);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        showSnackBar(countryTitle);
    }

    private void showSnackBar(String from) {
        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "You picked " + from,
                Snackbar.LENGTH_LONG
        );
        snackbar.show();
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