package com.falser.lab8;

import android.widget.TextView;

public class Level {
    Integer level;
    TextView textView;

    Level(TextView textView) {
        this.textView = textView;
        reset();
    }

    public void reset() {
        level = 1;
        textView.setText("1");
    }

    public Boolean up(int maxLevel) {
        level++;
        if (level == maxLevel) {
            textView.setText("W");
            return true;
        }
        textView.setText(String.valueOf(level));
        return false;
    }

    public Integer getLevelIndex() {
        return level - 1;
    }
}
