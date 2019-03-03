package com.example.hacktech2019;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.widget.Button;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.util.Log;
import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.Toast;
import android.view.View;

public class SMSActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    int nShots;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                Log.e("permission", "Permission already granted.");
            } else {
                requestPermission();
            }
        }

        number = getSharedPreferences("USER_INFO", MODE_PRIVATE).getString("BuddyMail", "");
        nShots = getIntent().getExtras().getInt(MainActivity.EXTRA_CALL);
        text();
    }

    public void text() {
        if(checkPermission()) {
            //Get the default SmsManager//
            SmsManager smsManager = SmsManager.getDefault();

//Send the SMS//

            String message = "You've had a lot to drink (a whole " + nShots + " shots) ya dummy";
            smsManager.sendTextMessage(number, null, message, null, null);
            Toast.makeText(SMSActivity.this, number, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SMSActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
        finish();
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(SMSActivity.this, Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(SMSActivity.this,
                            "Permission accepted", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(SMSActivity.this,
                            "Permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}