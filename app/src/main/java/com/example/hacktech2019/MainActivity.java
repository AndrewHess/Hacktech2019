package com.example.hacktech2019;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final static int drinksPerNote = 8;
    private final static int drinksPerEmail = 5;
    public final static String EXTRA_CALL = "call";
    private static final int PERMISSION_REQUEST_CODE = 1;
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

    /**
     * Increment the alcohol counter.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onAlcoholClick(View view) {
        // Get the counter.
        alcAmount++;
        if (alcAmount % drinksPerNote == drinksPerNote - 1) {
            makeNote();
        }
        if (alcAmount % drinksPerEmail == drinksPerEmail - 1) {
            textBuddyNshots(alcAmount);
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

    public void textBuddyNshots(int nDrinks) {
        Intent textIntent = new Intent(this, SMSActivity.class);
        textIntent.putExtra(EXTRA_CALL, nDrinks);
        startActivity(textIntent);
    }

    public void emailBuddyNshots(int nDrinks) {
        SharedPreferences info = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        String drunkName = info.getString("Name", "");
        String buddyEmail = info.getString("BuddyMail", "");

        TextView textView = (TextView) findViewById(R.id.water_counter);
        Integer nWater = Integer.parseInt(textView.getText().toString());

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.setData(Uri.parse(buddyEmail));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Update on " + drunkName);
        String emailBodyDrinks = "Dear friend,\n\n" + drunkName + " has consumed " + nDrinks +
                " drinks tonight, as well as " + nWater + " cups of water.\n\nThank you for " +
                "keeping an eye on their safety.";
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBodyDrinks);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Sent", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "Yike :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void emailBuddyBAC() {
        SharedPreferences info = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        String drunkName = info.getString("Name", "");
        String buddyEmail = info.getString("BuddyMail", "");

        TextView textView = (TextView) findViewById(R.id.water_counter);
        Integer nWater = Integer.parseInt(textView.getText().toString());

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse(buddyEmail));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Update on " + drunkName);
        String emailBodyBAC = "Dear friend,\n\n" + drunkName + " 's blood alcohol content may " +
                "be reaching " + bloodAlcohol() + ".\n\nThank you for " +
                "keeping an eye on their safety.";
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBodyBAC);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Sent", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "Yike :(", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void makeNote() {
        String notice = "You had " + alcAmount + " standard drinks, and only " + waterAmount + " glasses of water!";
        Notification.Builder nb = mNotificationUtils.
                getAndroidChannelNotification("Alcohol notice", notice, R.drawable.drinky_icon);
        mNotificationUtils.getManager().notify(101, nb.build());
    }
}

