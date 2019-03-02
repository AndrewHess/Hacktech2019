package com.example.hacktech2019;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;

public class Setup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }

    String name;
    int age;
    int weight;
    String gender;

    TextView errorBox = (TextView) findViewById(R.id.genderError);
    String genderErrorMessage = "Please select one";

    public void getUserValues(){
        EditText nameIn = (EditText) findViewById(R.id.nameInput);
        name = nameIn.getText().toString();

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
            errorBox.setText(genderErrorMessage);
        }
    }

    public void setPersonalInfo(View view){
        this.getUserValues();
        if(gender == null) {
            errorBox.setText(genderErrorMessage);
        }
        // Might want to do some error handling/data checking here in the future
        Intent startMain = new Intent(this, MainActivity.class);
        startActivity(startMain);
    }
}

