package com.mahdi.test.comp304_001_assignment04;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;

public class TheatreInfoActivity extends AppCompatActivity implements View.OnClickListener {

    TextView title, distance, address, majorIntersection;
    Button btnBookShowTime, btnBack;
    ImageView theatreBG;

    Theatre theatre;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theatre_info);

        //set Toolbar properties
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTheatreInfo);
        toolbar.setTitle("Theatre Information");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.theatreTitle);
        distance = findViewById(R.id.distance);
        address = findViewById(R.id.address);
        majorIntersection = findViewById(R.id.intersection);
        theatreBG = findViewById(R.id.imageView);
        btnBookShowTime = findViewById(R.id.btnBookShowTime);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnBookShowTime.setOnClickListener(this);

        theatre = (Theatre) Objects.requireNonNull(getIntent().getExtras()).getSerializable("theatreInfo");


        if(theatre != null){
            new ImageDownloader().execute(theatre.mobileBackgroundImageUrl);
            title.setText(theatre.name);
            distance.setText(String.format(Locale.CANADA,"Distance: %dKM",Math.round((theatre.distance*100.0)/100.0)));
            majorIntersection.setText(String.format("Intersection: %s",theatre.nearestIntersection));
            address.setText(String.format("Address: %s\n%s",theatre.address1,theatre.address2));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                this.onBackPressed();
                break;
            case R.id.btnBookShowTime:
                movie = (Movie) getIntent().getSerializableExtra("movie");
                Intent intent = new Intent(this, BookingInfoActivity.class);
                intent.putExtra("movie", movie);
                intent.putExtra("theatre", theatre);
                startActivityForResult(intent, 1);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                this.finishActivity(1);
                onBackPressed();
            }
        }
    }


    //this class will download images for theatres background
    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null){
                theatreBG.setImageBitmap(bitmap);
            }else {
                theatreBG.setImageResource(R.drawable.no_image_available);
            }

        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(strings[0]);
                urlConnection = (HttpURLConnection) uri.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {

                    return BitmapFactory.decodeStream(inputStream);
                }
            } catch (Exception e) {

                Log.d("URL CONNECTION ERROR", e.toString());
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                Log.w("ImageDownloader", "Error downloading image from " + strings[0]);
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
    //back button on action bar doing same as back button press on phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.onBackPressed();
        return true;
    }

}
