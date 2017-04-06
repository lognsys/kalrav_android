package com.lognsys.kalrav.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lognsys.kalrav.R;

import java.util.List;

/**
 * Created by admin on 06-04-2017.
 */

public class VerticalAdapterReview extends RecyclerView.Adapter<VerticalAdapterReview.MyViewHolder> {



    private List<String> verticalList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvReview;

        public MyViewHolder(View view) {
            super(view);
            tvReview = (TextView) view.findViewById(R.id.tvReview);

        }
    }


    public VerticalAdapterReview(List<String> verticalList) {
        this.verticalList = verticalList;
    }

    @Override
    public VerticalAdapterReview.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.critics_review_recycler, parent, false);

        return new VerticalAdapterReview.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VerticalAdapterReview.MyViewHolder holder, final int position) {
        holder.tvReview.setText(verticalList.get(position));

        holder.tvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),holder.tvReview.getText().toString(), Toast.LENGTH_SHORT)).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return verticalList.size();
    }
}
