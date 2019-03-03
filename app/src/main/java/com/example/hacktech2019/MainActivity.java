package com.example.hacktech2019;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        textView.setText(Integer.toString(cur + 1));
    }

    /** Increment the water counter. */
    public void onWaterClick(View view) {
        // Get the counter.
        TextView textView = (TextView) findViewById(R.id.water_counter);

        Integer cur = Integer.parseInt(textView.getText().toString());
        textView.setText(Integer.toString(cur + 1));
    }

    /** Send an email to the buddy. */
    public void emailBuddy(View view) {
        // Get the email.

    }
}
