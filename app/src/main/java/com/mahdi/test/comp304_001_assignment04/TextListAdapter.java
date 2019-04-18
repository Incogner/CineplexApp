package com.mahdi.test.comp304_001_assignment04;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TextListAdapter extends ArrayAdapter<SMSObject> {

    private final Activity context;
    private final List<SMSObject> smses;

    //View Holder static class for layout reuse purpose
    class ViewHolder {
        RelativeLayout layout;
        LinearLayout child;
        TextView id;
        TextView text;
        //TextView status;
        TextView date;
    }

    //constructor of list adapter
    TextListAdapter(Activity context, List<SMSObject> SmsList) {
        super(context, R.layout.text_row_layout, SmsList);
        this.context = context;
        this.smses = SmsList;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {

        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            if(smses.get(pos).type == 2){
                rowView = inflater.inflate(R.layout.text_row_layout_2, null);
            }else{
                rowView = inflater.inflate(R.layout.text_row_layout, null);
            }

            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            //viewHolder.id = rowView.findViewById(R.id.txtId);
            viewHolder.layout = rowView.findViewById(R.id.textLayout);
            viewHolder.child = rowView.findViewById(R.id.contentLayout);
            viewHolder.text = rowView.findViewById(R.id.txtBody);
            viewHolder.date = rowView.findViewById(R.id.txtDate);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();

/* no need for assign attributes programmatically
        //change position of item based on type
        RelativeLayout.LayoutParams lp = new
                RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, metrics );
        lp.bottomMargin = margin;
        lp.topMargin = margin;
        lp.rightMargin = margin;
        lp.leftMargin = margin;
        if(smses.get(pos).type == 2){
            lp.addRule(RelativeLayout.ALIGN_PARENT_END);
        }else{
            lp.addRule(RelativeLayout.ALIGN_PARENT_START);
        }
        holder.layout.removeView(holder.child);
        holder.layout.addView(holder.child, lp);
        */

        //set values for views
        holder.text.setText(smses.get(pos).smsBody);
        Date date = new Date(smses.get(pos).date);
        String formattedDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.CANADA).format(date);
        holder.date.setText(formattedDate);

        return rowView;
    }
}