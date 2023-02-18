package com.falser.lab1;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    TableLayout imagesRows;
    List<Integer> setsIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagesRows = findViewById(R.id.images);
        for (int i = 0; i < Constants.N_ROWS; i++) {
            TableRow imageRow = new TableRow(this);
            for (int j = 0; j < Constants.N_COLUMNS; j++) {
                ImageButton imageButton = new ImageButton(this);
                imageRow.addView(imageButton, j);
            }
            imagesRows.addView(imageRow, i);
        }
        restart();
    }

    public void restart() {
        updateRandom();
        TableRow row;
        for (int i = 0, k = 0; i < Constants.N_ROWS; i++) {
            row = (TableRow) imagesRows.getChildAt(i);
            for (int j = 0; j < Constants.N_COLUMNS; j++, k++) {
                ImageButton image = (ImageButton) row.getChildAt(j);
                image.number = setsIds.get(k);
                image.is_chosen = false;
                image.setText("");
//                image.setText(String.format(Locale.getDefault(), "(%d)", image.number));
            }
        }
    }

    private void updateRandom() {
        Integer[] intsArray = new Integer[Constants.N_IMAGES];
        for (int i = 0; i < Constants.N_IMAGES; i++)
            intsArray[i] = i / Constants.N_STATES;
        setsIds = Arrays.asList(intsArray);
        Collections.shuffle(setsIds);
        Log.d("restart()", String.format("Random ints: %s", setsIds));
    }

    public void restartClick(View view) {
        restart();
    }
}

class ImageButton extends AppCompatButton implements View.OnClickListener {
    static List<ImageButton> pickedButtons = new ArrayList<>();
    static int state = Constants.SET_LENGTH;
    int number;
    boolean is_chosen;

    public ImageButton(Context context) {
        super(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.weight = 1;
        params.rightMargin = params.leftMargin = 5;
        setLayoutParams(params);
        setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8, getResources().getDisplayMetrics()));
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setHeight(getWidth());
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View view) {
        if (is_chosen || pickedButtons.contains(this)) {
            return;
        }
        if (state == -1) {
            state = Constants.SET_LENGTH;

            if (!checkAllEqual()) {
                for (ImageButton button : pickedButtons)
                    button.setText("");
            } else {
                for (ImageButton button : pickedButtons) {
                    button.is_chosen = true;
                }
            }
            pickedButtons.clear();
        }
        pickedButtons.add(this);
        state--;
        setText(String.valueOf(number));
    }

    boolean checkAllEqual() {
        int check = pickedButtons.get(0).number;
        for (ImageButton button : pickedButtons) {
            if (check != button.number) return false;
        }
        return true;
    }
}