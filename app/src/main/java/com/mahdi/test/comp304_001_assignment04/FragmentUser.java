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
import android.widget.Toast;

import java.util.Objects;

public class FragmentUser extends Fragment{

    //shared preference object
    SharedPreferences sharedUserName;

    //layout views
    EditText userEmailIn, userPassIn;
    Button btnRegister, btnUserLogin;

    public FragmentUser() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_fragment_user, container, false);

        //create shared preference file
        sharedUserName = Objects.requireNonNull(getActivity())
                .getSharedPreferences("UserName", Context.MODE_PRIVATE);

        //Handle Register button click event
        btnRegister = view.findViewById(R.id.btnRegisterUser);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        //get edit text object to get the user inputs
        userEmailIn = view.findViewById(R.id.userUserName);
        userPassIn = view.findViewById(R.id.userPassword);

        //Handle Login button click event
        btnUserLogin = view.findViewById(R.id.btnLoginUser);
        btnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId;
                String password;

                //checking for validations
                if(Statics.checkValue(userEmailIn, getResources())){
                    emailId = userEmailIn.getText().toString().toLowerCase();
                    password = userPassIn.getText().toString();
                    DbManager dbMan = new DbManager(getActivity());
                    Audience user = dbMan.getAudience(emailId);
                    if(!Statics.isEmailValid(emailId)){
                        Statics.setError(userEmailIn, "Email format wrong!", getResources());
                    }else if(user == null ){
                        Statics.setError(userEmailIn, "User does not exist!", getResources());
                    }else if(!Statics.checkValue(userPassIn, getResources())){
                        //Todo: empty
                    }else if(!user.getPassword().equals(password)){
                        Statics.setError(userPassIn, "Password is wrong", getResources());
                    }else{
                        //when there is no error remove errors and proceed
                        Statics.removeError(userPassIn, getResources());
                        Statics.removeError(userEmailIn, getResources());
                        //edit the shared preference file and pu username in it
                        SharedPreferences.Editor editor = sharedUserName.edit();
                        editor.putString("userName", user.getUserName());
                        editor.putString("emailId", user.getEmail());
                        editor.putString("cappedUserName",
                                user.getUserName().substring(0,1).toUpperCase() +
                                user.getUserName().substring(1));
                        editor.putLong("userID", user.getId());
                        editor.putBoolean("isAdmin", false);
                        editor.apply();
                        //open Movies activity
                        Intent intent = new Intent(getActivity(), MoviesActivity.class);
                        startActivity(intent);
                        Objects.requireNonNull(getActivity()).finish();
                    }
                }
            }
        });//end of user button login click listener
        return view;
    }//end of OnCreateView method

}
