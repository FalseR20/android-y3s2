package com.falser.lab8;

import android.os.CountDownTimer;

import java.util.Locale;

public class Timer extends CountDownTimer {
    MainActivity mainActivity;

    public Timer(long millisInFuture, MainActivity mainActivity) {
        super(millisInFuture, 100);
        this.mainActivity = mainActivity;
        mainActivity.timerTextView.setText(String.format(Locale.US, "%.1f", millisInFuture / 1000D));
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mainActivity.timerTextView.setText(String.format(Locale.US, "%.1f", millisUntilFinished / 1000D));
    }

    @Override
    public void onFinish() {
        mainActivity.finishGame();
    }
}
