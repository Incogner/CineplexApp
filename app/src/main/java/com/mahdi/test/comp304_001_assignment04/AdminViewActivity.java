package com.mahdi.test.comp304_001_assignment04;
/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


public class AdminViewActivity extends AppCompatActivity {

    //data objects
    SharedPreferences sharedUserName;
    String sharedUserNameStr;
    long userId;
    Admin admin;

    //view objects
    TextView greetings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        //shared username
        sharedUserName =
                AdminViewActivity.this.getSharedPreferences("UserName", Context.MODE_PRIVATE);
        sharedUserNameStr = sharedUserName.getString("cappedUserName", "User");
        userId = sharedUserName.getLong("userID", 0);

        //get admin from intent
        admin = (Admin) getIntent().getSerializableExtra("admin");

        //set Toolbar properties
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAdminView);
        toolbar.setTitle("Welcome to Dashboard "+sharedUserNameStr);
        setSupportActionBar(toolbar);

        greetings = (TextView) findViewById(R.id.txtAViewGreet);
        greetings.setText(String.format(
                "Hi %s!%n%nAs an admin, here you can view, edit, delete and manage data",
                admin.getFirstName()));

    }//end of onCreate Method

    public void onButtonClicked(View v){
        Intent intent;
        switch(v.getId()){
            case R.id.btnAViewAudiences:
                intent = new Intent(AdminViewActivity.this, AudienceListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnAViewBookings:
                intent = new Intent(AdminViewActivity.this, BookingListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnAViewMovies:
                intent = new Intent(AdminViewActivity.this, MoviesActivity.class);
                startActivity(intent);
                break;
            case R.id.btnAViewLogout:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminViewActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
