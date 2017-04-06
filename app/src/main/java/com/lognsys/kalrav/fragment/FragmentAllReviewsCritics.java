package com.lognsys.kalrav.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lognsys.kalrav.R;
import com.lognsys.kalrav.adapter.VerticalAdapterReview;

import java.util.ArrayList;

/**
 * Created by admin on 06-04-2017.
 */

public class FragmentAllReviewsCritics extends Fragment {
    private RecyclerView vertical_recycler_view;
    private ArrayList<String> verticalList;
    private VerticalAdapterReview verticalAdapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_all_review_critics, container, false);
        vertical_recycler_view= (RecyclerView) v.findViewById(R.id.rvCriticsReview);
        verticalList=new ArrayList<>();
        verticalList.add("verticallist 1");
        verticalList.add("verticallist 2");
        verticalList.add("verticallist 3");
        verticalList.add("verticallist 4");
        verticalList.add("verticallist 5");
        verticalList.add("verticallist 6");
        verticalList.add("verticallist 7");
        verticalList.add("verticallist 8");
        verticalList.add("verticallist 9");
        verticalList.add("verticallist 10");

        verticalAdapter=new VerticalAdapterReview(verticalList);


        LinearLayoutManager verticalLayoutmanager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        vertical_recycler_view.setLayoutManager(verticalLayoutmanager);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        vertical_recycler_view.setAdapter(verticalAdapter);
        return  v;
    }

   /* public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_all_review_critics, container, false);

        return v;*/

}
