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
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class BookingViewActivity extends AppCompatActivity {

    //data objects
    DbManager dbManager;
    SharedPreferences sharedUserName;
    boolean isAdmin;
    Movie movie;
    Booking booking;
    String[] spValues;

    //view objects
    TextView userName, movieName, showDate, showTime, adultTicket, childTicket, seniorTicket,
            amtPaid, bookingStatus, paymentDate;
    ImageView icon;
    Spinner spStatus;
    Button saveInfo, delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_view);

        //shared username
        sharedUserName =
                BookingViewActivity.this.getSharedPreferences("UserName", Context.MODE_PRIVATE);
        isAdmin = sharedUserName.getBoolean("isAdmin", false);

        //set Toolbar properties
        Toolbar toolbar = findViewById(R.id.toolbarBookingView);
        toolbar.setTitle("Booking Details");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //get information from intent
        movie = (Movie) getIntent().getSerializableExtra("movie");
        booking = (Booking) getIntent().getSerializableExtra("booking");




        //price calculations
        String adultStr = NumberFormat.getCurrencyInstance(new Locale("en", "CA"))
                .format(Statics.ADULT_PRICE);
        String adultStrTotal = NumberFormat.getCurrencyInstance(new Locale("en", "CA"))
                .format((Statics.ADULT_PRICE*booking.getAdultTicket()));
        String seniorStr = NumberFormat.getCurrencyInstance(new Locale("en", "CA"))
                .format(Statics.SENIOR_PRICE);
        String seniorStrTotal = NumberFormat.getCurrencyInstance(new Locale("en", "CA"))
                .format((Statics.SENIOR_PRICE*booking.getSeniorTicket()));
        String childStr = NumberFormat.getCurrencyInstance(new Locale("en", "CA"))
                .format(Statics.CHILD_PRICE);
        String childStrTotal = NumberFormat.getCurrencyInstance(new Locale("en", "CA"))
                .format((Statics.CHILD_PRICE*booking.getChildTicket()));
        String totalStr = NumberFormat.getCurrencyInstance(new Locale("en", "CA"))
                .format(booking.getAmountPaid());

        //assign values to the text views
        int resID = getResources().getIdentifier(
                movie.getPicId(), "drawable", getPackageName());
        icon = findViewById(R.id.iconBViewMovie);
        icon.setImageResource(resID);
        userName = findViewById(R.id.txtBViewUserName);
        userName.setText(booking.getEmailId());
        movieName = findViewById(R.id.txtBViewMovieName);
        movieName.setText(movie.getMovieName());
        showDate = findViewById(R.id.txtBViewShowDate);
        showDate.setText(booking.getShowDate());
        showTime = findViewById(R.id.txtBViewShowTime);
        showTime.setText(booking.getShowTime());
        adultTicket = findViewById(R.id.txtBViewAdultTickets);
        adultTicket.setText(String.format(Locale.CANADA,"%d x %s = %s",booking.getAdultTicket(),adultStr,adultStrTotal));
        childTicket = findViewById(R.id.txtBViewChildTickets);
        childTicket.setText(String.format(Locale.CANADA,"%d x %s = %s",booking.getChildTicket(),childStr,childStrTotal));
        seniorTicket = findViewById(R.id.txtBViewSeniorTickets);
        seniorTicket.setText(String.format(Locale.CANADA,"%d x %s = %s",booking.getSeniorTicket(),seniorStr,seniorStrTotal));
        amtPaid = findViewById(R.id.txtBViewTotal);
        amtPaid.setText(totalStr);
        paymentDate = findViewById(R.id.txtBViewPayDate);
        paymentDate.setText(booking.getPaymentDate());

        bookingStatus = findViewById(R.id.txtBViewStatus);
        spStatus = findViewById(R.id.spinnerStatus);
        //load spinner values
        spValues = new String[] {
                Statics.CONFIRMED, Statics.CANCELLED, Statics.PENDING, Statics.REFUNDED};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(adapter);
        int selection = 0;
        switch (booking.getBookingStatus()){
            case Statics.CONFIRMED:
                selection = 0;
                break;
            case Statics.CANCELLED:
                selection = 1;
                break;
            case Statics.PENDING:
                selection = 2;
                break;
            case Statics.REFUNDED:
                selection = 3;
                break;
        }
        spStatus.setSelection(selection);

        saveInfo = findViewById(R.id.btnBViewUpdate);
        delete = findViewById(R.id.btnBViewDelete);

        //check for admin user
        if(isAdmin){//if user is admin
            bookingStatus.setVisibility(View.GONE);

        }else{//user is not admin
            spStatus.setVisibility(View.GONE);
            saveInfo.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.GONE);
            //status coloring
            String status = booking.getBookingStatus();
            switch (status){
                case Statics.CONFIRMED:
                    bookingStatus.setTextColor(Color.parseColor("#5db583"));
                    break;
                case Statics.CANCELLED:
                case Statics.REFUNDED:
                    bookingStatus.setTextColor(Color.RED);
                    break;
                case Statics.PENDING:
                    bookingStatus.setTextColor(Color.GRAY);
                    break;
            }
            bookingStatus.setText(status);
        }//end of if Admin
    }//end of onCreate Method

    //save button handling
    public void onSaveClicked(View v){
        dbManager = new DbManager(this);
        int done = dbManager.updateBookingStatus(
                booking.getBookingId(), spValues[spStatus.getSelectedItemPosition()]);
        if(done == 1){
            Toast.makeText(this, "Status Saved!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Error: Cannot save!", Toast.LENGTH_SHORT).show();
        }
    }

    //delete button handling
    public void onDeleteClicked(View v){
        dbManager = new DbManager(this);
        int done = dbManager.deleteBooking(booking.getBookingId());
        if(done == 1){
            onBackPressed();
        }else{
            Toast.makeText(this, "Error: Cannot delete!", Toast.LENGTH_SHORT).show();
        }
    }//end of onDeleteClicked method

    public void onDismissClicked(View v){

        this.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if(isAdmin){
            Intent intent = new Intent(BookingViewActivity.this, BookingListActivity.class);
            startActivity(intent);
            finish();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.onBackPressed();
        return true;
    }
}
