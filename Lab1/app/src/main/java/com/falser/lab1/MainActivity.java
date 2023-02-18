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
    final static int ROWS_COUNT = 6;
    final static int IMAGES_COUNT = ROWS_COUNT * ROWS_COUNT;

    TableLayout imagesRows;

    List<Integer> PAIRS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagesRows = findViewById(R.id.images);
        for (int i = 0; i < ROWS_COUNT; i++) {
            TableRow imageRow = new TableRow(this);
            for (int j = 0; j < ROWS_COUNT; j++) {
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
        for (int i = 0; i < ROWS_COUNT; i++) {
            row = (TableRow) imagesRows.getChildAt(i);
            for (int j = 0; j < ROWS_COUNT; j++) {
                ImageButton image = (ImageButton) row.getChildAt(j);
                image.number = PAIRS.get(i * ROWS_COUNT + j);
                image.setText("");
//                image.setText(String.valueOf(image.number));
            }
        }
    }

    private void updateRandom() {
        Integer[] intsArray = new Integer[IMAGES_COUNT];
        for (int i = 0; i < IMAGES_COUNT; i++)
            intsArray[i]  = i / ImageButton.STATE_COUNT;
        PAIRS = Arrays.asList(intsArray);
        Collections.shuffle(PAIRS);
        Log.d("restart()", String.format("Random ints: %s", PAIRS));
    }

    public void restartClick(View view) {
        restart();
    }
}

class ImageButton extends AppCompatButton implements View.OnClickListener {
    public int number;
    public static final int STATE_COUNT = 3;
    public static final int STATE_DEFAULT = STATE_COUNT - 1;

    public static List<ImageButton> numbersButtons = new ArrayList<>();

    public static int state = STATE_DEFAULT;

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
        if (numbersButtons.contains(this)) {
            return;
        }
        if (state == -1) {
            state = STATE_DEFAULT;

            if (!checkAllEqual()) {
                for (ImageButton button : numbersButtons)
                    button.setText("");
            }
            numbersButtons.clear();
        }
        numbersButtons.add(this);
        state--;
        setText(String.valueOf(number));
    }

    boolean checkAllEqual() {
        int check = numbersButtons.get(0).number;
        for (ImageButton button : numbersButtons) {
            if (check != button.number)
                return false;
        }
        return true;
    }
}