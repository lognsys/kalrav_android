package com.lognsys.kalrav.fragment;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lognsys.kalrav.R;
import com.lognsys.kalrav.fragment.MyticketListFragment.OnListFragmentInteractionListener;
import com.lognsys.kalrav.fragment.dummy.DummyContent.DummyItem;
import com.lognsys.kalrav.model.TicketsInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<TicketsInfo> mValues;
    private final OnListFragmentInteractionListener mListener;
Context mconContext;
    public MyItemRecyclerViewAdapter(Context mconContext, ArrayList<TicketsInfo> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mconContext = mconContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ticketlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if(holder.mItem!=null){
            holder.textDramaName.setText(mValues.get(position).getDrama_name());
            holder.textGroupName.setText(mValues.get(position).getDrama_group_name());
            holder.textDramaDate.setText(mValues.get(position).getDrama_date());
            holder.textDramaTiming.setText(mValues.get(position).getDrama_time());
            holder.textBookingDate.setText(mValues.get(position).getBooked_date());
            holder.textBookingTime.setText(mValues.get(position).getBooked_time());
            holder.textAuditorium.setText(mValues.get(position).getAuditorium_name());
            holder.textUserName.setText(mValues.get(position).getUser_name());
            holder.textticketNumber.setText(mValues.get(position).getSeart_seat_no());
            holder.textTotalnoofticket.setText(mValues.get(position).getSeats_no_of_seats_booked());
            holder.textTotalprice.setText(mValues.get(position).getSeats_total_price());
           if(mValues.get(position).getDrama_photo()!=null){
                Picasso.with(mconContext).load(mValues.get(position).getDrama_photo()).into(holder.dramaImage);
            }
            else{
                Picasso.with(mconContext).load(String.valueOf(mconContext.getResources().getDrawable(R.drawable.stub,null))).into(holder.dramaImage);
            }
            Log.d("","Text mValues.get(position)..getBitmapQRCode() ===== "+mValues.get(position).getBitmapQRCode());

            if(mValues.get(position).getBitmapQRCode()!=null){
                holder.imageViewQRCode.setImageBitmap(mValues.get(position).getBitmapQRCode());
            }
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textDramaName;
        public final TextView textAuditorium;
        public final TextView textGroupName;
        public final TextView textDramaTiming;
        public final TextView textUserName;
        public final TextView textTotalnoofticket;
        public final TextView textTotalprice;
        public final TextView textticketNumber;
        public final TextView textDramaDate;
        public final TextView textBookingDate;
        public final TextView textBookingTime;
        public final ImageView imageViewQRCode;
        public final ImageView dramaImage;
        public TicketsInfo mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            textDramaName = (TextView) view.findViewById(R.id.textDramaName);
            textAuditorium = (TextView) view.findViewById(R.id.textAuditorium);
            textGroupName = (TextView) view.findViewById(R.id.textGroupName);
            textDramaTiming = (TextView) view.findViewById(R.id.textDramaTiming);
            textUserName = (TextView) view.findViewById(R.id.textUserName);
            textTotalnoofticket = (TextView) view.findViewById(R.id.textTotalnoofticket);
            textTotalprice = (TextView) view.findViewById(R.id.textTotalprice);
            textticketNumber = (TextView) view.findViewById(R.id.textticketNumber);
            textDramaDate = (TextView) view.findViewById(R.id.textDramaDate);
            textBookingDate = (TextView) view.findViewById(R.id.textBookingDate);
            textBookingTime = (TextView) view.findViewById(R.id.textBookingTime);
            imageViewQRCode = (ImageView) view.findViewById(R.id.imageViewQRCode);
            dramaImage = (ImageView) view.findViewById(R.id.dramaImage);
        }

        @Override
        public String toString() {
            return super.toString() ;
        }
    }
}
