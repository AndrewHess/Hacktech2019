package com.example.hacktech2019;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class EmailActivity extends AppCompatActivity {
    private SharedPreferences info;
    int nWater;
    int nAlc;
    double bac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent emIntent = getIntent();
        nAlc = emIntent.getExtras().getInt(MainActivity.ALC_AMOUNT_KEY);
        nWater = emIntent.getExtras().getInt(MainActivity.WATER_AMOUNT_KEY);
        bac = emIntent.getExtras().getDouble(MainActivity.BAC_AMOUNT_KEY);
        emailBuddy();
        finish();
    }

    public void emailBuddy() {
        info = getSharedPreferences("USER_INFO", MODE_PRIVATE);

        String drunkName = info.getString("Name", "");
        String[] TO = {info.getString("BuddyMail", "")};
        String[] CC = {info.getString("Mail", "")};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setType("text/plain");
        emailIntent.setData(Uri.parse("mailto:"));

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Update on " + drunkName);
        String emailBodyDrinks = "Dear friend,\n\n" + drunkName + " has consumed " + nAlc +
                " drinks tonight, as well as " + nWater + " cups of water. Their estimated bac is " + (bac * 100) + "%" +
                "\n\nThank you for " +
                "keeping an eye on their safety.";
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBodyDrinks);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Sent", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Email Didn't Send!", Toast.LENGTH_SHORT).show();
        }
    }
}

