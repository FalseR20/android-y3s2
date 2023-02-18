package com.falser.lab1;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TableLayout imagesRows;
    List<Integer> setsIds;

    List<ImageButton> pickedButtons = new ArrayList<>();
    int state;
    Chronometer chronometer;
    TextView progress;
    int progressInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        progress = findViewById(R.id.progress);
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
        progressInt = Constants.N_SETS;
        progress.setText(String.valueOf(progressInt));
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
        if (!chronometer.isActivated() && progressInt > 0) chronometer.start();
        if (imageButton.is_chosen || state != Constants.N_STATES && pickedButtons.contains(imageButton)) {
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
        if (!checkAllEqual()) {
            return;
        }
        for (ImageButton button : pickedButtons) {
            button.is_chosen = true;
        }
        pickedButtons.clear();
        progressInt--;
        progress.setText(String.valueOf(progressInt));
        if (progressInt == 0) {
            chronometer.stop();
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
        params.rightMargin = params.leftMargin = 5;
        setLayoutParams(params);
        setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8, getResources().getDisplayMetrics()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setHeight(getWidth());
        super.onDraw(canvas);
    }

}