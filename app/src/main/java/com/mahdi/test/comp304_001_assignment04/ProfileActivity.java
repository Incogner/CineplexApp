package com.mahdi.test.comp304_001_assignment04;
/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    //database helper
    DbManager dbManager;

    //toolbar
    Toolbar toolbar;

    //popup window objects
    LinearLayout mLinearLayout;
    PopupWindow mPopupWindow;
    Context context;

    //edit text and text view objects
    EditText userName, fName, lName, age,phoneNo, address, city, postalCode;
    TextView emailId;

    //audience object for user
    Audience audience;
    boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //getting userName and id from Shared preference
        SharedPreferences sharedUserName =
                ProfileActivity.this.getSharedPreferences("UserName", Context.MODE_PRIVATE);
        String userNameCapped = sharedUserName.getString("cappedUserName", "User");
        long sharedId = sharedUserName.getLong("userID", 0);
        isAdmin = sharedUserName.getBoolean("isAdmin", false);

        //get audience from database
        context = ProfileActivity.this;
        dbManager = new DbManager(context);
        //setting toolbar
        toolbar = findViewById(R.id.toolbarProfile);

        if(isAdmin && getIntent().hasExtra("audience")){
            audience = (Audience) getIntent().getSerializableExtra("audience");
            toolbar.setTitle("Audience: "+audience.getUserName()+", ID: "+audience.getId());
        }else{
            audience = dbManager.getAudience(sharedId);
            toolbar.setTitle("Profile: "+userNameCapped+", ID: "+sharedId);

            Button btnDelete = findViewById(R.id.btnProfileDelete);
            btnDelete.setVisibility(View.GONE);
        }

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        //adding edit boxes and assign onFocusChangeListener
        emailId = findViewById(R.id.emailProf);
        emailId.setText(audience.getEmail());
        userName = findViewById(R.id.userNameProf);
        userName.setText(audience.getUserName());
        fName = findViewById(R.id.fNameProf);
        fName.setText(audience.getFName());
        lName = findViewById(R.id.lNameProf);
        lName.setText(audience.getLName());
        age = findViewById(R.id.ageProf);
        age.setText(String.valueOf(audience.getAge()));
        phoneNo = findViewById(R.id.phoneProf);
        phoneNo.setText(String.valueOf(audience.getPhoneNo()));
        address = findViewById(R.id.addressProf);
        address.setText(audience.getAddress());
        city = findViewById(R.id.cityProf);
        city.setText(audience.getCity());
        postalCode = findViewById(R.id.postalCodeProf);
        postalCode.setText(audience.getPostalCode());

        //assign values for popup window objects
        mLinearLayout = findViewById(R.id.profileLinLayout);

    }//end of onCreate method

    //change password method
    public void onChangePassClicked(View v){

        //instantiate and assign objects for popup window
        LayoutInflater layoutInflater = (LayoutInflater)
                ProfileActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View customView = layoutInflater.inflate(R.layout.popup_pass_change,null);

        final EditText current = customView.findViewById(R.id.currentPassPopup);
        final EditText newPass = customView.findViewById(R.id.newPassPopup);
        final EditText repNewPass = customView.findViewById(R.id.repeatPassPopup);

        Button close = customView.findViewById(R.id.btnPopupClose);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        //handle the save click button
        Button save = customView.findViewById(R.id.btnPopupSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validations
                boolean currentValue = Statics.checkValue(current, getResources());
                boolean newPassValue = Statics.checkValue(newPass, getResources());
                boolean repNewValue = Statics.checkValue(repNewPass, getResources());
                if(currentValue && !audience.getPassword().equals(current.getText().toString()+"")){
                    Statics.setError(current,"Current Password is Wrong!", getResources());
                    currentValue = false;
                }
                String newPassStr =newPass.getText().toString()+"";
                String repNewPassStr =repNewPass.getText().toString()+"";
                if(newPassValue && repNewValue && !newPassStr.equals(repNewPassStr)){
                    Statics.setError(repNewPass,"Password does not match!", getResources());
                    repNewValue = false;
                }

                if(currentValue && newPassValue && repNewValue){
                    //update audience after validations
                    audience.setPassword(newPassStr);
                    boolean isDone = audience.updatePassword(dbManager);
                    if(isDone){
                        Toast.makeText(context, "Password Updated!", Toast.LENGTH_LONG).show();
                        mPopupWindow.dismiss();
                    }else{
                        Toast.makeText(context, "Update Error: can not change!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });//end of click listener for save button

        //instantiate popup window
        mPopupWindow = new PopupWindow(
                customView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);

        // Set an elevation value for popup window
        mPopupWindow.setElevation(10.0f);
        //display the popup window
        mPopupWindow.showAtLocation(mLinearLayout, Gravity.CENTER_HORIZONTAL, 0, 0);
    }//end of change password click method

    //method to handle update button
    public void onUpdateClicked(View v){
        boolean userNameValue = Statics.checkValue(userName, getResources());
        boolean fNameValue = Statics.checkValue(fName, getResources());
        boolean lNameValue = Statics.checkValue(lName, getResources());
        boolean ageValue = Statics.checkValue(age, getResources());
        boolean phoneValue = Statics.checkValue(phoneNo, getResources());
        int ageInt = 0;
        long phoneLong = 0;
        //check for ages between 16 and 75
        if(ageValue){
            ageInt = Integer.parseInt(age.getText().toString());
            if(ageInt<15 || ageInt>76){
                ageValue = false;
                Statics.setError(age, "15<Age<76", getResources());
            }else{
                ageValue = true;
                Statics.removeError(age, getResources());
            }
        }
        if(phoneValue){
            phoneLong = Long.parseLong(phoneNo.getText().toString());
            if(phoneLong<1000000000 || phoneLong>9999999999f){
                phoneValue = false;
                Statics.setError(phoneNo, "wrong format", getResources());
            }else{
                phoneValue = true;
                Statics.removeError(phoneNo, getResources());
            }
        }

        //if username and first and last name and age are not empty run update code
        if(userNameValue && fNameValue && lNameValue && ageValue && phoneValue){
            String userNameStr = userName.getText().toString().toLowerCase();

            String fNameStr =
                    fName.getText().toString().substring(0,1).toUpperCase() +
                            fName.getText().toString().substring(1);
            String lNameStr =
                    lName.getText().toString().substring(0,1).toUpperCase() +
                            lName.getText().toString().substring(1);

            String cityStr = "";
            if(Statics.checkValue(city)){
                cityStr = city.getText().toString().substring(0,1).toUpperCase() +
                        city.getText().toString().substring(1);
            }

            //create database object
            dbManager = new DbManager(this);

            //update audience
            audience.setUserName(userNameStr);
            audience.setfName(fNameStr);
            audience.setlName(lNameStr);
            audience.setAge(ageInt);
            audience.setPhoneNo(phoneLong);
            audience.setAddress(""+address.getText().toString());
            audience.setCity(cityStr);
            audience.setPostalCode(""+postalCode.getText().toString());
            boolean isDone = audience.update(dbManager);
            if(isDone){
                Toast.makeText(this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Update Error: can not save!", Toast.LENGTH_SHORT).show();
            }

        }//end of if for validations
    }//end of OnUpdateClick method

    public void onDeleteClicked(View v){
        int done = dbManager.deleteAudience(audience.getId());
        if(done == 1){
            this.onBackPressed();
        }else{
            Toast.makeText(context, "Error: cannot delete!", Toast.LENGTH_SHORT).show();
        }

    }

    //back click method
    public void onBackClicked(View v){
        this.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if(isAdmin){
            Intent intent = new Intent(ProfileActivity.this, AudienceListActivity.class);
            startActivity(intent);
            finish();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.onBackPressed();
        return true;
    }
}//end of activity
