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

public class AudienceListAdapter extends ArrayAdapter<Audience> {

    private final Activity context;
    private final List<Audience> audiences;

    //View Holder static class for layout reuse purpose
    static class ViewHolder {
        TextView userName;
        TextView email;
        TextView id;
    }

    //constructor of list adapter
    AudienceListAdapter(Activity context, List<Audience> audiences) {
        super(context, R.layout.audience_row_layout, audiences);
        this.context = context;
        this.audiences = audiences;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {

        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.audience_row_layout, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.id = rowView.findViewById(R.id.txtAudienceId);
            viewHolder.userName = rowView.findViewById(R.id.txtAudienceUserName);
            viewHolder.email = rowView.findViewById(R.id.txtAudienceEmail);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.id.setText(String.valueOf(audiences.get(pos).getId()));
        holder.userName.setText(
                String.format("UserName: %s",audiences.get(pos).getUserName()));
        holder.email.setText(
                String.format("Email: %s",audiences.get(pos).getEmail()));

        return rowView;
    }
}
