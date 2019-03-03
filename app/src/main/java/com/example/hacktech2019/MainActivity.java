package com.example.hacktech2019;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final static int drinksPerNote = 8;
    private final static int drinksPerEmail = 9;
    private final static int drinksPerText = 5;

    private NotificationUtils mNotificationUtils;

    private int waterAmount = 0;
    private int alcAmount = 0;

    public final static String ALC_AMOUNT_KEY = "alc";
    public final static String WATER_AMOUNT_KEY = "water";

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", true).commit();
        // Handle is first run case
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //show start activity
            startActivity(new Intent(MainActivity.this, Setup.class));
//            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            //Toast.makeText(MainActivity.this, "First Run", Toast.LENGTH_LONG).show();
        }

        if (savedInstanceState != null) {
            alcAmount = savedInstanceState.getInt(ALC_AMOUNT_KEY);
            waterAmount = savedInstanceState.getInt(WATER_AMOUNT_KEY);
        }

        mNotificationUtils = new NotificationUtils(this);
        updateScreen();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(ALC_AMOUNT_KEY, alcAmount);
        savedInstanceState.putInt(WATER_AMOUNT_KEY, waterAmount);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onSettingsClick(View view) {
        startActivity(new Intent(MainActivity.this, Setup.class));
    }

    /**
     * Increment the alcohol counter.
     */
    @TargetApi(Build.VERSION_CODES.O)
    public void onAlcoholClick(View view) {
        // Get the counter.
        alcAmount++;
        if (alcAmount % drinksPerNote == drinksPerNote - 1) {
            makeNote();
        }
        if (alcAmount % drinksPerText == drinksPerText - 1) {
            textBuddy();
        }
        if (alcAmount % drinksPerEmail == drinksPerEmail - 1) {
            emailBuddy();
        }
        updateScreen();
    }

    /**
     * Increment the water counter.
     */
    public void onWaterClick(View view) {
        // Get the counter.
        waterAmount++;
        updateScreen();
    }

    public void updateScreen() {
        ((TextView) findViewById(R.id.alcohol_counter)).setText(Integer.toString(alcAmount));
        ((TextView) findViewById(R.id.water_counter)).setText(Integer.toString(waterAmount));
    }

    public double bloodAlcohol() {
        return 0;
    }

    public void textBuddy() {
        Intent textIntent = new Intent(this, SMSActivity.class);
        textIntent.putExtra(ALC_AMOUNT_KEY, alcAmount);
        startActivity(textIntent);
    }

    public void emailBuddy() {
        Intent emailIntent = new Intent(this, EmailActivity.class);
        emailIntent.putExtra(ALC_AMOUNT_KEY, alcAmount);
        emailIntent.putExtra(WATER_AMOUNT_KEY, alcAmount);
        startActivity(emailIntent);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void makeNote() {
        String notice = "You had " + alcAmount + " standard drinks, and only " + waterAmount + " glasses of water!";
        Notification.Builder nb = mNotificationUtils.
                getAndroidChannelNotification("Alcohol notice", notice, R.drawable.drinky_icon);
        mNotificationUtils.getManager().notify(101, nb.build());
    }
}

