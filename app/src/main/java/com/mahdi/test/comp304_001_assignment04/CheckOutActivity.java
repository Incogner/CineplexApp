package com.mahdi.test.comp304_001_assignment04;
/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CheckOutActivity extends AppCompatActivity {

    //data objects
    DbManager dbManager;
    SharedPreferences sharedUserName;
    String sharedUserNameStr;
    long userId;
    Movie movie;
    Booking booking;
    String cardTypeStr, cardNoStr, ccvStr;
    double totalPrice;

    //view objects
    TextView userName, movieName, showDate, showTime, adultTicket, childTicket, seniorTicket,
        total, cardType, cardNo, ccv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        //shared username
        sharedUserName =
                CheckOutActivity.this.getSharedPreferences("UserName", Context.MODE_PRIVATE);
        sharedUserNameStr = sharedUserName.getString("cappedUserName", "User");
        userId = sharedUserName.getLong("userID", 0);

        //set Toolbar properties
        Toolbar toolbar = findViewById(R.id.toolbarCheckOut);
        toolbar.setTitle("Booking CheckOut");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //get information from intent
        movie = (Movie) getIntent().getSerializableExtra("movie");
        booking = (Booking) getIntent().getSerializableExtra("booking");
        cardTypeStr = getIntent().getStringExtra("cardType");
        cardNoStr = getIntent().getStringExtra("cardNo");
        ccvStr = getIntent().getStringExtra("ccv");

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
        totalPrice = (booking.getAdultTicket()* Statics.ADULT_PRICE)+
                (booking.getSeniorTicket()* Statics.SENIOR_PRICE)+
                (booking.getChildTicket()* Statics.CHILD_PRICE);
        String totalStr = NumberFormat.getCurrencyInstance(new Locale("en", "CA"))
                .format(totalPrice);

        //assign values to the text views
        userName = findViewById(R.id.txtChOutUserName);
        userName.setText(sharedUserNameStr);
        movieName = findViewById(R.id.txtChOutMovieName);
        movieName.setText(movie.getMovieName());
        showDate = findViewById(R.id.txtChOutShowDate);
        showDate.setText(booking.getShowDate());
        showTime = findViewById(R.id.txtChOutShowTime);
        showTime.setText(booking.getShowTime());
        adultTicket = findViewById(R.id.txtChOutAdultTickets);
        adultTicket.setText(String.format(Locale.CANADA,"%d x %s = %s",booking.getAdultTicket(),adultStr,adultStrTotal));
        childTicket = findViewById(R.id.txtChOutChildTickets);
        childTicket.setText(String.format(Locale.CANADA,"%d x %s = %s",booking.getChildTicket(),childStr,childStrTotal));
        seniorTicket = findViewById(R.id.txtChOutSeniorTickets);
        seniorTicket.setText(String.format(Locale.CANADA,"%d x %s = %s",booking.getSeniorTicket(),seniorStr,seniorStrTotal));
        total = findViewById(R.id.txtChOutTotal);
        total.setText(totalStr);
        cardType = findViewById(R.id.txtChOutCardType);
        cardType.setText(cardTypeStr);
        cardNo = findViewById(R.id.txtChOutCardNo);
        cardNo.setText(cardNoStr);
        ccv = findViewById(R.id.txtChOutCCV);
        ccv.setText(ccvStr);

    }

    public void onConfirmClicked(View v){

        dbManager = new DbManager(this);

        //get EmailId for User
        String emailId = dbManager.getEmailById(userId);
        //get Today Date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        Date date = new Date();
        String todayDate = formatter.format(date);

        //Create New Booking Object saved in Database as Confirmed Booking
        booking = new Booking(
                dbManager, emailId, booking.getMovieId(), todayDate, booking.getAdultTicket(),
                booking.getChildTicket(), booking.getSeniorTicket(), totalPrice,
                booking.getShowDate(), booking.getShowTime()
        );

        //open booking list Activity
        setResult(RESULT_OK);
        this.finishActivity(1);
        onBackPressed();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.onBackPressed();
        return true;
    }
}
