package com.mahdi.test.comp304_001_assignment04;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Theatre implements Serializable {

    int id;
    String name;
    String address1;
    String address2;
    String city;
    String provinceCode;
    String nearestIntersection;
    float latitude;
    float longitude;
    double distance;
    String urlSlug;
    List<Experience> experiences;
    boolean isFavourite;
    boolean isTicketingAvailable;
    List<TheatreMessage> theatreMessages;
    boolean isDriveIn;
    String mapImageUrl;
    String mobileMapImageUrl;
    String mobileBackgroundImageUrl;
    String calorieChartUrl;

    public Theatre(){
        experiences = new ArrayList<>();
        theatreMessages = new ArrayList<>();
    }




    class Experience implements Serializable{
                String experienceId;
                String title;
                String imageName;
                String description;
    }

    private class TheatreMessage implements Serializable {
        int id;
        String body;
        String category;
    }
}

