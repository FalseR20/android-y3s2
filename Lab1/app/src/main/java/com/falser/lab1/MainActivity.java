package com.falser.lab1;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TableLayout rows;
    ArrayList<Integer> setsIds;

    ArrayDeque<PlateButton> pickedButtons = new ArrayDeque<>();
    Chronometer chronometer;
    TextView triesCounter;
    int platesLeft;
    int triesLeft;
    boolean isGameStarted;
    boolean isGameFinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        countConstants();
        update();
        createSettings();
    }

    void update() {
        createPlates();
        createSetsIds();
        restartPlates();
    }

    void createPlates() {
        rows = findViewById(R.id.plates);
        TableRow row;
        for (int i = 0; i < Constants.N_ROWS; i++) {
            row = new TableRow(this);
            for (int j = 0; j < Constants.N_COLUMNS; j++) {
                PlateButton plateButton = new PlateButton(this);
                plateButton.setOnClickListener(this);
                row.addView(plateButton, j);
            }
            rows.addView(row, i);
        }
        chronometer = findViewById(R.id.chronometer);
        triesCounter = findViewById(R.id.triesCounter);
    }

    private void createSetsIds() {
        setsIds = new ArrayList<>();
        for (int i = 0; i < Constants.N_PLATES; i++)
            setsIds.add(i / Constants.SET_LEN);
    }

    public void restartPlates() {
        randomizeSetsIds();
        TableRow row;
        for (int i = 0, k = 0; i < Constants.N_ROWS; i++) {
            row = (TableRow) rows.getChildAt(i);
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

    private void createSettings() {
        TableRow setting;
        TextView label;
        Spinner spinner;
        ArrayAdapter<CharSequence> adapter;

        setting = findViewById(R.id.settingRows);
        label = (TextView) setting.getChildAt(0);
        label.setText(R.string.count_of_rows);
        spinner = (Spinner) setting.getChildAt(1);
        adapter = ArrayAdapter.createFromResource(this, R.array.rows_array, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        setting = findViewById(R.id.settingColumns);
        label = (TextView) setting.getChildAt(0);
        label.setText(R.string.count_of_columns);
        spinner = (Spinner) setting.getChildAt(1);
        adapter = ArrayAdapter.createFromResource(this, R.array.columns_array, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        setting = findViewById(R.id.settingSetLen);
        label = (TextView) setting.getChildAt(0);
        label.setText(R.string.set_len);
        spinner = (Spinner) setting.getChildAt(1);
        adapter = ArrayAdapter.createFromResource(this, R.array.set_lengths, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
    }

    public void restartOnClick(View view) {
        restartPlates();
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

        pickedButtons.addLast(plateButton);
        plateButton.setText(String.valueOf(plateButton.number));

        if (pickedButtons.size() == Constants.SET_LEN) {
            if (checkAllEqual()) {
                for (PlateButton button : pickedButtons) button.is_chosen = true;
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
                pickedButtons.pop().setText("");
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

    public void onApply(View view) {
        TableRow setting;
        Spinner spinner;

        setting = findViewById(R.id.settingRows);
        spinner = (Spinner) setting.getChildAt(1);
        Constants.N_ROWS = Integer.parseInt((String) spinner.getSelectedItem());

        setting = findViewById(R.id.settingColumns);
        spinner = (Spinner) setting.getChildAt(1);
        Constants.N_COLUMNS = Integer.parseInt((String) spinner.getSelectedItem());

        setting = findViewById(R.id.settingSetLen);
        spinner = (Spinner) setting.getChildAt(1);
        Constants.SET_LEN = Integer.parseInt((String) spinner.getSelectedItem());

        countConstants();
        rows.removeAllViews();
        update();
    }

    void countConstants() {
        Constants.N_PLATES = Constants.N_ROWS * Constants.N_COLUMNS;
        Constants.N_SETS = Constants.N_PLATES / Constants.SET_LEN;
        Constants.N_TRIES = Constants.N_PLATES * Constants.SET_LEN;
    }
}

