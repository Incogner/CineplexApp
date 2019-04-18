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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MoviesActivity extends AppCompatActivity {

    DbManager dbManager;

    List<Movie> movies;
    Boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        dbManager = new DbManager(this);

        //loading movie list from database
        movies = dbManager.getMovies();
        if(movies.size() <1){
            dbManager.insertMovies();
            movies = dbManager.getMovies();
        }

        //shared username
        SharedPreferences sharedUserName =
                MoviesActivity.this.getSharedPreferences("UserName", Context.MODE_PRIVATE);
        String userNameCapped = sharedUserName.getString("cappedUserName", "User");
        isAdmin = sharedUserName.getBoolean("isAdmin", false);

        Toolbar toolbar = findViewById(R.id.toolbarMovies);
        toolbar.setTitle("Welcome "+userNameCapped);
        //show back button for admins only
        if(isAdmin){
            toolbar.setTitle("Movie List, "+userNameCapped);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.viewpagerMovies);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabsMovies);
        tabLayout.setupWithViewPager(viewPager);
    }

    //this method create multiple tabs and fragments depend on size of movie list
    private void setupViewPager(ViewPager viewPager) {
        MoviesActivity.ViewPagerAdapter adapter
                = new MoviesActivity.ViewPagerAdapter(getSupportFragmentManager());
        for(Movie movie : movies){
            adapter.addFragment(
                    MovieFragment.newInstance(movie), movie.getMovieName());
        }
        viewPager.setAdapter(adapter);
    }

    //viewPage adapter class for populating movie fragments
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    //load the user menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //do no show menu for admins
        if(!isAdmin){
            //inflating the menu for this activity
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
        }
        return true;
    }

    //handle menu item click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m1:
                Intent intent1 = new Intent(this, ProfileActivity.class);
                this.startActivity(intent1);
                break;
            case R.id.m3:
                Intent intent2 = new Intent(this, BookingListActivity.class);
                this.startActivity(intent2);
                break;
            case R.id.m4:
                Intent intent4 = new Intent(this, CustomerServiceActivity.class);
                this.startActivity(intent4);
                break;
            case R.id.m5:
                onBackPressed();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(isAdmin){
            super.onBackPressed();
        }else{//do not open main activity if user is admin
            Intent intent3 = new Intent(this, MainActivity.class);
            this.startActivity(intent3);
            finish();
        }
    }
}
