package com.example.android_lab_4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private TextView textView;
    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        gestureDetector = new GestureDetectorCompat(this, this);
        gestureDetector.setOnDoubleTapListener(this);
    }

    private void setHelloMessage() {
        String helloMessage = "Available gestures: \n" +
                "1. Single Tap\n" +
                "2. Long Press\n" +
                "3. Double Tap\n" +
                "4. Fling";
        textView.setText(helloMessage);
    }

    @Override
    protected void onStart() {
        setHelloMessage();
        super.onStart();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(@NonNull MotionEvent motionEvent) {
        textView.setText("onDown");
        return false;
    }

    // TAP MOTION EVENTS
    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        textView.setText("onSingleTapUp");
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(@NonNull MotionEvent motionEvent) {
        showToast("EVENT: " + getCurrentMethodName());
        Intent intent = new Intent(this, SingleTapActivity.class);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean onDoubleTap(@NonNull MotionEvent motionEvent) {
        showToast("EVENT: " + getCurrentMethodName());
        Intent intent = new Intent(this, DoubleTapActivity.class);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(@NonNull MotionEvent motionEvent) {
        textView.setText("onDoubleTapEvent");
        return false;
    }


    // PRESS MOTION EVENT
    @Override
    public void onShowPress(@NonNull MotionEvent motionEvent) {
        textView.setText("onShowPress");
    }

    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {
        showToast("EVENT: " + getCurrentMethodName());
        Intent intent = new Intent(this, LongPressActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        textView.setText("You are scrolling now...");
        return false;
    }

    @Override
    public boolean onFling(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        showFlingDialog();
        return false;
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(
                getApplicationContext(),
                message,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showFlingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("onFling")
                .setMessage("onFling Event was called")
                .setPositiveButton("Оk", (dialog, id) -> {
                    dialog.cancel();
                    setHelloMessage();
                })
                .show();
    }

    private static String getCurrentMethodName() {
        return Thread.currentThread()
                .getStackTrace()[3]
                .getMethodName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            Intent i = new Intent(this, InfoActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}