package com.falser.lab8;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    CountDownTimer timer;
    Integer platesLeft;
    Boolean isGameStarted;
    Boolean isGameFinished;
    Integer width;
    Integer height;
    TextView levelTextView;
    Integer level;
    Integer setLen;
    Integer difficulty;

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
        difficulty = Integer.parseInt(prefs.getString("difficulty", "1"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAll();
    }

    void createAll() {
        width = getResources().getInteger(R.integer.start_width);
        height = getResources().getInteger(R.integer.start_height);
        setLen = getResources().getInteger(R.integer.start_set_len);
        level = 1;
        createField();
        setSetsIDs();
    }

    public void restartOnClick(View view) {
        plates.removeAllViews();
        createAll();
    }

    void levelUp() {
        level++;
        width++;
        height++;
        plates.removeAllViews();
        createField();
    }

    void createField() {
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
        levelTextView = findViewById(R.id.level);
        timerTextView = findViewById(R.id.timer);
        timerTextView.setText("--:--");

        setsIDs = new ArrayList<>();
        for (int i = 0; i < width * height; i++)
            setsIDs.add(i / setLen);
        Collections.shuffle(setsIDs);
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
        platesLeft = setLen;
        pickedButtons.clear();
        isGameStarted = false;
        isGameFinished = false;
    }

    @Override
    public void onClick(View view) {
        PlateButton plateButton = (PlateButton) view;
        if (!isGameStarted) {
            timer = new Timer(30000, this);
            timer.start();
            isGameStarted = true;
        }
        if (isGameFinished) {
            Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show();
            return;
        }
        if (plateButton.is_chosen || pickedButtons.contains(plateButton)) {
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
                    if (isGameFinished) {
                        Toast.makeText(this, R.string.almost_losing, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.win, Toast.LENGTH_SHORT).show();
                    }
                    isGameFinished = true;
                    timer.cancel();
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

    void finishGame(){
        isGameFinished = true;
        Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show();
    }

}

