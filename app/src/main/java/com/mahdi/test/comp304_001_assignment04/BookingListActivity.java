package com.mahdi.test.comp304_001_assignment04;
/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BookingListActivity extends ListActivity {

    DbManager dbManager;
    List<Booking> bookings;
    List<Movie> movies;
    List<Audience> audiences;
    ArrayAdapter<String> adapterMenu;
    List<Booking> filtered;
    boolean isFiletered;

    SharedPreferences sharedUserName;
    String sharedUserNameStr;
    String sharedEmail;
    Boolean isAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        //shared username
        sharedUserName =
                BookingListActivity.this.getSharedPreferences("UserName", Context.MODE_PRIVATE);
        sharedUserNameStr = sharedUserName.getString("cappedUserName", "User");
        isAdmin = sharedUserName.getBoolean("isAdmin", false);
        sharedEmail = sharedUserName.getString("emailId", "email");

        //set toolbar name and back button
        //get bookings based on user
        Toolbar toolbar = findViewById(R.id.toolbarBookingList);

        dbManager = new DbManager(this);
        //load movies information
        movies = dbManager.getMovies();

        //case admin, load all bookings, case audience, load only audience's bookings
        if(isAdmin){//user is admin
            toolbar.setTitle("Booking List: "+sharedUserNameStr);
            //get all bookings
            bookings = dbManager.getBookings();
            //get users list and set spinner on toolbar for user selection
            audiences = dbManager.getAudiences();
            String[] userList = new String[audiences.size()+1];
            userList[0]=String.format(Locale.CANADA,"All Users (%d) %d bookings", audiences.size(), bookings.size());
            for(int x = 1; x<audiences.size()+1; x++){

                //get number of each user's bookings
                List<Booking> filtered = filterBookingByEmail(audiences.get(x-1).getEmail());
                userList[x] = String.format(Locale.CANADA,"%s, %s, %d bookings",
                        audiences.get(x-1).getId(),audiences.get(x-1).getUserName(), filtered.size());
            }
            adapterMenu =
                    new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, userList);
            Spinner spinner = findViewById(R.id.spinnerMenu);
            adapterMenu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterMenu);
            //handle spinner item selection
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 0){//position 0 means all users
                        loadList(bookings);
                        isFiletered = false;
                    }else{
                        filtered = filterBookingByEmail(audiences.get(position-1).getEmail());
                        loadList(filtered);
                        isFiletered = true;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(BookingListActivity.this, "Error: Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        }else {//user is not admin
            toolbar.setTitle(sharedUserNameStr+"'s Bookings");
            Toolbar second = findViewById(R.id.toolbarBookingList2);
            second.setVisibility(View.GONE);
            bookings = dbManager.getBookings(sharedEmail);
            loadList(bookings);
            isFiletered = false;
        }
        setActionBar(toolbar);
        Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);

    }//end of onCreate method

    //this method loading the list of bookings
    private void loadList(List<Booking> bookings){
        //load the list by adapter and set adapter to view
        BookingListAdapter adapter = new BookingListAdapter(this, bookings, movies);
        setListAdapter(adapter);
    }

    //this method will filter the list of bookings
    private List<Booking> filterBookingByEmail(String emailFilter) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings) {
            if (emailFilter.equals(b.getEmailId())) {
                result.add(b);
            }
        }
        return result;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //onClick on each item open view activity
        Intent intent = new Intent(BookingListActivity.this, BookingViewActivity.class);
        //check is the list is filtered so send the filtered list to intent
        if(isFiletered){
            intent.putExtra("booking", filtered.get(position));
            intent.putExtra("movie", movies.get((int)filtered.get(position).getMovieId()-1));
        }else{
            intent.putExtra("booking", bookings.get(position));
            intent.putExtra("movie", movies.get((int)bookings.get(position).getMovieId()-1));
        }
        startActivity(intent);
        if(isAdmin){
            finish();
        }
    }

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
