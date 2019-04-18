package com.mahdi.test.comp304_001_assignment04;
/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnFocusChangeListener{

    //database helper
    DbManager dbManager;
    //toolbar
    Toolbar toolbar;

    //edit text objects
    EditText emailId, userName, password, password2,
            fName, lName, age,phoneNo, address, city, postalCode;

    //validations handling objects
    int[] errors;
    EditText[] fields;
    boolean isReady;
    //audience object
    Audience audience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //setting toolbar
        toolbar = findViewById(R.id.toolbarReg);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //create database object
        dbManager = new DbManager(this);

        //adding edit boxes and assign onFocusChangeListener
        emailId = findViewById(R.id.emailReg);
        emailId.setOnFocusChangeListener(this);
        userName = findViewById(R.id.userNameReg);
        userName.setOnFocusChangeListener(this);
        password = findViewById(R.id.passwordReg);
        password.setOnFocusChangeListener(this);
        password2 = findViewById(R.id.passwordReg2);
        password2.setOnFocusChangeListener(this);
        fName = findViewById(R.id.fNameReg);
        fName.setOnFocusChangeListener(this);
        lName = findViewById(R.id.lNameReg);
        lName.setOnFocusChangeListener(this);
        age = findViewById(R.id.ageReg);
        age.setOnFocusChangeListener(this);
        phoneNo = findViewById(R.id.phoneReg);
        phoneNo.setOnFocusChangeListener(this);
        address = findViewById(R.id.addressReg);
        city = findViewById(R.id.cityReg);
        postalCode = findViewById(R.id.postalCodeReg);

        //variables to check for validations
        errors = new int[]{1, 1, 1, 1, 1, 1, 1,1};
        fields = new EditText[]{emailId, userName, password, password2, age,phoneNo, fName, lName};
        //
        isReady = false;
        //
    }//end of OnCreate method

    //method to submit the registration
    public void onRegisterClicked(View v){
        //check for errors
        int sum = 0;
        for(int x : errors ){
            sum += x;
        }
        if(sum != 0 || !isReady){
            for(int x = 0; x<errors.length; x++){
                onFocusChange(fields[x], true);
                if(errors[x] == 0){
                    Statics.removeError(fields[x], getResources());
                }else if(errors[x] == 1){
                    Statics.setError(fields[x], "cannot be empty!", getResources());
                }
            }
        }else {
            String fNameStr =
                    fName.getText().toString().substring(0,1).toUpperCase() +
                            fName.getText().toString().substring(1);
            String lNameStr =
                    lName.getText().toString().substring(0,1).toUpperCase() +
                            lName.getText().toString().substring(1);
            String cityStr = "";
            if(city.getText().toString().length()>0){
                cityStr = city.getText().toString().substring(0,1).toUpperCase() +
                        city.getText().toString().substring(1);
            }
            //save to database(create record)
            audience = new Audience(dbManager,
                     userName.getText().toString().toLowerCase(), emailId.getText().toString().toLowerCase(),
                    password.getText().toString(), fNameStr,
                    lNameStr, Integer.parseInt(age.getText().toString()),
                    Long.parseLong(phoneNo.getText().toString()),
                    address.getText().toString()+"", cityStr,
                    postalCode.getText().toString().toUpperCase()+""
            );

            //if there is no error in inserting data to database
            long ret = audience.getId();

            //return error if data can not be inserted
            if(ret < 1){
                Toast.makeText(this,
                        "Error: Registration failed!",
                        Toast.LENGTH_SHORT).show();
            }else{
                //send confirmation sms to user
                sendSmsMsg(
                        phoneNo.getText().toString(),
                        "Thank you for registering with us!"
                );
                //sendSms(phoneNo.getText().toString());
                Intent intent = new Intent(RegisterActivity.this, RegConfirmActivity.class);
                intent.putExtra("Audience", audience);
                startActivity(intent);
                finish();
            }
        }
    }//end of OnRegisterClicked method

    void sendSmsMsg(String phone_no, String message)
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
        {
            try
            {
                SmsManager smsMgrVar = SmsManager.getDefault();
                smsMgrVar.sendTextMessage(phone_no, null, message, null, null);
                Toast.makeText(getApplicationContext(), "Confirmation Sent",
                        Toast.LENGTH_LONG).show();
            }
            catch (Exception ErrVar)
            {
                Toast.makeText(getApplicationContext(),ErrVar.getMessage(),
                        Toast.LENGTH_LONG).show();
                ErrVar.printStackTrace();
            }
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
                sendSmsMsg(phone_no,message);
            }
        }

    }

    public void sendLongSMS() {
        String phoneNumber = "0123456789";
        String message = "Hello World! Now we are going to demonstrate " +
                "how to send a message with more than 160 characters from your Android application.";
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
    }

    public void sendSms(String phoneNo){
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        //smsIntent.setData(Uri.parse("smsto:"));
        //smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.setDataAndType(Uri.parse("smsto:"), "vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , phoneNo);
        smsIntent.putExtra("sms_body"  , "Welcome to Cineplex "+fName.getText().toString()+"! ");

        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(RegisterActivity.this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    //This method validating the inputs
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {
            case R.id.emailReg:
                String email = emailId.getText().toString() + "";
                String result = dbManager.getEmails(email);
                if (email.equals("")) {
                    errors[0] = 1;
                } else if (!Statics.isEmailValid(email)) {
                    isReady = false;
                    Statics.setError(emailId, "Email not valid!", getResources());
                } else if (result.length() != 0) {
                    isReady = false;
                    Statics.setError(emailId, "This email already registered!", getResources());
                } else {
                    isReady = true;
                    errors[0] = 0;
                    Statics.removeError(emailId, getResources());
                }
                break;

            case R.id.userNameReg:
                if (!Statics.checkValue(userName)) {
                    errors[1]=1;
                } else {
                    errors[1]=0;
                    Statics.removeError(userName, getResources());
                }
                break;

            case R.id.passwordReg:
                String pass = password.getText().toString()+"";
                if (pass.equals("")) {
                    errors[2]=1;
                } else {
                    isReady = true;
                    errors[2]=0;
                    Statics.removeError(password, getResources());
                }
                break;

            case R.id.passwordReg2:
                String pass1 = password.getText().toString()+"";
                String pass2 = password2.getText().toString()+"";
                if(!Statics.checkValue(password2)){
                    errors[3]=1;
                }else if (!pass2.equals(pass1)) {
                    isReady = false;
                    Statics.setError(password2, "Passwords not matching!", getResources());
                } else {
                    isReady = true;
                    errors[3]=0;
                    Statics.removeError(password2, getResources());
                }
                break;

            case R.id.ageReg:
                int ageVal = 16;
                if(Statics.checkValue(age))
                    ageVal = Integer.parseInt(age.getText().toString()+"");
                if ((age.getText().toString()+"").equals("")) {
                    errors[4]=1;
                }else if(ageVal<15 || ageVal>76){
                        isReady = false;
                        Statics.setError(age, "15<Age<76", getResources());
                }else{
                    isReady = true;
                    errors[4]=0;
                    Statics.removeError(age, getResources());
                }
                break;
            case R.id.phoneReg:
                long phoneVal = 0;
                if(Statics.checkValue(phoneNo))
                    phoneVal = Long.parseLong(phoneNo.getText().toString()+"");
                if ((phoneNo.getText().toString()+"").equals("")) {
                    errors[5]=1;
                }else if(phoneVal < 1000000000 || phoneVal > 9999999999f){
                    isReady = false;
                    Statics.setError(phoneNo, "wrong format", getResources());
                }else{
                    isReady = true;
                    errors[5]=0;
                    Statics.removeError(phoneNo, getResources());
                }
                break;

            case R.id.fNameReg:
                if (!Statics.checkValue(fName)) {
                    errors[6]=1;
                } else {
                    errors[6]=0;
                    Statics.removeError(fName, getResources());
                }
                break;

            case R.id.lNameReg:
                if (!Statics.checkValue(lName)) {
                    errors[7]=1;
                } else {
                    errors[7]=0;
                    Statics.removeError(lName, getResources());
                }
                break;
        }//end of Switch
    }//end of OnFocusChange method

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
