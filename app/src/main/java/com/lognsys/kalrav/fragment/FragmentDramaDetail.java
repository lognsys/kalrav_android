package com.lognsys.kalrav.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.adapter.CustomGridArrayAdapter;
import com.lognsys.kalrav.adapter.ExpadableListAdapter;
import com.lognsys.kalrav.adapter.HorizontalAdapterCriticsReview;
import com.lognsys.kalrav.adapter.HorizontalAdapterUsersReview;
import com.lognsys.kalrav.adapter.SlidingImage_Adapter;
import com.lognsys.kalrav.model.MySpannable;
import com.lognsys.kalrav.model.TimeSlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 02-04-2017.
 */

public class FragmentDramaDetail extends Fragment implements View.OnClickListener {
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
    private List<TimeSlot> timeSlotArrayList;

    TextView tvRateDrama, tvAllreviewUsers,tvAllreviewCritics;
    AlertDialog dialog;
    Button btnbook;
    private static final Integer[] IMAGES= {R.drawable.gujjubhai_ghode_chadhiya,R.drawable.gujjubhai_great,R.drawable.google,R.drawable.com_facebook_button_background};
    ViewPager viewPager;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View view = inflater.inflate(R.layout.fragment_dramadetai, container, false);
         viewPager=(ViewPager)view.findViewById(R.id.viewpager);
        btnbook=(Button)view.findViewById(R.id.btnbook);
        btnbook.setOnClickListener(this);
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

        TextView tv = (TextView) view.findViewById(R.id.textsynopsys);
        makeTextViewResizable(tv, 2, "View More", true);

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

    private static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false){
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. View More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }


    @Override
    public void onClick(View v) {

// Created a new Dialog
        final Dialog dialog = new Dialog(getActivity());

// Set the title
        dialog.setTitle("Booking Details");

// inflate the layout
        dialog.setContentView(R.layout.dialog_timealot);
//        Log.d("","Dialog open ");
//        Log.d("","Dialog open ");
//        Log.d("","Dialog open ");
//        Log.d("","Dialog open ");
//        Log.d("","Dialog open ");

        timeSlotArrayList =new ArrayList<TimeSlot>();
        for(int i=0;i<3;i++){
            TimeSlot timeSlot=new TimeSlot();
            timeSlot.setDramaId("1"+i);
            timeSlot.setDateSlot("April 14, 2017 ");
            timeSlot.setTimeSlot("2:00pm "+i);
            Log.d("","Dialog open timeSlot "+timeSlot);
            timeSlotArrayList.add(timeSlot);

        }
        Log.d("","Dialog open timeSlotArrayList size "+timeSlotArrayList.size());

   CustomGridArrayAdapter arrayAdapter=
                new CustomGridArrayAdapter(getActivity(), (ArrayList<TimeSlot>) timeSlotArrayList);
        GridView gridView = (GridView)dialog.findViewById(R.id.gridview);
        gridView.setAdapter(arrayAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TimeSlot timeSlot = (TimeSlot) parent.getItemAtPosition(position);
                 Fragment fragment = new BookingSeatsFragment();
                Bundle args = new Bundle();
                args.putSerializable("timeSlot",timeSlot);
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
//                Log.i("RecyclerView Item ", String.valueOf(getLayoutPosition()));
                dialog.dismiss();
            }
        });
// Display the dialog
        dialog.show();
    }
}

