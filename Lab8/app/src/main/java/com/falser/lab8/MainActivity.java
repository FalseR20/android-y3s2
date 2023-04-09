package com.falser.lab8;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TableLayout plates;
    ArrayList<Integer> setsIds;
    ArrayDeque<PlateButton> pickedButtons = new ArrayDeque<>();
    Chronometer chronometer;
    TextView triesCounter;
    Integer platesLeft;
    Integer triesLeft;
    Boolean isGameStarted;
    Boolean isGameFinished;
    Integer width;
    Integer height;
    Integer setLen;
    Integer level;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Integer difficulty = Integer.parseInt(prefs.getString("difficulty", "1"));
        Log.d("difficulty", String.format("Difficulty is %d", difficulty));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createPlates();
        restart();
        createSetsIds();
    }

    void setFirstLevel() {
        level = 1;
        changeLevel();
    }

    void levelUp() {
        level++;
        width++;
        height++;
        changeLevel();
    }

    void changeLevel() {

    }

    void createPlates() {
        plates = findViewById(R.id.plates);
        TableRow row;
        for (int i = 0; i < width; i++) {
            row = new TableRow(this);
            for (int j = 0; j < height; j++) {
                PlateButton plateButton = new PlateButton(this);
                plateButton.setOnClickListener(this);
                row.addView(plateButton, j);
            }
            plates.addView(row, i);
        }
        chronometer = findViewById(R.id.chronometer);
        triesCounter = findViewById(R.id.triesCounter);
    }

    void restart() {
        width = R.integer.start_width;
        height = R.integer.start_height;
        setLen = R.integer.start_set_len;
        restartPlates();
    }

    public void restartOnClick(View view) {
        restart();
    }

    private void createSetsIds() {
        setsIds = new ArrayList<>();
        for (int i = 0; i < width * height; i++)
            setsIds.add(i / Constants.SET_LEN);
    }

    public void restartPlates() {
        randomizeSetsIds();
        TableRow row;
        for (int i = 0, k = 0; i < Constants.N_ROWS; i++) {
            row = (TableRow) plates.getChildAt(i);
            for (int j = 0; j < Constants.N_COLUMNS; j++, k++) {
                PlateButton image = (PlateButton) row.getChildAt(j);
                image.number = setsIds.get(k);
                image.is_chosen = false;
                image.setText("");
            }
        }
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        platesLeft = Constants.N_SETS;
        triesLeft = Constants.N_TRIES;
        triesCounter.setText(String.valueOf(triesLeft));
        triesCounter.setTextColor(Color.WHITE);
        pickedButtons.clear();
        isGameStarted = false;
        isGameFinished = false;
    }

    private void randomizeSetsIds() {
        Collections.shuffle(setsIds);
        Log.d(this.getClass().getName(), String.format("Random ints: %s", setsIds));
    }

    @Override
    public void onClick(View view) {
        PlateButton plateButton = (PlateButton) view;
        if (!isGameStarted) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            isGameStarted = true;
        }
        if (isGameFinished) {
            Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show();
            return;
        }
        if (plateButton.is_chosen || pickedButtons.contains(plateButton)) {
            return;
        }

        triesLeft--;
        triesCounter.setText(String.valueOf(triesLeft));
        if (triesLeft == 9) {
            triesCounter.setTextColor(Color.YELLOW);
        }
        if (triesLeft == 0) {
            triesCounter.setTextColor(Color.RED);
            isGameFinished = true;
            chronometer.stop();
        }
        if (pickedButtons.size() > 0) pickedButtons.getLast().setText("");
        pickedButtons.addLast(plateButton);
        plateButton.setText(String.valueOf(plateButton.number));

        if (pickedButtons.size() == Constants.SET_LEN) {
            if (checkAllEqual()) {
                for (PlateButton button : pickedButtons) {
                    button.is_chosen = true;
                    button.setText(String.valueOf(button.number));
                }
                pickedButtons.clear();
                platesLeft--;
                if (platesLeft == 0) {
                    if (isGameFinished) {
                        Toast.makeText(this, R.string.almost_losing, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.win, Toast.LENGTH_SHORT).show();
                    }
                    isGameFinished = true;
                    chronometer.stop();
                    return;
                }
            } else {
                pickedButtons.pop();
            }
        }


        if (isGameFinished) {
            Toast.makeText(this, R.string.lost, Toast.LENGTH_SHORT).show();
        }
    }

    boolean checkAllEqual() {
        int check = pickedButtons.getFirst().number;
        for (PlateButton button : pickedButtons) {
            if (check != button.number) return false;
        }
        return true;
    }

}

