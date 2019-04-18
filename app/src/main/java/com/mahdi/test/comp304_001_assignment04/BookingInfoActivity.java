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
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Objects;

public class BookingInfoActivity extends AppCompatActivity {

    private Movie movie;
    private Theatre theatre;

    private LinearLayout ticketNoLay;
    private LinearLayout payInfo;
    private String showDate;
    private String showTime;

    private Booking booking;

    int amtAdult = 0;
    int amtChild = 0;
    int amtSenior = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_info);

        //get movie from movies page
        movie = (Movie) getIntent().getSerializableExtra("movie");
        theatre = (Theatre) getIntent().getSerializableExtra("theatre");

        //shared username
        SharedPreferences sharedUserName =
                BookingInfoActivity.this.getSharedPreferences("UserName", Context.MODE_PRIVATE);
        String userNameFromShared = sharedUserName.getString("cappedUserName", "User");

        //set toolbar name and back button
        Toolbar toolbar = findViewById(R.id.toolbarBookingInfo);
        toolbar.setTitle("Booking Information");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        //page elements
        //set movie name and user name on top of page
        TextView movieTitle = findViewById(R.id.bInfoMovieTitle);
        movieTitle.setText(movie.getMovieName());
        TextView theatreName = findViewById(R.id.theatreShowInfo);
        theatreName.setText(theatre.name);
        TextView theatreCity = findViewById(R.id.theatreCity);
        theatreCity.setText(theatre.city);
        TextView userName = findViewById(R.id.bInfoUserName);
        userName.setText(userNameFromShared);

        //hide ticket number
        ticketNoLay = findViewById(R.id.ticketInfoLayout);
        ticketNoLay.setVisibility(View.GONE);
        //hide payment info
        payInfo = findViewById(R.id.paymentInfoLayout);
        payInfo.setVisibility(View.GONE);

        //submit button, hide
        final Button submit = findViewById(R.id.btnSubmitInfo);
        submit.setVisibility(View.GONE);

        //Radio Buttons for date and showtime
        final RadioGroup dateGroup = findViewById(R.id.showDateGroup);
        final RadioGroup timeGroup = findViewById(R.id.showTimeGroup);
        timeGroup.setVisibility(View.INVISIBLE);

        //date picker
        //Radio group initialization to create event listener for changing options
        dateGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(
                    RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton rb2 =
                            findViewById(checkedId);
                    if (rb2 != null) {
                        //assign the show date value
                        showDate = rb2.getText().toString();
                        //make the ticket number visible
                        timeGroup.setVisibility(View.VISIBLE);
                    }else{
                        timeGroup.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        //time picker
        //Radio group initialization to create event listener for changing option
        timeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(
                    RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton rb =
                            findViewById(checkedId);
                    if (rb != null) {
                        //assign the show time value
                        showTime = rb.getText().toString();
                        //make the ticket number visible
                        ticketNoLay.setVisibility(View.VISIBLE);
                    }else{
                        ticketNoLay.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        ///

        //Spinners for no of tickets
        //Initializing spinners and set a array of integers by adapter for its items
        final Spinner mSpinAdult= findViewById(R.id.spAdult);
        final Spinner mSpinChild= findViewById(R.id.spChild);
        final Spinner mSpinSenior= findViewById(R.id.spSenior);

        final Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, items);
        mSpinAdult.setAdapter(adapter);
        mSpinAdult.setEnabled(false);
        mSpinChild.setAdapter(adapter);
        mSpinChild.setEnabled(false);
        mSpinSenior.setAdapter(adapter);
        mSpinSenior.setEnabled(false);

        //initializing switchers and checkbox
        final CheckBox cbAdult = findViewById(R.id.cbAdult);
        final CheckBox cbChildren = findViewById(R.id.cbChild);
        final CheckBox cbSenior = findViewById(R.id.cbSenior);

        //event listeners for switch and checkbox if it is active
        //then activate the spinner to get the number of tickets
        cbAdult.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mSpinAdult.setEnabled(true);
                    amtAdult = 1;
                    payInfo.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                }else{
                    mSpinAdult.setEnabled(false);
                    amtAdult = 0;
                    if(!cbSenior.isChecked()&&!cbChildren.isChecked()){
                        payInfo.setVisibility(View.GONE);
                        submit.setVisibility(View.GONE);
                    }
                }
            }
        });
        cbChildren.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mSpinChild.setEnabled(true);
                    amtChild = 1;
                    payInfo.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                }else{
                    mSpinChild.setEnabled(false);
                    amtChild = 0;
                    if(!cbSenior.isChecked()&&!cbAdult.isChecked()){
                        payInfo.setVisibility(View.GONE);
                        submit.setVisibility(View.GONE);
                    }
                }
            }
        });
        cbSenior.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mSpinSenior.setEnabled(true);
                    amtSenior = 1;
                    payInfo.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                }else{
                    mSpinSenior.setEnabled(false);
                    amtSenior = 0;
                    if(!cbChildren.isChecked()&&!cbAdult.isChecked()){
                        payInfo.setVisibility(View.GONE);
                        submit.setVisibility(View.GONE);
                    }
                }
            }
        });//end of ticket number spinners and checkboxes handling

        //payment info handling
        final Spinner cardSpin= findViewById(R.id.payMethod);
        final EditText cardNo = findViewById(R.id.cardNo);
        final EditText ccv = findViewById(R.id.ccv);
        String[] payCards = new String[]{"Master Card","Visa","American Express"};
        ArrayAdapter<String> adapterStr =
                new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, payCards);
        cardSpin.setAdapter(adapterStr);

        //Initialize the button to accept the values for ticket amounts
        Button btnTicketInfo = findViewById(R.id.btnSubmitInfo);
        btnTicketInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //get number of tickets in each category
                if(cbAdult.isChecked()){
                    amtAdult = items[mSpinAdult.getSelectedItemPosition()];
                }
                if(cbChildren.isChecked()){
                    amtChild = items[mSpinChild.getSelectedItemPosition()];
                }
                if(cbSenior.isChecked()){
                    amtSenior = items[mSpinSenior.getSelectedItemPosition()];
                }

                //validate and get payment method info
                boolean ccvReady = Statics.checkValue(ccv, getResources());
                boolean cardNoReady = Statics.checkValue(cardNo, getResources());

                if(ccvReady && cardNoReady){
                    //create a booking object without saving in database with PENDING booking status
                    //for passing to confirm and check out
                    booking = new Booking(
                            movie.getMovieId(), showDate, showTime, amtAdult, amtChild,amtSenior
                    );

                    String cardTypeStr = cardSpin.getSelectedItem().toString();
                    String ccvStr = ccv.getText().toString()+"";
                    String cardNoStr = cardNo.getText().toString()+"";

                    //sending all information to checkout for last check and confirm by user
                    Intent intent = new Intent(BookingInfoActivity.this, CheckOutActivity.class);
                    intent.putExtra("movie", movie);
                    intent.putExtra("booking", booking);
                    intent.putExtra("cardType", cardTypeStr);
                    intent.putExtra("cardNo", cardNoStr);
                    intent.putExtra("ccv", ccvStr);
                    startActivityForResult(intent, 1);
                }
            }
        });//end of Submit Button Handler
    }//end of OnCreate method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent( BookingInfoActivity.this, BookingListActivity.class);
                startActivity(intent);
                setResult(RESULT_OK);
                this.finishActivity(1);
                onBackPressed();
            }
        }
    }

    //back button on action bar doing same as back button press on phone
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
