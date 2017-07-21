package com.lognsys.kalrav.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lognsys.kalrav.R;
import com.lognsys.kalrav.model.TimeSlot;

import java.util.ArrayList;

/**
 * Created by admin on 4/14/2017.
 */

public class CustomGridArrayAdapter extends BaseAdapter{
    private Context mContext;
    ArrayList<TimeSlot> mtimeSlots;
    private static LayoutInflater inflater=null;
    // Constructor
    public CustomGridArrayAdapter(Context c, ArrayList<TimeSlot> timeSlots) {
        mContext = c;
        this.mtimeSlots=timeSlots;
        inflater = ( LayoutInflater )mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return this.mtimeSlots.size();
    }

    public TimeSlot getItem(int position) {
        return this.mtimeSlots.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView=convertView;
        final TimeSlot timeSlot=this.mtimeSlots.get(position);
        if(convertView==null)
            convertView = inflater.inflate(R.layout.dialog_timealot_details, null,false);
        holder.textDate=(TextView) convertView.findViewById(R.id.textDate);
        holder.textTime=(TextView) convertView.findViewById(R.id.textTime);

        holder.textDate.setText(timeSlot.getDateSlot());
        holder.textTime.setText(timeSlot.getTimeSlot());

/*        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(mContext, "You Clicked "+timeSlot.getDateSlot()+" "+timeSlot.getTimeSlot(), Toast.LENGTH_LONG).show();

            }
        });*/
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
    public class Holder
    {
        TextView textDate;
        TextView textTime;
    }
}