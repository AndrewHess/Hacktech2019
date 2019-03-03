package com.example.hacktech2019;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import static android.content.Context.MODE_PRIVATE;

public class MainActivity extends AppCompatActivity {


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
    }

    public void onSettingsClick(View view) {
        startActivity(new Intent(MainActivity.this, Setup.class));
        setContentView(R.layout.activity_main);
    }

    /** Increment the alcohol counter. */
    public void onAlcoholClick(View view) {
        // Get the counter.
        TextView textView = (TextView) findViewById(R.id.alcohol_counter);

        Integer cur = Integer.parseInt(textView.getText().toString());
        cur++;
        textView.setText(Integer.toString(cur));
    }

    /** Increment the water counter. */
    public void onWaterClick(View view) {
        // Get the counter.
        TextView textView = (TextView) findViewById(R.id.water_counter);

        Integer cur = Integer.parseInt(textView.getText().toString());
        cur++;
        textView.setText(Integer.toString(cur));

        int drinksPerEmail = 2;
        if(cur % drinksPerEmail == drinksPerEmail - 1){
            emailBuddyNshots(cur);
        }
    }


    public double bloodAlcohol(){
        return 0;
    }

    public void emailBuddyNshots(int nDrinks){

        SharedPreferences info = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        String drunkName = info.getString("Name", "");
        String buddyEmail = info.getString("BuddyMail", "");

        TextView textView = (TextView) findViewById(R.id.water_counter);
        Integer nWater = Integer.parseInt(textView.getText().toString());

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
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

    public void emailBuddyBAC(){

        SharedPreferences info = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        String drunkName = info.getString("Name", "");
        String buddyEmail = info.getString("BuddyMail", "");

        TextView textView = (TextView) findViewById(R.id.water_counter);
        Integer nWater = Integer.parseInt(textView.getText().toString());

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse(buddyEmail));
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
}
