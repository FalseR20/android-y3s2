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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TableLayout rows;
    List<Integer> setsIds;

    List<PlateButton> pickedButtons = new ArrayList<>();
    int state;
    Chronometer chronometer;
    TextView triesCounter;
    int platesLeft;
    int triesLeft;


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
        createElements();
        createSetsIds();
        restartImages();
    }

    void createElements() {
        rows = findViewById(R.id.images);
        for (int i = 0; i < Constants.N_ROWS; i++) {
            TableRow imageRow = new TableRow(this);
            for (int j = 0; j < Constants.N_COLUMNS; j++) {
                PlateButton plateButton = new PlateButton(this);
                plateButton.setOnClickListener(this);
                imageRow.addView(plateButton, j);
            }
            rows.addView(imageRow, i);
        }
        chronometer = findViewById(R.id.chronometer);
        triesCounter = findViewById(R.id.triesCounter);
    }

    private void createSetsIds() {
        Integer[] intsArray = new Integer[Constants.N_PLATES];
        for (int i = 0; i < Constants.N_PLATES; i++)
            intsArray[i] = i / Constants.N_STATES;
        setsIds = Arrays.asList(intsArray);
    }

    public void restartImages() {
        randomizeSetsIds();
        TableRow row;
        for (int i = 0, k = 0; i < Constants.N_ROWS; i++) {
            row = (TableRow) rows.getChildAt(i);
            for (int j = 0; j < Constants.N_COLUMNS; j++, k++) {
                PlateButton image = (PlateButton) row.getChildAt(j);
                image.number = setsIds.get(k);
                image.is_chosen = false;
                image.setText("");
//                image.setText(String.format("(%d)", image.number));
            }
        }
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        platesLeft = Constants.N_SETS;
        triesLeft = Constants.N_TRIES;
        triesCounter.setText(String.valueOf(triesLeft));
        triesCounter.setTextColor(Color.WHITE);
        state = Constants.N_STATES;
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

        setting = findViewById(R.id.settingsRows);
        label = (TextView) setting.getChildAt(0);
        label.setText(R.string.count_of_rows);
        spinner = (Spinner) setting.getChildAt(1);
        adapter = ArrayAdapter.createFromResource(this, R.array.rows_array, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        setting = findViewById(R.id.settingsColumns);
        label = (TextView) setting.getChildAt(0);
        label.setText(R.string.count_of_columns);
        spinner = (Spinner) setting.getChildAt(1);
        adapter = ArrayAdapter.createFromResource(this, R.array.columns_array, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        setting = findViewById(R.id.settingsStates);
        label = (TextView) setting.getChildAt(0);
        label.setText(R.string.count_of_states);
        spinner = (Spinner) setting.getChildAt(1);
        adapter = ArrayAdapter.createFromResource(this, R.array.states_array, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
    }

    public void restartOnClick(View view) {
        restartImages();
    }

    @Override
    public void onClick(View view) {
        PlateButton plateButton = (PlateButton) view;
        if (triesLeft == Constants.N_TRIES && state == Constants.N_STATES) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        }
        if (plateButton.is_chosen || platesLeft < 1 || state != Constants.N_STATES && pickedButtons.contains(plateButton)) {
            return;
        }
        if (state == Constants.N_STATES) {
            for (PlateButton button : pickedButtons)
                button.setText("");
            pickedButtons.clear();
        }
        pickedButtons.add(plateButton);
        state--;
        plateButton.setText(String.valueOf(plateButton.number));

        if (state != 0) {
            return;
        }
        state = Constants.N_STATES;
        triesLeft--;
        triesCounter.setText(String.valueOf(triesLeft));
        if (triesLeft == 0) {
            triesCounter.setTextColor(Color.RED);
            Toast.makeText(this, R.string.lost, Toast.LENGTH_SHORT).show();
            platesLeft = -1;
            chronometer.stop();
            return;
        }
        if (triesLeft <= 9) {
            triesCounter.setTextColor(0xFFFF7F7F);
        }
        if (!checkAllEqual()) {
            return;
        }
        for (PlateButton button : pickedButtons) {
            button.is_chosen = true;
        }
        pickedButtons.clear();
        platesLeft--;
        if (platesLeft == 0) {
            chronometer.stop();
            Toast.makeText(this, R.string.win, Toast.LENGTH_SHORT).show();
        }
    }

    boolean checkAllEqual() {
        int check = pickedButtons.get(0).number;
        for (PlateButton button : pickedButtons) {
            if (check != button.number) return false;
        }
        return true;
    }

    public void onApply(View view) {
        TableRow setting;
        Spinner spinner;

        setting = findViewById(R.id.settingsRows);
        spinner = (Spinner) setting.getChildAt(1);
        Constants.N_ROWS = Integer.parseInt((String) spinner.getSelectedItem());

        setting = findViewById(R.id.settingsColumns);
        spinner = (Spinner) setting.getChildAt(1);
        Constants.N_COLUMNS = Integer.parseInt((String) spinner.getSelectedItem());

        setting = findViewById(R.id.settingsStates);
        spinner = (Spinner) setting.getChildAt(1);
        Constants.N_STATES = Integer.parseInt((String) spinner.getSelectedItem());

        countConstants();
        rows.removeAllViews();
        update();
    }

    void countConstants(){
        Constants.N_PLATES = Constants.N_ROWS * Constants.N_COLUMNS;
        Constants.N_SETS = Constants.N_PLATES / Constants.N_STATES;
        Constants.N_TRIES = (int) (Constants.N_PLATES * Constants.N_STATES * 0.6);
    }
}

