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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.Objects;

public class MovieFragment extends Fragment {
    //
    // the fragment initialization parameters
    private static final String ARG_PARAM1 = "movie";

    //
    private Movie movie;
    Boolean isAdmin;

    public MovieFragment() {
        // Required empty public constructor
    }

    public static MovieFragment newInstance(Movie movie) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable(ARG_PARAM1);
        }
        //shared username
        SharedPreferences sharedUserName =
                Objects.requireNonNull(getActivity()).getSharedPreferences("UserName", Context.MODE_PRIVATE);
        isAdmin = sharedUserName.getBoolean("isAdmin", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View finalView = inflater.inflate(R.layout.fragment_movie, container, false);

        //set movie information for views
        //
        //get resource name and find resource id by its name used to find pictures
        //String iconName = getResources().getResourceEntryName(R.drawable.m1);
        int resID = getResources().getIdentifier(
                movie.getPicId(), "drawable", getActivity().getPackageName());

        ImageView pic = finalView.findViewById(R.id.fragMovieImage);
        pic.setImageResource(resID);
        TextView title = finalView.findViewById(R.id.fragMovieTitle);
        title.setText(movie.getMovieName());
        TextView imdb = finalView.findViewById(R.id.fragMovieIMDB);
        imdb.setText(String.format(Locale.CANADA,"IMDB: %.1f/10",movie.getImdbRating()));
        TextView duration = finalView.findViewById(R.id.fragMovieDuration);
        duration.setText(String.format(Locale.CANADA,"%d mins",movie.getDuration()));
        TextView genre = finalView.findViewById(R.id.fragMovieGenre);
        genre.setText(movie.getGenre());
        TextView director = finalView.findViewById(R.id.fragMovieDirector);
        director.setText(movie.getDirector());
        TextView starts = finalView.findViewById(R.id.fragMovieStars);
        starts.setText(movie.getStars());
        TextView description = finalView.findViewById(R.id.fragMovieDesc);
        description.setText(movie.getDescription());
        Button book = finalView.findViewById(R.id.btnFragMovieBook);
        //show button if user is not admin
        if(isAdmin){
            book.setVisibility(View.INVISIBLE);
        }else{
            //Handle book show time button
            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AreaActivity.class);
                    intent.putExtra("movie", movie);
                    startActivity(intent);
                }
            });
        }
        return finalView;
    }
}