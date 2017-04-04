package com.lognsys.kalrav.adapter;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lognsys.kalrav.R;

import java.util.List;

/**
 * Created by admin on 03-04-2017.
 */

public class HorizontalAdapterUsersReview extends RecyclerView.Adapter<HorizontalAdapterUsersReview.MyViewHolder> {


    private List<String> horizontalListUser;
    private List<String> horizontalListReview;
    static int positionC = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvReview, tvUser;
        CardView cardView;


        public MyViewHolder(View view) {
            super(view);
            tvReview = (TextView) view.findViewById(R.id.tvReview);
            tvUser = (TextView) view.findViewById(R.id.tvUserName);
            //cardView = (CardView) view.findViewById(R.id.card_view);

        }
    }


    public HorizontalAdapterUsersReview(List<String> horizontalListUser, List<String> horizontalListReview) {
        this.horizontalListUser = horizontalListUser;
        this.horizontalListReview = horizontalListReview;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_review_recycler, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvReview.setText(horizontalListReview.get(position));
        holder.tvUser.setText(horizontalListUser.get(position));
        positionC = position;
        //holder.cardView.setTag(position);
        holder.tvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),holder.tvReview.getText().toString(), Toast.LENGTH_SHORT)).show();
            }
        });

    }

    @Override
    public long getItemId(int position) {


        return position;
    }

    @Override
    public int getItemCount() {
        return horizontalListUser.size();
    }


    public int position() {

        return positionC;
//display toast with position of cardview in recyclerview list upon click
        // Toast.makeText(view.getContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
    }
}






