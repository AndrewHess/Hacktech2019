package com.example.hacktech2019;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static java.util.Calendar.AM;
import static java.util.Calendar.PM;

public class MainActivity extends AppCompatActivity {

    private final static int drinksPerNote = 8;
    private final static int drinksPerEmail = 9;
    private final static int drinksPerText = 5;

    private NotificationUtils mNotificationUtils;

    private int waterAmount = 0;
    private int alcAmount = 0;
    private double bac;

    public final static String ALC_AMOUNT_KEY = "alc";
    public final static String BAC_AMOUNT_KEY = "bac";
    public final static String WATER_AMOUNT_KEY = "water";
    public final static String GRAPH_KEY = "graph";
    public final static String BAC_KEY= "bacs";

    ArrayList<String> times = new ArrayList<>();
    ArrayList<String> bacs = new ArrayList<>();

    public double startTime = 1.0;
    public String timeLastDrink;
    public String timeLastWater;
    int prevAMPM = Calendar.PM;

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

    public static double parseTime(String time) {
        String[] parts = time.split(":");
        return (double) Integer.parseInt(parts[0]) + ((double) Integer.parseInt(parts[1])) / 60.0;
    }

    /**
     * Increment the alcohol counter.
     */
    @TargetApi(Build.VERSION_CODES.O)
    public void onAlcoholClick(View view) {
        checkForReset();
        timeLastDrink = getCurrentTime();

        // Get the counter.
        alcAmount++;
        updateBAC();
        times.add(Double.toString(parseTime(getFastTime())));
        bacs.add(Double.toString(bac));

        if (alcAmount == 1) {
            startTime = parseTime(timeLastDrink);
        }

        if (alcAmount % drinksPerNote == drinksPerNote - 1) {
            makeNote();
        }
        if (alcAmount % drinksPerText == drinksPerText - 1 || alcAmount > waterAmount + 2) {
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
        checkForReset();
        timeLastWater = getCurrentTime();
        // Get the counter.
        waterAmount++;
        updateScreen();
    }

    private void updateBAC() {
        int w = getSharedPreferences("USER_INFO", MODE_PRIVATE).getInt("Weight", 0);
        String gender = getSharedPreferences("USER_INFO", MODE_PRIVATE).getString("Gender", "");
        double r = (gender.equals("male")) ? 0.73 : 0.66;
        double h = parseTime(getCurrentTime()) - startTime;
        if (h < 0) { h += 12; }

        Toast.makeText(this, "Alc: " + alcAmount + ",Weight: " + w + ",R: " + r + ",time: " + h, Toast.LENGTH_LONG).show();
        bac = this.BAC(alcAmount, w, r, h);
    }

    public static double BAC(int a, int w, double r, double h) {
       return ((double) a * 5.14 / ((double) w) * r) - 0.15 * h;
    }

    public void updateScreen() {
        ((TextView) findViewById(R.id.alcohol_counter)).setText(Integer.toString(alcAmount));
        ((TextView) findViewById(R.id.water_counter)).setText(Integer.toString(waterAmount));
    }

    public void textBuddy() {
        Intent textIntent = new Intent(this, SMSActivity.class);
        textIntent.putExtra(ALC_AMOUNT_KEY, alcAmount);
        textIntent.putExtra(BAC_AMOUNT_KEY, bac);
        startActivity(textIntent);
    }

    public void emailBuddy() {
        Intent emailIntent = new Intent(this, EmailActivity.class);
        emailIntent.putExtra(ALC_AMOUNT_KEY, alcAmount);
        emailIntent.putExtra(WATER_AMOUNT_KEY, alcAmount);
        emailIntent.putExtra(BAC_AMOUNT_KEY, bac);
        startActivity(emailIntent);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void makeNote() {
        String notice = "You had " + alcAmount + " standard drinks, and only " + waterAmount +
                " glasses of water! Estimated BAC: " + (bac * 100) + "%";
        Notification.Builder nb = mNotificationUtils.
                getAndroidChannelNotification("Alcohol notice", notice, R.drawable.drinky_icon);
        mNotificationUtils.getManager().notify(101, nb.build());
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String strDate = mdformat.format(calendar.getTime());
        return (strDate);
    }

    public static String getFastTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("mm:ss");
        String strDate = mdformat.format(calendar.getTime());
        return (strDate);
    }

    private void checkForReset(){
        Calendar calendar = Calendar.getInstance();
        int currentAMPM = calendar.get(Calendar.AM_PM);
        if (currentAMPM == PM && prevAMPM == AM){
            resetStats();
        }
        prevAMPM = currentAMPM;
    }

    private void resetStats(){
        alcAmount = 0;
        waterAmount = 0;
        timeLastDrink = "";
        timeLastWater = "";
        times.clear();
        bacs.clear();
        updateScreen();
    }

    public void onAnalyticsClick(View view) {
        Intent graphIntent = new Intent(this, AnalyticsActivity.class);
        graphIntent.putStringArrayListExtra(GRAPH_KEY, times);
        graphIntent.putStringArrayListExtra(BAC_KEY, bacs);
        startActivity(graphIntent);
    }
}

