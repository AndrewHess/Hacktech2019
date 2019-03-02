package com.example.hacktech2019;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
            Toast.makeText(MainActivity.this, "First Run", Toast.LENGTH_LONG).show();
        }
        setContentView(R.layout.activity_main);

        int age = getSharedPreferences("USER_INFO", MODE_PRIVATE).getInt("Age", 0);
        int weight = getSharedPreferences("USER_INFO", MODE_PRIVATE).getInt("Weight", 0);
        String name = getSharedPreferences("USER_INFO", MODE_PRIVATE).getString("Name", "");


        TextView ageDisp = findViewById(R.id.aBox);
        ageDisp.setText("Age: " + age);

        TextView wDisp = findViewById(R.id.wBox);
        wDisp.setText("Weight: " + weight);

        TextView nDisp = findViewById(R.id.nBox);
        nDisp.setText(name);
    }
}
