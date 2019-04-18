package com.mahdi.test.comp304_001_assignment04;
/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_confirm);

        //get audience object from intent
        Audience audience =(Audience) getIntent().getSerializableExtra("Audience");

        //Set values to text views
        TextView userNameTxt = findViewById(R.id.userNameShow);
        userNameTxt.setText(audience.getUserName());
        TextView passTxt = findViewById(R.id.passwordShow);
        passTxt.setText(audience.getPassword());
        TextView emailTxt = findViewById(R.id.emailShow);
        emailTxt.setText(audience.getEmail());
        TextView fNameTxt = findViewById(R.id.fNameShow);
        fNameTxt.setText(audience.getFName());
        TextView lNameTxt = findViewById(R.id.lNameShow);
        lNameTxt.setText(audience.getLName());
        TextView ageTxt = findViewById(R.id.ageShow);
        ageTxt.setText(String.valueOf(audience.getAge()));
        TextView addressTxt = findViewById(R.id.addressShow);
        addressTxt.setText(audience.getAddress());
        TextView cityTxt = findViewById(R.id.cityShow);
        cityTxt.setText(audience.getCity());
        TextView postalCodeTxt = findViewById(R.id.postalCodeShow);
        postalCodeTxt.setText(audience.getPostalCode());
    }

    public void OnConfirmClicked(View v){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
