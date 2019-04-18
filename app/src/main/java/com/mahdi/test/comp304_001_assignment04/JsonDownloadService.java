package com.mahdi.test.comp304_001_assignment04;
/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

//service to download a json file from www.cineplex.com
//this file is the source for Theatre locations
//if the source is not available anymore there is a copy of json file
//in project folder by name of *data.json*
public class JsonDownloadService extends Service {

    final static int NOT_STARTED = 0;
    final static int STARTED = 1;
    final static int COMPLETED = 2;
    final static int ERROR = 3;
    int progress = 0;
    Intent broadcastIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //create broadcast and set flag as not started
        broadcastIntent = new Intent();
        broadcastIntent.setAction("progress_action");
        broadcastIntent.putExtra("status", NOT_STARTED);
        broadcastIntent.putExtra("progress", progress);
        sendBroadcast(broadcastIntent);

        //create and run the Json Downloader
        new JsonTask().execute(
          "https://www.cineplex.com/api/v1/theatres?language=en-us&range=100000&skip=0&take=1000");

        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }//end of onStartCommand()

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    //this class will get the json data and convert it to list of Theatre object
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            //broadcast started flag
            broadcastIntent.putExtra("status", STARTED);
            sendBroadcast(broadcastIntent);
        }//end of onPreExecute()

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    //logging the downloaded data
                    Log.d("Response: ", "> " + line);
                    //increment progress and broadcast value
                    broadcastIntent.putExtra("progress", ++progress);
                    sendBroadcast(broadcastIntent);
                }
                return String.valueOf(builder);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }//end of try
            return null;
        }//end of doInBackground()

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //instantiate json objects
            JSONObject json;
            Gson gson = new Gson();
            Statics.THEATRES = new ArrayList<>();
            try { //Reading the data object from jSon that has the data
                if(result == null){
                    broadcastIntent.putExtra("status", ERROR);
                    sendBroadcast(broadcastIntent);
                    return;
                }
                json = new JSONObject(result);
                //get the array of Theatres from data
                JSONArray dataArray = json.getJSONArray("data");

                //fetch the array data one by one and add to Theatres object List
                for(int x = 0; x < dataArray.length(); x++){
                    JSONObject obj = (JSONObject) dataArray.get(x);
                    Theatre theatre = gson.fromJson(obj.toString(), Theatre.class);
                    Statics.THEATRES.add(theatre);
                }
                //send completed flag
                broadcastIntent.putExtra("status", COMPLETED);
                sendBroadcast(broadcastIntent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }//end of onPostExecute()
    }//end of JsonTask class
}//end of JsonDownloadService
