package com.falser.lab1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TableLayout imagesRows;
    List<Integer> setsIds;

    List<ImageButton> pickedButtons = new ArrayList<>();
    int state;
    Chronometer chronometer;
    TextView triesCounter;
    int imagesLeft;
    int triesLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        imagesRows = findViewById(R.id.images);
        for (int i = 0; i < Constants.N_ROWS; i++) {
            TableRow imageRow = new TableRow(this);
            for (int j = 0; j < Constants.N_COLUMNS; j++) {
                ImageButton imageButton = new ImageButton(this);
                imageButton.setOnClickListener(this);
                imageRow.addView(imageButton, j);
            }
            imagesRows.addView(imageRow, i);
        }
        chronometer = findViewById(R.id.chronometer);
        triesCounter = findViewById(R.id.triesCounter);
        createSetsIds();
        resetImages();
    }

    public void resetImages() {
        randomizeSetsIds();
        TableRow row;
        for (int i = 0, k = 0; i < Constants.N_ROWS; i++) {
            row = (TableRow) imagesRows.getChildAt(i);
            for (int j = 0; j < Constants.N_COLUMNS; j++, k++) {
                ImageButton image = (ImageButton) row.getChildAt(j);
                image.number = setsIds.get(k);
                image.is_chosen = false;
                image.setText("");
//                image.setText(String.format("(%d)", image.number));
            }
        }
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        imagesLeft = Constants.N_SETS;
        triesLeft = Constants.N_TRIES;
        triesCounter.setText(String.valueOf(triesLeft));
        triesCounter.setTextColor(Color.WHITE);
        state = Constants.N_STATES;
    }

    private void createSetsIds() {
        Integer[] intsArray = new Integer[Constants.N_IMAGES];
        for (int i = 0; i < Constants.N_IMAGES; i++)
            intsArray[i] = i / Constants.N_STATES;
        setsIds = Arrays.asList(intsArray);
    }

    private void randomizeSetsIds() {
        Collections.shuffle(setsIds);
        Log.d(this.getClass().getName(), String.format("Random ints: %s", setsIds));
    }

    public void restartOnClick(View view) {
        resetImages();
    }

    @Override
    public void onClick(View view) {
        ImageButton imageButton = (ImageButton) view;
        if (!chronometer.isActivated() && imagesLeft > 0) chronometer.start();
        if (imageButton.is_chosen || imagesLeft < 1 || state != Constants.N_STATES && pickedButtons.contains(imageButton)) {
            return;
        }
        if (state == Constants.N_STATES) {
            for (ImageButton button : pickedButtons)
                button.setText("");
            pickedButtons.clear();
        }
        pickedButtons.add(imageButton);
        state--;
        imageButton.setText(String.valueOf(imageButton.number));

        if (state != 0) {
            return;
        }
        state = Constants.N_STATES;
        triesLeft--;
        triesCounter.setText(String.valueOf(triesLeft));
        if (triesLeft == 0) {
            triesCounter.setTextColor(Color.RED);
            Toast.makeText(this, R.string.lost, Toast.LENGTH_SHORT).show();
            imagesLeft = -1;
            chronometer.stop();
            return;
        }
        if (triesLeft <= 9) {
            triesCounter.setTextColor(0xFFFF7F7F);
        }
        if (!checkAllEqual()) {
            return;
        }
        for (ImageButton button : pickedButtons) {
            button.is_chosen = true;
        }
        pickedButtons.clear();
        imagesLeft--;
        if (imagesLeft == 0) {
            chronometer.stop();
            Toast.makeText(this, R.string.win, Toast.LENGTH_SHORT).show();
        }
    }

    boolean checkAllEqual() {
        int check = pickedButtons.get(0).number;
        for (ImageButton button : pickedButtons) {
            if (check != button.number) return false;
        }
        return true;
    }
}

class ImageButton extends AppCompatButton {
    int number;
    boolean is_chosen;

    public ImageButton(Context context) {
        super(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.weight = 1;
        params.rightMargin = params.leftMargin = (int) getResources().getDimension(R.dimen.defMargin);
        setLayoutParams(params);
        setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8, getResources().getDisplayMetrics()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setHeight(getWidth());
        super.onDraw(canvas);
    }

}