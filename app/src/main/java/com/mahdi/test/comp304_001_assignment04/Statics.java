package com.mahdi.test.comp304_001_assignment04;

/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class Statics {

    //list of theatres downloading from cineplex Api
    static List<Theatre> THEATRES;
    static List<SMSObject> MESSAGES = new ArrayList<>();

    //ticket prices
    static final double ADULT_PRICE = 13.99;
    static final double SENIOR_PRICE = 9.99;
    static final double CHILD_PRICE = 8.99;

    //booking status
    static final String CONFIRMED = "Confirmed";
    static final String CANCELLED = "Cancelled";
    static final String REFUNDED = "Refunded";
    static final String PENDING = "Pending";

    //method to remove errors from edit text
    static void removeError(EditText e, Resources res){
        e.setError(null);
        Resources.Theme theme = res.newTheme();
        e.setBackground(res.getDrawable(R.drawable.edit_text_background, null));
    }

    //method to set errors on edit text
    static void setError(EditText e, String text, Resources res){
        e.setError(text);
        e.setBackground(res.getDrawable(R.drawable.edit_txt_error, null));
    }

    //method to check email pattern
    static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //method to check value if its empty or not, if empty set error
    //otherwise remove error
    static boolean checkValue(EditText editText, Resources res){
        if(editText.getText().length()==0){
            setError(editText,"Cannot be Empty", res);
            return false;
        }else{
            removeError(editText, res);
            return true;
        }
    }

    //this method only check the value if its empty or not
    static boolean checkValue(EditText editText){
        return editText.getText().length() != 0;
    }
}
