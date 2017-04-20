package com.lognsys.kalrav.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lognsys.kalrav.HomeActivity;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.fragment.ConfirmFragment;
import com.lognsys.kalrav.fragment.MyTicketFragment;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.TimeSlot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 4/14/2017.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
     private List<String> mSeats = new ArrayList<String>();
    private String[] mData = new String[0];
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private TextView confirm;
    int totalPrice=0;
    Activity mcontext;
    public int price;
    public int count;
    TimeSlot timeSlot;
    DramaInfo dramaInfo;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Activity context, String[] data, int price, TextView confirm, TimeSlot timeSlot, DramaInfo dramaInfo) {
        this.mcontext=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.price = price;
        this.confirm=confirm;
        this.timeSlot=timeSlot;
        this.dramaInfo=dramaInfo;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycle_seats_info, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.position=position;
        String seatnumber =  mData[position];
        if(position==0 || position==11 || position==22 || position==33 || position==44 || position==55 || position==66
                || position==77 ){
            holder.myTextView.setBackgroundColor(Color.WHITE);
            holder.myTextView.setTextColor(Color.BLACK);
            holder.myTextView.setText(seatnumber);
            holder.myTextView.setClickable(false);
            holder.myTextView.setTextSize(10.5f);
            holder.myTextView.setPadding(2,2,2,2);
            holder.linearLayout.setBackgroundColor(Color.WHITE);
        }
        else {
            holder.myTextView.setText(seatnumber);
        }
        this.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSeats.size()>0){
                  ((Activity) mcontext).getFragmentManager();//use this
                Toast.makeText(v.getContext(), "You selected position = "
                        +" totalPrice = "+totalPrice+" mSeats.size() ="+mSeats.size(), Toast.LENGTH_SHORT).show();
                  Fragment fff=new ConfirmFragment();
                     Bundle args = new Bundle();
                    args.putInt("totalPrice", totalPrice);
                    args.putSerializable("timeSlot",timeSlot);
                    args.putSerializable("dramaInfo",dramaInfo);
                    args.putStringArrayList("seats", (ArrayList<String>) mSeats);
                    fff.setArguments(args);
                    if (fff != null)
                        switchFragment(fff);
                }
                else{
                    Toast.makeText(v.getContext(), "Please select your seat no. ", Toast.LENGTH_SHORT).show();

                }
            }

            private void switchFragment(Fragment fff) {
                if (mcontext == null)
                    return;
                if (mcontext instanceof HomeActivity) {
                    HomeActivity feeds = (HomeActivity) mcontext;
                    feeds.switchContent(fff);
                }
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView myTextView,textseatLetter;
        LinearLayout linearLayout;
        public int position;
        public ViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.info_text);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            myTextView.setOnClickListener(new View.OnClickListener() {
                private boolean stateChanged;

                @Override
                public void onClick(View v) {

                    if(stateChanged) {
                        count--;
                        if(count!=0) {
                         totalPrice = totalPrice - price ;

                        }
                        if(position==0 || position==11 || position==22 || position==33 || position==44 || position==55 || position==66
                                || position==77 ){
                            myTextView.setBackgroundColor(Color.WHITE);
                            myTextView.setTextColor(Color.BLACK);

                            myTextView.setClickable(false);
                            myTextView.setTextSize(10.5f);
                            myTextView.setPadding(2,2,2,2);
                            linearLayout.setBackgroundColor(Color.WHITE);
                        }
                        else{
                            myTextView.setBackgroundColor(Color.parseColor("#c8e8ff"));
                            myTextView.setTextColor(Color.BLACK);

                        }
                        mSeats.remove(myTextView.getText().toString());
                        Log.d("","Rest mSeats onremove size   "+mSeats.size());
                      Toast.makeText(v.getContext(), "You unselected seat no. = " +myTextView.getText().toString()+" totalPrice = "+totalPrice, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        count++;
                        Log.d("","Rest count "+count);
                        myTextView.setBackgroundColor(Color.GRAY);
                        myTextView.setTextColor(Color.WHITE);
                         totalPrice=count* price;
                        Toast.makeText(v.getContext(), "You selected seat no. = " +myTextView.getText().toString()+" totalPrice = "+totalPrice, Toast.LENGTH_SHORT).show();
                        mSeats.add(myTextView.getText().toString());
                    }
                    stateChanged = !stateChanged;

                }
            });
            if(mSeats.size()>0)
            Log.d("","Rest mSeats onremove size   "+mSeats.size());

        }

        @Override
        public void onClick(View view) {
//            Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();

            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData[id];
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
