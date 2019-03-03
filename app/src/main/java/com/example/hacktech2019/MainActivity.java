package com.example.hacktech2019;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private NotificationUtils mNotificationUtils;
    private int waterAmount = 0;
    private int alcAmount = 0;
    private String ALC_AMOUNT_KEY = "alc";
    private String WATER_AMOUNT_KEY = "water";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", true).commit();
        // Handle is first run case
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //show start activity
            startActivity(new Intent(MainActivity.this, Setup.class));
            //Toast.makeText(MainActivity.this, "First Run", Toast.LENGTH_LONG).show();
        }
        setContentView(R.layout.activity_main);

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

    /** Increment the alcohol counter. */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onAlcoholClick(View view) {
        // Get the counter.
        alcAmount++;
        if (alcAmount > 5) {
            makeNote();
        }
        updateScreen();
    }

    /** Increment the water counter. */
    public void onWaterClick(View view) {
        // Get the counter.
        waterAmount++;
        updateScreen();
    }

    public void updateScreen() {
        ((TextView) findViewById(R.id.alcohol_counter)).setText(Integer.toString(alcAmount));
        ((TextView) findViewById(R.id.water_counter)).setText(Integer.toString(waterAmount));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void makeNote() {
        String notice = "You had " + alcAmount + " standard drinks, and only " + waterAmount + " glasses of water!";
        Notification.Builder nb = mNotificationUtils.
                getAndroidChannelNotification("Alcohol notice", notice, R.drawable.drinky_icon);
        mNotificationUtils.getManager().notify(101, nb.build());
    }

    /** Send an email to the buddy. */
    public void emailBuddy(View view) {
        // Get the email.

    }
}
