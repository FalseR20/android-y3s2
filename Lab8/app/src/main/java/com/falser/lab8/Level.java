package com.falser.lab8;

import android.widget.TextView;

import java.util.Locale;

public class Level {
    Integer level;
    Integer maxLevel;
    TextView textView;

    Level(TextView textView, int maxLevel) {
        this.textView = textView;
        this.maxLevel = maxLevel;
        reset();
    }

    public void reset() {
        level = 1;
        textView.setText(String.format(Locale.US, "1/%d", maxLevel));
    }

    public Boolean up() {
        level++;
        if (level > maxLevel) {
            textView.setText(R.string.win);
            return true;
        }
        textView.setText(String.format(Locale.US, "%d/%d", level, maxLevel));
        return false;
    }

    public void lose() {
        textView.setText(R.string.lost);
    }

    public Integer getLevelIndex() {
        return level - 1;
    }
}
