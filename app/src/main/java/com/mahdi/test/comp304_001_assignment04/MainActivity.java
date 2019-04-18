package com.mahdi.test.comp304_001_assignment04;

/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //layout Objects
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //set toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set view pager for tab fragments
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        //tab layout and setup adapter to the layout to populate pages and tabs
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    //method to add tabs and pages to the adapter and set adapter to ViewPager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentUser(), "Login/Register");
        adapter.addFragment(new FragmentAdmin(), "Admin Login");
        viewPager.setAdapter(adapter);
    }

    //ViewPagerAdapter Custom class to feed ViewPager
    class ViewPagerAdapter extends FragmentPagerAdapter {
        //adapter data objects
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        //constructor
        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override//fragment getter
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override//size getter
        public int getCount() {
            return mFragmentList.size();
        }

        //method add fragment and its title to adapter
        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override//title getter
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
