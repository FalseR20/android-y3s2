package com.example.android_lab_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

public class MediaActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer;
    private CheckBox checkBoxLoop;
    private  Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        checkBoxLoop = findViewById(R.id.media_checkbox_loop);

        Button startPlayBtn = findViewById(R.id.media_play_btn);
        Button pauseBtn = findViewById(R.id.media_pause_btn);
        Button resumeBtn = findViewById(R.id.media_resume_btn);
        Button stopBtn = findViewById(R.id.media_stop_btn);

        startPlayBtn.setOnClickListener(this::onClickStart);
        pauseBtn.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
        });
        resumeBtn.setOnClickListener(view -> {
            if (!mediaPlayer.isPlaying())
                mediaPlayer.start();
        });
        stopBtn.setOnClickListener(view -> {
            mediaPlayer.stop();
        });

        checkBoxLoop.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(isChecked);
            }
        });

        spinner = findViewById(R.id.media_spinner);
        String[] items = new String[]{"Video #1", "Video #2", "Video #3", "Video #4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onClickStart(View view) {
        releaseMP();

        int resourceId = getResourceId(spinner.getSelectedItem().toString());

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(this, Uri.parse("android.resource://com.example.android_lab_6/" + resourceId));
            mediaPlayer.setDisplay(((SurfaceView) findViewById(R.id.surfaceViewMedia)).getHolder());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            showMessage();
        }

        if (mediaPlayer == null)
            return;

        mediaPlayer.setLooping(checkBoxLoop.isChecked());
        mediaPlayer.setOnCompletionListener(this);
    }

    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showMessage() {
        Toast toast = Toast.makeText(getApplicationContext(), "Error while playing", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
    }

    private int getResourceId(String currentSpinnerItem) {
        switch (currentSpinnerItem) {
            case "Video #1": {
                return R.raw.video1;
            }
            case "Video #2": {
                return R.raw.video2;
            }
            case "Video #3": {
                return R.raw.video3;
            }
            case "Video #4": {
                return R.raw.video4;
            }
        }
        return R.raw.video1;
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