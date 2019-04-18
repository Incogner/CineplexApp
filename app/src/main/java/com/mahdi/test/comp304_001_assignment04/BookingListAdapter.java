package com.mahdi.test.comp304_001_assignment04;
/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BookingListAdapter extends ArrayAdapter<Booking> {

    private final Activity context;
    private final List<Booking> bookings;
    private final List<Movie> movies;

    //View Holder static class for layout reuse purpose
    static class ViewHolder {
        TextView title;
        TextView showTime;
        TextView status;
        TextView payment;
        ImageView icon;
    }

    //constructor of list adapter
    BookingListAdapter(Activity context, List<Booking> bookings, List<Movie> movies) {
        super(context, R.layout.booking_row_layout, bookings);
        this.context = context;
        this.bookings = bookings;
        this.movies = movies;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {

        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.booking_row_layout, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = rowView.findViewById(R.id.bRowTitle);
            viewHolder.showTime = rowView.findViewById(R.id.bRowDateTime);
            viewHolder.status = rowView.findViewById(R.id.bRowStatus);
            viewHolder.payment = rowView.findViewById(R.id.bRowPrice);
            viewHolder.icon = rowView.findViewById(R.id.bookingIcon);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        //get index for movie
        int movieIndex = (int)bookings.get(pos).getMovieId()-1;
        //find resource ID by using the name of picture
        int resID = context.getResources().getIdentifier(
                movies.get(movieIndex).getPicId(), "drawable", context.getPackageName());
        String movieTitle = movies.get(movieIndex).getMovieName();
        holder.title.setText(movieTitle);
        holder.showTime.setText(
                String.format("ShowTime: %s, %s",bookings.get(pos).getShowDate(),
                        bookings.get(pos).getShowTime()));
        //status coloring
        String status = bookings.get(pos).getBookingStatus();
        switch (status){
            case Statics.CONFIRMED:
                holder.status.setTextColor(Color.parseColor("#5db583"));
                break;
            case Statics.CANCELLED:
            case Statics.REFUNDED:
                holder.status.setTextColor(Color.RED);
                break;
            case Statics.PENDING:
                holder.status.setTextColor(Color.GRAY);
                break;
        }
        holder.status.setText(status);
        String amount = NumberFormat.getCurrencyInstance(new Locale("en", "CA"))
                .format(bookings.get(pos).getAmountPaid());
        holder.payment.setText(String.format("Amount Paid: %s", amount));
        holder.icon.setImageResource(resID);

        return rowView;
    }
}
