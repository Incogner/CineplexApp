package com.mahdi.test.comp304_001_assignment04;
/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

//class for Database related methods and database connection object
public class DbManager {

    private SqlDbHelper myHelper;

    //constructor
    DbManager(Context context) {
        myHelper = new SqlDbHelper(context);
    }


    ////Update Methods
    //update an audience info
    int updateAudience(Audience audience) {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", audience.getUserName());
        contentValues.put("firstName", audience.getFName());
        contentValues.put("lastName", audience.getLName());
        contentValues.put("age", audience.getAge());
        contentValues.put("address", audience.getAddress());
        contentValues.put("city", audience.getCity());
        contentValues.put("postalCode", audience.getPostalCode());
        String[] whereArgs = {String.valueOf(audience.getId())};
        int updId = db.update(SqlDbHelper.TABLES[0], contentValues, "ID = ?", whereArgs);
        db.close();
        return updId;
    }

    //update password for audience
    int updatePassword(Audience audience) {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", audience.getPassword());
        String[] whereArgs = {String.valueOf(audience.getId())};
        int updId = db.update(SqlDbHelper.TABLES[0], contentValues, "ID = ?", whereArgs);
        db.close();
        return updId;
    }

    //update password for audience
    int updateBookingStatus(long bookingId, String newStatus) {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bookingStatus", newStatus);
        String[] whereArgs = {String.valueOf(bookingId)};
        int updId = db.update(SqlDbHelper.TABLES[3], contentValues, "ID = ?", whereArgs);
        db.close();
        return updId;
    }


    ////Insert Methods
    //this method will run once to insert movie information to database
    void insertMovies(){
        String[][] movieList = new String[][]
                {
                        {"Alita: Battle Angel (2019)",
                                "Robert Rodriguez", "Action, Adventure, Romance, Sci-Fi, Thriller",
                                "Rosa Salazar, Christoph Waltz, Jennifer Connelly, Mahershala Ali",
                                "A deactivated female cyborg is revived, but cannot remember anything of " +
                                        "her past life and goes on a quest to find out who she is.",
                                "7.6", "112", "m1"},
                        {"A Star Is Born (2018)","Bradley Cooper","Drama, Music, Romance","" +
                                "Lady Gaga, Bradley Cooper, Sam Elliott, Greg Grunberg",
                                "A musician helps a young singer find fame, even as age and " +
                                        "alcoholism send his own career into a downward spiral.",
                                "7.8", "136", "m2"},
                        {"Gully Boy (2019)", "Zoya Akhtar", "Drama, Music",
                                "Ranveer Singh, Alia Bhatt, Siddhant Chaturvedi, Vijay Raaz",
                                "A coming-of-age story based on the lives of street rappers in Mumbai.",
                                "8.5", "153", "m3"},
                        {"Apollo 11 (2019)", "Todd Douglas Miller", "Documentary",
                                "Buzz Aldrin, Neil Armstrong, Michael Collins",
                                "A look at the Apollo 11 mission to land on the moon led " +
                                        "by commander Neil Armstrong and pilot Buzz Aldrin.",
                                "8.8","93","m4"},
                        {"Glass (2019)", "M. Night Shyamalan", "Drama, Sci-Fi, Thriller",
                                "James McAvoy, Bruce Willis, Samuel L. Jackson, Anya Taylor-Joy",
                                "Security guard David Dunn uses his supernatural abilities to " +
                                        "track Kevin Wendell Crumb, a disturbed man who has twenty-four personalities.",
                                "7.0", "129","m5"},
                        {"Isn't It Romantic (2019)", "Todd Strauss-Schulson", "Comedy, Fantasy, Romance",
                                "Rebel Wilson, Liam Hemsworth, Adam Devine, Priyanka Chopra",
                                "A young woman disenchanted with love mysteriously finds herself " +
                                        "trapped inside a romantic comedy.", "6.1", "89","m6"}
                };
        SQLiteDatabase db = myHelper.getWritableDatabase();
        for(String[] x : movieList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("movieName", x[0]);
            contentValues.put("director", x[1]);
            contentValues.put("genre", x[2]);
            contentValues.put("stars", x[3]);
            contentValues.put("description", x[4]);
            contentValues.put("imdbRating", Float.valueOf(x[5]));
            contentValues.put("duration", Integer.valueOf(x[6]));
            contentValues.put("picId", x[7]);
            //Insert row and get id
            db.insert(SqlDbHelper.TABLES[2], null, contentValues);
        }
        db.close();
    }

    //this method will run once to insert admins to database
    void insertAdmins(){
        String[][] adminList = new String[][]
                {
                        {"admin", "pass" ,"Robert", "Rodriguez"},
                        {"admin01", "pass01","Bradley", "Cooper"},
                        {"admin02", "pass02", "Zoya", "Akhtar"}
                };
        SQLiteDatabase db = myHelper.getWritableDatabase();
        for(String[] x : adminList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("userName", x[0]);
            contentValues.put("password", x[1]);
            contentValues.put("firstName", x[2]);
            contentValues.put("lastName", x[3]);
            //Insert row and get id
            db.insert(SqlDbHelper.TABLES[1], null, contentValues);
        }
        db.close();
    }

    //Add new record in Audience Table
    long insertBooking(
            String emailId, long movieId, String paymentDate, double amountPaid, String showDate,
            int adultTicket, int childTicket, int seniorTicket,String showTime, String bookingStatus ) {
        SQLiteDatabase dbAudience = myHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("emailId", emailId);
        contentValues.put("movieId", movieId);
        contentValues.put("paymentDate", paymentDate);
        contentValues.put("amountPaid", amountPaid);
        contentValues.put("adultTicket", adultTicket);
        contentValues.put("childTicket", childTicket);
        contentValues.put("seniorTicket", seniorTicket);
        contentValues.put("showDate", showDate);
        contentValues.put("showTime", showTime);
        contentValues.put("bookingStatus", bookingStatus);
        //Insert row and get id
        long insID = dbAudience.insert(SqlDbHelper.TABLES[3], null, contentValues);
        dbAudience.close();
        return insID;
    }

   //Add new record in Audience Table
    long insertAudience(
            String emailId, String userName, String password, String firstName,
            String lastName, int age,long phoneNo, String address, String city, String postalCode ) {
        SQLiteDatabase dbAudience = myHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", userName);
        contentValues.put("password", password);
        contentValues.put("emailId", emailId);
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("age", age);
        contentValues.put("phoneNo", phoneNo);
        contentValues.put("address", address);
        contentValues.put("city", city);
        contentValues.put("postalCode", postalCode);
        //Insert row and get id
        long insID = dbAudience.insert(SqlDbHelper.TABLES[0], null, contentValues);
        dbAudience.close();
        return insID;
    }


    ////Get Methods
    //Get List of all Bookings
    List<Booking> getBookings(){
        SQLiteDatabase db = myHelper.getReadableDatabase();
        String[] columns = {
                "ID", "emailId", "movieId", "paymentDate","amountPaid",
                "adultTicket", "childTicket", "seniorTicket", "showDate",
                "showTime", "bookingStatus"
        };
        Cursor cursor = db.query(SqlDbHelper.TABLES[3], columns,
                null, null, null, null, null, null);
        assert cursor != null;
        List<Booking> aList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Booking aBooking = new Booking(
                    cursor.getLong(cursor.getColumnIndex("ID")),
                    cursor.getString(cursor.getColumnIndex("emailId")),
                    cursor.getLong(cursor.getColumnIndex("movieId")),
                    cursor.getString(cursor.getColumnIndex("paymentDate")),
                    cursor.getInt(cursor.getColumnIndex("adultTicket")),
                    cursor.getInt(cursor.getColumnIndex("childTicket")),
                    cursor.getInt(cursor.getColumnIndex("seniorTicket")),
                    cursor.getDouble(cursor.getColumnIndex("amountPaid")),
                    cursor.getString(cursor.getColumnIndex("showDate")),
                    cursor.getString(cursor.getColumnIndex("showTime")),
                    cursor.getString(cursor.getColumnIndex("bookingStatus"))
            );
            aList.add(aBooking);
        }
        cursor.close();
        db.close();
        return aList;
    }

    //Get List of User's Bookings
    List<Booking> getBookings(String emailId){
        SQLiteDatabase db = myHelper.getReadableDatabase();
        String[] columns = {
                "ID", "emailId", "movieId", "paymentDate","amountPaid",
                "adultTicket", "childTicket", "seniorTicket", "showDate",
                "showTime", "bookingStatus"
        };
        Cursor cursor = db.query(SqlDbHelper.TABLES[3], columns,
                "emailId=?", new String[]{emailId}, null, null, null, null);
        assert cursor != null;
        List<Booking> aList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Booking aBooking = new Booking(
                    cursor.getLong(cursor.getColumnIndex("ID")),
                    cursor.getString(cursor.getColumnIndex("emailId")),
                    cursor.getLong(cursor.getColumnIndex("movieId")),
                    cursor.getString(cursor.getColumnIndex("paymentDate")),
                    cursor.getInt(cursor.getColumnIndex("adultTicket")),
                    cursor.getInt(cursor.getColumnIndex("childTicket")),
                    cursor.getInt(cursor.getColumnIndex("seniorTicket")),
                    cursor.getDouble(cursor.getColumnIndex("amountPaid")),
                    cursor.getString(cursor.getColumnIndex("showDate")),
                    cursor.getString(cursor.getColumnIndex("showTime")),
                    cursor.getString(cursor.getColumnIndex("bookingStatus"))
            );
            aList.add(aBooking);
        }
        cursor.close();
        db.close();
        return aList;
    }

    //Get List of Movies
    List<Movie> getMovies(){
        SQLiteDatabase db = myHelper.getReadableDatabase();
        String[] columns = {
                "ID", "movieName", "director", "genre","stars",
                "description", "imdbRating", "duration", "picId"
        };
        Cursor cursor = db.query(SqlDbHelper.TABLES[2], columns,
                null, null, null, null, null, null);
        assert cursor != null;
        List<Movie> aList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Movie aMovie = new Movie();
            aMovie.setMovieId(cursor.getLong(cursor.getColumnIndex("ID")));
            aMovie.setMovieName(cursor.getString(cursor.getColumnIndex("movieName")));
            aMovie.setDirector(cursor.getString(cursor.getColumnIndex("director")));
            aMovie.setGenre(cursor.getString(cursor.getColumnIndex("genre")));
            aMovie.setStars(cursor.getString(cursor.getColumnIndex("stars")));
            aMovie.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            aMovie.setImdbRating(cursor.getFloat(cursor.getColumnIndex("imdbRating")));
            aMovie.setDuration(cursor.getInt(cursor.getColumnIndex("duration")));
            aMovie.setPicId(cursor.getString(cursor.getColumnIndex("picId")));

            aList.add(aMovie);
        }
        cursor.close();
        db.close();
        return aList;
    }

    //method to return the email exist in database by ID
    String getEmailById(long id) {

        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query(SqlDbHelper.TABLES[0], new String[] {"emailId"},
                "ID=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        assert cursor != null;
        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()) {
            String email = cursor.getString(cursor.getColumnIndex("emailId"));
            builder.append(email);
        }
        cursor.close();
        db.close();
        return builder.toString();
    }

    //method to return the email exist in database by email
    String getEmails(String inputEmail) {

        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query(SqlDbHelper.TABLES[0], new String[] {"emailId"},
                "emailId=?",
                new String[] { inputEmail }, null, null, null, null);
        assert cursor != null;
        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()) {
            String email = cursor.getString(cursor.getColumnIndex("emailId"));
            builder.append(email);
        }
        cursor.close();
        db.close();
        return builder.toString();
    }

    //method to get all admins from database
    List<Admin> getAdmins(){
        SQLiteDatabase db = myHelper.getReadableDatabase();
        String[] columns = {
                "ID", "userName", "password","firstName", "lastName"
        };
        Cursor cursor = db.query(SqlDbHelper.TABLES[1], columns,
                null, null, null, null, null, null);
        assert cursor != null;
        List<Admin> aList = new ArrayList<>();
        while (cursor.moveToNext()) {
            aList.add(new Admin(
                    cursor.getLong(cursor.getColumnIndex("ID")),
                    cursor.getString(cursor.getColumnIndex("userName")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("firstName")),
                    cursor.getString(cursor.getColumnIndex("lastName"))
            ));
        }
        cursor.close();
        db.close();
        if(aList.size()<1){
            return null;
        }else{
            return aList;
        }
    }

    //method to find a user by userName
    Audience getAudience(String emailId){
        SQLiteDatabase db = myHelper.getReadableDatabase();
        String[] columns = {
                "ID", "userName", "emailId", "password","firstName",
                "lastName", "age", "phoneNo", "address", "city", "postalCode"
        };
        Cursor cursor = db.query(SqlDbHelper.TABLES[0], columns,
                "emailId=?",
                new String[] { emailId }, null, null, null, null);
        assert cursor != null;
        List<Audience> aList = new ArrayList<>();
        while (cursor.moveToNext()) {
            aList.add(new Audience(
                cursor.getLong(cursor.getColumnIndex("ID")),
                cursor.getString(cursor.getColumnIndex("userName")),
                cursor.getString(cursor.getColumnIndex("emailId")),
                cursor.getString(cursor.getColumnIndex("password")),
                cursor.getString(cursor.getColumnIndex("firstName")),
                cursor.getString(cursor.getColumnIndex("lastName")),
                cursor.getInt(cursor.getColumnIndex("age")),
                    cursor.getLong(cursor.getColumnIndex("phoneNo")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("city")),
                    cursor.getString(cursor.getColumnIndex("postalCode"))
            ));
        }
        cursor.close();
        db.close();
        if(aList.size()<1){
            return null;
        }else{
            return aList.get(0);
        }
    }

    //method to find a user by ID
    Audience getAudience(long id){
        SQLiteDatabase db = myHelper.getReadableDatabase();
        String[] columns = {
                "ID", "userName", "emailId", "password","firstName",
                "lastName", "age","phoneNo", "address", "city", "postalCode"
        };
        Cursor cursor = db.query(SqlDbHelper.TABLES[0], columns,
                "ID=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        assert cursor != null;
        List<Audience> aList = new ArrayList<>();
        while (cursor.moveToNext()) {
            aList.add(new Audience(
                    cursor.getLong(cursor.getColumnIndex("ID")),
                    cursor.getString(cursor.getColumnIndex("userName")),
                    cursor.getString(cursor.getColumnIndex("emailId")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("firstName")),
                    cursor.getString(cursor.getColumnIndex("lastName")),
                    cursor.getInt(cursor.getColumnIndex("age")),
                    cursor.getLong(cursor.getColumnIndex("phoneNo")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("city")),
                    cursor.getString(cursor.getColumnIndex("postalCode"))
            ));
        }
        cursor.close();
        db.close();
        if(aList.size()<1){
            return null;
        }else{
            return aList.get(0);
        }
    }

    List<Audience> getAudiences() {
        SQLiteDatabase db = myHelper.getReadableDatabase();
        String[] columns = {
                "ID", "userName", "emailId", "password","firstName",
                "lastName", "age","phoneNo", "address", "city", "postalCode"
        };
        Cursor cursor = db.query(SqlDbHelper.TABLES[0], columns, null, null, null, null, null);
        assert cursor != null;
        List<Audience> audiences = new ArrayList<>();
        while (cursor.moveToNext()) {
            audiences.add(new Audience(
                    cursor.getLong(cursor.getColumnIndex("ID")),
                    cursor.getString(cursor.getColumnIndex("userName")),
                    cursor.getString(cursor.getColumnIndex("emailId")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("firstName")),
                    cursor.getString(cursor.getColumnIndex("lastName")),
                    cursor.getInt(cursor.getColumnIndex("age")),
                    cursor.getLong(cursor.getColumnIndex("phoneNo")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("city")),
                    cursor.getString(cursor.getColumnIndex("postalCode"))
            ));
        }
        cursor.close();
        db.close();
        return audiences;
    }


    ////Delete Methods
    //delete a booking
    int deleteBooking(long bookingId) {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        String[] whereArgs = {String.valueOf(bookingId)};
        int delId = db.delete(SqlDbHelper.TABLES[3],  "ID = ?", whereArgs);
        db.close();
        return delId;
    }

    //delete a Audience user
    int deleteAudience(long id) {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        String[] whereArgs = {String.valueOf(id)};
        int delId = db.delete(SqlDbHelper.TABLES[0],  "ID = ?", whereArgs);
        db.close();
        return delId;
    }

    ////Database Class
    //static class for SQLite open Helper for database connections
    static class SqlDbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "cineplex.db";    // Database Name
        private static final int DATABASE_Version = 1;    // Database Version
        //table names
        private static final String[] TABLES = {"Audience","Admin","Movies","Booking"};
        //table creation statements
        private static final String[] CREATE_TABLES =
                {
                        "CREATE TABLE Audience ( "+
                                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "emailId VARCHAR(16) ," +
                                "userName VARCHAR(36),"+
                                "password VARCHAR(16),"+
                                "firstName VARCHAR(36),"+
                                "lastName VARCHAR(36),"+
                                "age NUMBER(3),"+
                                "phoneNo NUMBER(10),"+
                                "address VARCHAR(64),"+
                                "city VARCHAR(36),"+
                                "postalCode VARCHAR(8)"+
                                ")",
                        "CREATE TABLE Admin ( "+
                                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "userName VARCHAR(36),"+
                                "password VARCHAR(16),"+
                                "firstName VARCHAR(36),"+
                                "lastName VARCHAR(36)"+
                                ")",
                        "CREATE TABLE Movies ( "+
                                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "movieName VARCHAR(64),"+
                                "director VARCHAR(64),"+
                                "genre VARCHAR(128),"+
                                "description VARCHAR(256),"+
                                "stars VARCHAR(128),"+
                                "imdbRating Number(2,1),"+
                                "duration Number(3),"+
                                "picId VARCHAR(32)"+
                                ")",
                        "CREATE TABLE Booking ( "+
                                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "emailId VARCHAR(16) ," +
                                "movieId VARCHAR(16) ," +
                                "paymentDate VARCHAR(12),"+
                                "amountPaid number(14,4),"+
                                "adultTicket number(2),"+
                                "childTicket number(2),"+
                                "seniorTicket number(2),"+
                                "showDate VARCHAR(12),"+
                                "showTime VARCHAR(10),"+
                                "bookingStatus VARCHAR(10)"+
                                ")",
                };
        //private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        SqlDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                for (String CREATE_TABLE : CREATE_TABLES) db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Toast.makeText(context, "OnUpgrade", Toast.LENGTH_SHORT).show();
                for (String TABLE : TABLES) db.execSQL("DROP TABLE IF EXISTS " + TABLE);
                onCreate(db);
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}