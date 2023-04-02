package com.example.android_lab_5;

import android.app.AlertDialog;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {
    private GestureLibrary gestureLibrary;
    private ArrayList<Integer> calculatorData;
    ArrayList<Integer> numbers;
    private Integer number;
    Random randNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GestureOverlayView gestures = findViewById(R.id.gestures);

        calculatorData = new ArrayList<>();
        randNumber = new Random();
        numbers = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,9));
        Collections.shuffle(numbers);
        number = numbers.get(0);
        TextView clue = findViewById(R.id.number);
        clue.setText(number.toString());


        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gesture);

        if (!gestureLibrary.load()) {
            finish();
        }

        gestures.addOnGesturePerformedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            Intent i = new Intent(this, InfoActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
        ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
        if (predictions.size() > 0 && predictions.get(0).score > 1) {
            String predictionName = predictions.get(0).name;

            if (!Integer.valueOf(predictionName).equals(number)){
                Toast.makeText(this, "Try one more time", Toast.LENGTH_SHORT).show();
            } else{
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder
                        .setTitle("Вы выиграли")
                        .setIcon(R.drawable.baseline_emoji_events_24)
                        .setMessage("Диалоговое окно")
                        .setPositiveButton("OK", null)
                        .create().show();
                Collections.shuffle(numbers);
                number = numbers.get(0);
                TextView clue = findViewById(R.id.number);
                clue.setText(number.toString());
            }

        }
    }

}