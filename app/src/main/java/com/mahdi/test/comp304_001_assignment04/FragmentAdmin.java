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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.Objects;

public class FragmentAdmin extends Fragment{

    //shared preference object
    SharedPreferences sharedUserName;
    List<Admin> admins;
    DbManager dbMan;

    //layout views
    EditText userNameIn, userPassIn;
    Button btnUserLogin;

    public FragmentAdmin() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_fragment_admin, container, false);

        //create shared preference file
        sharedUserName = Objects.requireNonNull(getActivity())
                .getSharedPreferences("UserName", Context.MODE_PRIVATE);

        //get edit text object to get the admin user inputs
        userNameIn = view.findViewById(R.id.adminUserName);
        userPassIn = view.findViewById(R.id.adminPassword);

        //Handle Login button click event
        btnUserLogin = view.findViewById(R.id.btnLoginAdmin);
        btnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameId;
                String password;

                //checking for validations
                if(Statics.checkValue(userNameIn, getResources())){
                    userNameId = userNameIn.getText().toString().toLowerCase();
                    password = userPassIn.getText().toString();
                    //get database object
                    dbMan = new DbManager(getActivity());
                    admins = dbMan.getAdmins();
                    if(admins == null){
                        dbMan.insertAdmins();
                        admins = dbMan.getAdmins();
                    }
                    Admin admin = null;
                    //check to find admin username in list of admins fetched from database
                    for (Admin x : admins){
                        if(x.getUserName().equals(userNameId))
                            admin = x;
                    }
                    if(admin == null ){
                        Statics.setError(userNameIn, "User does not exist!", getResources());
                    }else if(!Statics.checkValue(userPassIn, getResources())){
                        //Todo: condition will check if edit box is empty or not
                    }else if(!admin.getPassword().equals(password)){
                        //if password is wrong set error
                        Statics.setError(userPassIn, "Password is wrong", getResources());
                    }else{
                        //when there is no error remove errors and proceed
                        Statics.removeError(userPassIn, getResources());
                        Statics.removeError(userNameIn, getResources());
                        //edit the shared preference file and pu username in it
                        SharedPreferences.Editor editor = sharedUserName.edit();
                        editor.putString("userName", admin.getUserName());
                        editor.putString("cappedUserName",
                                admin.getUserName().substring(0,1).toUpperCase() +
                                        admin.getUserName().substring(1));
                        editor.putLong("userID", admin.getId());
                        editor.putBoolean("isAdmin", true);
                        editor.apply();
                        //open Movies activity
                        Intent intent = new Intent(getActivity(), AdminViewActivity.class);
                        intent.putExtra("admin", admin);
                        startActivity(intent);
                        Objects.requireNonNull(getActivity()).finish();
                    }
                }
            }
        });//end of user button login click listener
        return view;
    }//end of OnCreateView method

}

