package com.falser.lab8;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    ArrayList<Integer> setsIDs;
    ArrayDeque<PlateButton> pickedButtons = new ArrayDeque<>();
    TextView timerTextView;
    Timer timer;
    Integer platesLeft;
    Boolean isGameStarted;
    Boolean isGameFinished;
    Integer height;
    Integer width;
    Integer nSets;
    Level level;
    Integer setLen;
    Integer time = 100000;
    int[] heights;
    int[] widths;
    int[] setLengths;

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
        } else if (id == R.id.action_info) {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        time = Integer.parseInt(prefs.getString(getResources().getString(R.string.time_key), "100000"));
        if (isGameStarted && !isGameFinished) return;
        timer = new Timer(time, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heights = getResources().getIntArray(R.array.difficulties_heights);
        widths = getResources().getIntArray(R.array.difficulties_widths);
        setLengths = getResources().getIntArray(R.array.difficulties_set_lengths);

        level = new Level(findViewById(R.id.level), heights.length);
        timerTextView = findViewById(R.id.timer);
        plates = findViewById(R.id.plates);

        isGameStarted = false;
        isGameFinished = false;
        timer = new Timer(time, this);

        createAll();
    }

    void createAll() {
        height = heights[0];
        width = widths[0];
        setLen = setLengths[0];

        nSets = height * width / setLen;
        createField();
    }

    public void restartOnClick(View view) {
        plates.removeAllViews();
        isGameStarted = false;
        isGameFinished = false;
        if (timer != null) timer.cancel();
        timer = new Timer(time, this);
        level.reset();
        createAll();
    }

    void createField() {
        TableRow row;
        for (int i = 0; i < height; i++) {
            row = new TableRow(this);
            for (int j = 0; j < width; j++) {
                PlateButton plateButton = new PlateButton(this);
                plateButton.setOnClickListener(this);
                row.addView(plateButton, j);
            }
            plates.addView(row, i);
        }


        setsIDs = new ArrayList<>();
        for (int i = 0; i < height * width; i++)
            setsIDs.add(i / setLen);
        Collections.shuffle(setsIDs);
        setSetsIDs();
    }

    public void setSetsIDs() {
        TableRow row;
        for (int i = 0, k = 0; i < height; i++) {
            row = (TableRow) plates.getChildAt(i);
            for (int j = 0; j < width; j++, k++) {
                PlateButton image = (PlateButton) row.getChildAt(j);
                image.number = setsIDs.get(k);
                image.is_chosen = false;
                image.setText("");
            }
        }
        platesLeft = nSets;
        pickedButtons.clear();
    }

    @Override
    public void onClick(View view) {
        PlateButton plateButton = (PlateButton) view;
        if (!isGameStarted) {
            timer.start();
            isGameStarted = true;
        }
        if (isGameFinished || plateButton.is_chosen || pickedButtons.contains(plateButton)) {
            return;
        }

        if (pickedButtons.size() > 0) pickedButtons.getLast().setText("");
        pickedButtons.addLast(plateButton);
        plateButton.setText(String.valueOf(plateButton.number));

        if (pickedButtons.size() == setLen) {
            if (checkAllEqual()) {
                for (PlateButton button : pickedButtons) {
                    button.is_chosen = true;
                    button.setText(String.valueOf(button.number));
                }
                pickedButtons.clear();
                platesLeft--;
                if (platesLeft == 0) {
                    new Handler().postDelayed(this::levelUp, 100);
                }
            } else {
                pickedButtons.pop();
            }
        }
    }

    boolean checkAllEqual() {
        int check = pickedButtons.getFirst().number;
        for (PlateButton button : pickedButtons) {
            if (check != button.number) return false;
        }
        return true;
    }

    void finishGame() {
        isGameFinished = true;
        level.lose();
    }

    void levelUp() {
        if (level.up()) {
            timer.cancel();
            return;
        }
        Toast.makeText(this, R.string.level_up, Toast.LENGTH_SHORT).show();

        height = heights[level.getLevelIndex()];
        width = widths[level.getLevelIndex()];
        setLen = setLengths[level.getLevelIndex()];
        nSets = height * width / setLen;
        plates.removeAllViews();
        createField();
    }
}
