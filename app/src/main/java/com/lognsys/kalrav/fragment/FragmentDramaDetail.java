package com.lognsys.kalrav.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lognsys.kalrav.R;
import com.lognsys.kalrav.adapter.ExpadableListAdapter;
import com.lognsys.kalrav.adapter.HorizontalAdapterCriticsReview;
import com.lognsys.kalrav.adapter.HorizontalAdapterUsersReview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 02-04-2017.
 */

public class FragmentDramaDetail extends Fragment  {
//for dots
   static TextView mDotsText[];
    private int mDotsCount;
    private LinearLayout mDotsLayout;
    int position;

    private RecyclerView horizontal_recycler_view_users;
    private RecyclerView horizontal_recycler_view_critics;
    private ArrayList<String> horizontalListUsers;
    private ArrayList<String> horizontalListReview;
    private ArrayList<String> horizontalListCritics;
    private HorizontalAdapterUsersReview horizontalAdapterUsers;
    private HorizontalAdapterCriticsReview horizontalAdapterCritics;
    ExpadableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    TextView tvRateDrama, tvAllreviewUsers,tvAllreviewCritics;
    AlertDialog dialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View view = inflater.inflate(R.layout.fragment_dramadetai, container, false);

        //recycler view for users review
        horizontal_recycler_view_users = (RecyclerView) view.findViewById(R.id.rvUsersReview);
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(horizontal_recycler_view_users);
        horizontalListUsers = new ArrayList<>();
        horizontalListUsers.add("Radhe Singh");
        horizontalListUsers.add("horizontal 2");
        horizontalListUsers.add("horizontal 3");
        horizontalListUsers.add("horizontal 4");
        horizontalListUsers.add("horizontal 5");
        horizontalListUsers.add("horizontal 6");
        horizontalListUsers.add("horizontal 7");
        horizontalListUsers.add("horizontal 8");
        horizontalListUsers.add("horizontal 9");
        horizontalListUsers.add("horizontal 10");

        horizontal_recycler_view_critics = (RecyclerView) view.findViewById(R.id.rvCriticsReview);
        SnapHelper helper2 = new LinearSnapHelper();
        helper2.attachToRecyclerView(horizontal_recycler_view_critics);
        horizontalListCritics = new ArrayList<>();
        horizontalListCritics.add("horizontal 1");
        horizontalListCritics.add("horizontal 2");
        horizontalListCritics.add("horizontal 3");
        horizontalListCritics.add("horizontal 4");
        horizontalListCritics.add("horizontal 5");
        horizontalListCritics.add("horizontal 6");
        horizontalListCritics.add("horizontal 7");
        horizontalListCritics.add("horizontal 8");
        horizontalListCritics.add("horizontal 9");
        horizontalListCritics.add("horizontal 10");
        horizontalListReview = new ArrayList<>();
        horizontalListReview.add("Really third class drama");
        horizontalListReview.add("horizontal 2");
        horizontalListReview.add("horizontal 3");
        horizontalListReview.add("horizontal 4");
        horizontalListReview.add("horizontal 5");
        horizontalListReview.add("horizontal 6");
        horizontalListReview.add("horizontal 7");
        horizontalListReview.add("horizontal 8");
        horizontalListReview.add("horizontal 9");
        horizontalListReview.add("horizontal 10");
        horizontalAdapterUsers = new HorizontalAdapterUsersReview(horizontalListUsers, horizontalListReview);
        horizontalAdapterCritics = new HorizontalAdapterCriticsReview(horizontalListCritics);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManagaer2
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view_users.setLayoutManager(horizontalLayoutManagaer);
        horizontal_recycler_view_critics.setLayoutManager(horizontalLayoutManagaer2);
        horizontal_recycler_view_users.setAdapter(horizontalAdapterUsers);
        horizontal_recycler_view_critics.setAdapter(horizontalAdapterCritics);
        tvRateDrama = (TextView) view.findViewById(R.id.tvRateDrama);
        tvAllreviewUsers=(TextView)view.findViewById(R.id.tvAllReviewUsers);
        tvAllreviewCritics=(TextView)view.findViewById(R.id.tvAllReviewCritics);

        tvAllreviewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new TestFragment();


                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });
        tvAllreviewCritics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new TestFragment();


                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });
        tvAllreviewCritics=(TextView)view.findViewById(R.id.tvAllReviewCritics);
        tvRateDrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.dialog_rate_now, null);
                final TextView tvRatingStar = (TextView) convertView.findViewById(R.id.tvRatingStar);
                final RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.rbRating);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser) {

                        tvRatingStar.setText(String.valueOf(ratingBar.getRating()));

                    }
                });
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setView(convertView);

                dialog = builder.create();
//           dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.DIM_AMOUNT_CHANGED);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();

            }
        });
// get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.elSynopsis);

        // preparing list data
        prepareListData();

        listAdapter = new ExpadableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getActivity(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        //here we create the dots
        //as you can see the dots are nothing but "."  of large size
        mDotsCount = horizontalListUsers.size();
        mDotsText = new TextView[mDotsCount];
        mDotsLayout = (LinearLayout) view.findViewById(R.id.image_count);
        //here we set the dots
        for (int i = 0; i < mDotsCount; i++) {
            mDotsText[i] = new TextView(getActivity());
            mDotsText[i].setText(".");
            mDotsText[i].setTextSize(45);
            mDotsText[i].setTypeface(null, Typeface.BOLD);
            mDotsText[i].setTextColor(android.graphics.Color.GRAY);
            mDotsLayout.addView(mDotsText[i]);
        }

        //when we scroll the images we have to set the dot that corresponds to the image to White and the others
        //will be Gray
        horizontal_recycler_view_users.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                /*long currentVisiblePosition = 0;
                currentVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
*/

                for (int i = 0; i < mDotsCount; i++) {
                    FragmentDramaDetail.mDotsText[i]
                            .setTextColor(Color.GRAY);
                }

                FragmentDramaDetail.mDotsText[horizontalAdapterUsers.position()]
                        .setTextColor(Color.RED);
            }


        });



        return view;
    }


    /*
    * Preparing the list data
    */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Nice drama go to watch it");


        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Its really great drama");


        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data

    }


}

