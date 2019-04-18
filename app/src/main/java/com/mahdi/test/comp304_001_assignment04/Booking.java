package com.mahdi.test.comp304_001_assignment04;
/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */

import java.io.Serializable;

public class Booking implements Serializable {

    private long bookingId;
    private String emailId;
    private long movieId;
    private String paymentDate;
    private int adultTicket;
    private int childTicket;
    private int seniorTicket;
    private double amountPaid;
    private String showDate;
    private String showTime;
    private String bookingStatus;


    //Constructors
    //for save object to database status confirmed
    Booking(
            DbManager dbManager, String emailId, long movieId, String paymentDate,
            int adultTicket, int childTicket, int seniorTicket, double amountPaid, String showDate, String showTime) {
        this.emailId = emailId;
        this.movieId = movieId;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
        this.adultTicket = adultTicket;
        this.childTicket = childTicket;
        this.seniorTicket = seniorTicket;
        this.showDate = showDate;
        this.showTime = showTime;
        this.bookingStatus = Statics.CONFIRMED;
        this.bookingId = dbManager.insertBooking(
                this.emailId, this.movieId, this.paymentDate, this.amountPaid, this.showDate,
                this.adultTicket, this.childTicket, this.seniorTicket , this.showTime, this.bookingStatus
        );
    }

    //for loading data from database
    Booking(
            long id, String emailId, long movieId, String paymentDate,
            int adultTicket, int childTicket, int seniorTicket, double amountPaid,
            String showDate, String showTime, String bookingStatus) {
        this.emailId = emailId;
        this.movieId = movieId;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
        this.adultTicket = adultTicket;
        this.childTicket = childTicket;
        this.seniorTicket = seniorTicket;
        this.showDate = showDate;
        this.showTime = showTime;
        this.bookingStatus = bookingStatus;
        this.bookingId = id;
    }

    //for saving temporary object status pending
    Booking(long movieId, String showDate,
                   String showTime, int adultTicket, int childTicket, int seniorTicket){
        this.movieId = movieId;
        this.showDate = showDate;
        this.showTime = showTime;
        this.adultTicket = adultTicket;
        this.childTicket = childTicket;
        this.seniorTicket = seniorTicket;
        this.bookingStatus = Statics.PENDING;
    }

    //getters and setters
    public long getBookingId() {
        return bookingId;
    }

    public String getEmailId() {
        return emailId;
    }

    long getMovieId() {
        return movieId;
    }

    String getPaymentDate() {
        return paymentDate;
    }

    double getAmountPaid() {
        return amountPaid;
    }

    String getShowDate() {
        return showDate;
    }

    String getShowTime() {
        return showTime;
    }

    String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    int getAdultTicket() {
        return adultTicket;
    }

    int getChildTicket() {
        return childTicket;
    }

    int getSeniorTicket() {
        return seniorTicket;
    }
}
