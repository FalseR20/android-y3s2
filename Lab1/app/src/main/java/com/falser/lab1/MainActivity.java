package com.falser.lab1;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final int ROWS_COUNT = 6;
    final int IMAGES_COUNT = ROWS_COUNT * ROWS_COUNT;
    final int PAIRS_COUNT = IMAGES_COUNT / 2;

    List<Integer> PAIRS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TableLayout imagesRows = findViewById(R.id.images);
        for (int i = 0; i < ROWS_COUNT; i++) {
            TableRow imageRow = new TableRow(this);
            for (int j = 0; j < ROWS_COUNT; j++) {
                Button imageButton = new ImageButton(this);
                imageRow.addView(imageButton, j);
            }
            imagesRows.addView(imageRow, i);
        }
        restart();
    }

    private void restart() {
        Integer[] intsArray = new Integer[IMAGES_COUNT];
        for (int i = 0; i < PAIRS_COUNT; i++)
            intsArray[2 * i] = intsArray[2 * i + 1] = i;
        PAIRS = Arrays.asList(intsArray);
        Collections.shuffle(PAIRS);
        Log.d("restart()", String.format("Random ints: %s", PAIRS));
    }
}

class ImageButton extends AppCompatButton {

    public ImageButton(Context context) {
        super(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.weight = 1;
        params.rightMargin = params.leftMargin = 5;
        setLayoutParams(params);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setHeight(getWidth());
        super.onDraw(canvas);
    }
}