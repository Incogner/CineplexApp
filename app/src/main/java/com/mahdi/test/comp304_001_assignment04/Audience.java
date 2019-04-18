package com.mahdi.test.comp304_001_assignment04;
/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */

import java.io.Serializable;

class Audience implements Serializable {
    private long id;
    private String userName;
    private String email;
    private String password;
    private String fName;
    private String lName;
    private int age;
    private long phoneNo;
    private String address;
    private String city;
    private String postalCode;

    //Saving audience in database while creation
    Audience(
            DbManager dbManager, String userName, String email, String password, String fName,
            String lName, int age,long phoneNo, String address, String city, String postalCode){
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.fName = fName;
        this.lName = lName;
        this.age = age;
        this.phoneNo = phoneNo;
        this.address =address;
        this.city=city;
        this.postalCode=postalCode;
        this.id = dbManager.insertAudience(
                email, userName,
                password, fName,
                lName, age, phoneNo,
                address, city,
                postalCode
        );
    }
    //create audience from data reading from database
    Audience(
            long id, String userName, String email, String password, String fName,
            String lName, int age,long phoneNo, String address, String city, String postalCode){
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.fName = fName;
        this.lName = lName;
        this.age = age;
        this.phoneNo = phoneNo;
        this.address =address;
        this.city=city;
        this.postalCode=postalCode;
        this.id = id;
    }

    //update method for audience
    boolean update(DbManager dbManager){
        int id = dbManager.updateAudience(this);
        return id > 0;
    }

    //update method to password change
    boolean updatePassword(DbManager dbManager){
        int id = dbManager.updatePassword(this);
        return id > 0;
    }

    //get methods
    long getId(){return this.id;}
    String getUserName(){
        return this.userName;
    }
    String getPassword(){
        return this.password;
    }
    String getEmail(){
        return this.email;
    }
    String getFName(){
        return this.fName;
    }
    String getLName(){
        return this.lName;
    }
    int getAge(){
        return this.age;
    }
    long getPhoneNo(){return this.phoneNo;}
    String getAddress(){
        return this.address;
    }
    String getCity(){
        return this.city;
    }
    String getPostalCode(){
        return this.postalCode;
    }

    //set methods
    void setUserName(String value){
        this.userName = value;
    }
    void setPassword(String value){
        this.password = value;
    }
    void setfName(String value){
        this.fName = value;
    }
    void setlName(String value){
        this.lName = value;
    }
    void setAge(int value){
        this.age = value;
    }
    void setPhoneNo(long value){this.phoneNo = value;}
    void setAddress(String value){
        this.address = value;
    }
    void setCity(String value){
        this.city = value;
    }
    void setPostalCode(String value){
        this.postalCode = value;
    }

}
