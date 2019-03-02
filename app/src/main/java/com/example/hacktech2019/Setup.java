package com.example.hacktech2019;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;

public class Setup extends AppCompatActivity {
    String name;
    int age;
    int weight;
    String gender;
    String myMail;
    String buddyMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        setUserValues();
    }

    public void setUserValues(){
        SharedPreferences info = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        age = info.getInt("Age", 0);
        weight = info.getInt("Weight", 0);
        name = info.getString("Name", "");
        gender = info.getString("Gender", "");
        myMail = info.getString("MyMail", "");
        buddyMail = info.getString("BuddyMail", "");

        EditText nameIn = (EditText) findViewById(R.id.nameInput);
        nameIn.setText(name);

        EditText ageIn = (EditText) findViewById(R.id.ageInput);
        ageIn.setText(Integer.toString(age));

        EditText weightIn = (EditText) findViewById(R.id.weightInput);
        weightIn.setText(Integer.toString(weight));

        EditText myMailIn = (EditText) findViewById(R.id.myMailInput);
        myMailIn.setText(myMail);

        EditText buddyMailIn = (EditText) findViewById(R.id.buddyMailInput);
        buddyMailIn.setText(buddyMail);
    }


    public void getUserValues(){
        EditText nameIn = (EditText) findViewById(R.id.nameInput);
        name = nameIn.getText().toString();

        EditText myMailIn = (EditText) findViewById(R.id.myMailInput);
        myMail = myMailIn.getText().toString();

        EditText buddyMailIn = (EditText) findViewById(R.id.buddyMailInput);
        buddyMail = buddyMailIn.getText().toString();

        EditText ageIn = (EditText) findViewById(R.id.ageInput);
        age = Integer.parseInt(ageIn.getText().toString());

        EditText weightIn = (EditText) findViewById(R.id.weightInput);
        weight = Integer.parseInt(weightIn.getText().toString());
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        int numChecked = 0;
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.male:
                if (checked){
                    gender = "male";
                    numChecked++;
                }
            case R.id.female:
                if (checked){
                    gender = "female";
                    numChecked++;
                }
            case R.id.otherGender:
                if(checked && numChecked == 0){
                    gender = "female";
                }
            else
                // I'm lactose intolerant
                break;

        }
        if(numChecked > 1){
            TextView errorBox = (TextView) findViewById(R.id.genderError);
            errorBox.setText(R.string.gender_error_message);
        }
    }

    public void setPersonalInfo(View view){
        this.getUserValues();

        if(gender == null) {
            TextView errorBox = (TextView) findViewById(R.id.genderError);
            errorBox.setText(R.string.gender_error_message);
        }

        SharedPreferences.Editor edit = getSharedPreferences("USER_INFO", MODE_PRIVATE).edit();
        edit.putString("Name", name).commit();
        edit.putString("Gender", gender).commit();
        edit.putString("MyMail", myMail).commit();
        edit.putString("BuddyMail", buddyMail).commit();
        edit.putInt("Age", age).commit();
        edit.putInt("Weight", weight).commit();
        edit.apply();

        // Might want to do some error handling/data checking here in the future
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().apply();
        finish();
    }
}

