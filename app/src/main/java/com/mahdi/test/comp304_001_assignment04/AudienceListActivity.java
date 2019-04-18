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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AudienceListActivity extends ListActivity {

    DbManager dbManager;
    List<Audience> audiences;

    SharedPreferences sharedUserName;
    String sharedUserNameStr;
    String sharedEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audience_list);

        //shared username
        sharedUserName =
                AudienceListActivity.this.getSharedPreferences("UserName", Context.MODE_PRIVATE);
        sharedUserNameStr = sharedUserName.getString("cappedUserName", "User");
        sharedEmail = sharedUserName.getString("emailId", "email");

        //set toolbar name and back button
        //get bookings based on user
        Toolbar toolbar = findViewById(R.id.toolbarAudienceList);

        dbManager = new DbManager(this);
        audiences = dbManager.getAudiences();


        //case admin, load all bookings, case audience, load only audience's bookings
        toolbar.setTitle("Audience List: "+sharedUserNameStr);
        setActionBar(toolbar);
        Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);

        AudienceListAdapter adapter = new AudienceListAdapter(this, audiences);
        setListAdapter(adapter);

    }//end of onCreate method


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //onClick on each item open view activity
        Intent intent = new Intent(AudienceListActivity.this, ProfileActivity.class);
        intent.putExtra("audience", audiences.get(position));
        startActivity(intent);
        finish();
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
