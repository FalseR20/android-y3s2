package com.falser.lab8;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TableRow;

import androidx.appcompat.widget.AppCompatButton;

class PlateButton extends AppCompatButton {
    int number;
    boolean is_chosen;

    public PlateButton(Context context) {
        super(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.weight = 1;
        params.rightMargin = params.leftMargin = (int) getResources().getDimension(R.dimen.defMargin);
        setLayoutParams(params);
        setTextSize(getResources().getDimensionPixelSize(R.dimen.imagesTextSize));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setHeight(getWidth());
        super.onDraw(canvas);
    }

}
