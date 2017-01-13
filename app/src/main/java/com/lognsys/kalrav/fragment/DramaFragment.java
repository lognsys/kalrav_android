package com.lognsys.kalrav.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.util.Constants;

import java.util.ArrayList;


public class DramaFragment extends Fragment {
    ArrayList<DramaInfo> listitems = new ArrayList<>();
    RecyclerView myRecyclerView;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeList();
        getActivity().setTitle("Kalrav Arts");
    }

    public void initializeList() {
        listitems.clear();

        for (int i = 0; i < 3; i++) {

            DramaInfo item = new DramaInfo();
            item.setDrama_name(Constants.dramaNames[i]);
            item.setImageResourceId(Constants.dramaImages[i]);
            listitems.add(item);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_drama, container, false);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        myRecyclerView.setHasFixedSize(true);

        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() > 0 & myRecyclerView != null) {
            myRecyclerView.setAdapter(new MyAdapter(listitems));
        }
        myRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
            shareImageView = (ImageView) v.findViewById(R.id.shareImageView);

        }
    }


    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<DramaInfo> list;

        public MyAdapter(ArrayList<DramaInfo> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            holder.titleTextView.setText(list.get(position).getDrama_name());
            holder.coverImageView.setImageResource(list.get(position).getImageResourceId());
            // holder.coverImageView.setTag(list.get(position).getImageResourceId());
            holder.likeImageView.setTag(R.drawable.ic_like);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
