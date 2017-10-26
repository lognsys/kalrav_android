package com.lognsys.kalrav.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lognsys.kalrav.LoginActivity;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.adapter.CustomGridArrayAdapter;
import com.lognsys.kalrav.adapter.HorizontalAdapterUsersReview;
import com.lognsys.kalrav.db.DramaInfoDAOImpl;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.MySpannable;
import com.lognsys.kalrav.model.TimeSlot;
import com.lognsys.kalrav.util.CallAPI;
import com.lognsys.kalrav.util.Constants;
import com.lognsys.kalrav.util.KalravApplication;
import com.lognsys.kalrav.util.PropertyReader;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by admin on 02-04-2017.
 */

public class FragmentDramaDetail extends Fragment  {
//for dots
   static TextView tvDramaName,tvDramaLength,tvDramaMusic,textRatingvalue,tvDramaDate,tvDramaTiming,tvDramaLanguage,
        tvDramaGenre,textsynopsys,textDirector,textWriter,textStarcast;

    DramaInfoDAOImpl dramaInfoDAO;
    RatingBar rbRatingBar;
    //Properties
    private PropertyReader propertyReader;
    private Properties properties;
    public static final String PROPERTIES_FILENAME = "kalrav_android.properties";
    int dramaId;
    CallAPI callAPI;
    DramaInfo dramaInfo;
    TextView tvRateDrama, tvAllreviewUsers,tvAllreviewCritics, textGroupname;
    AlertDialog dialog;
    Button btnbook;
    ViewPager viewPager;
    ImageView dramaImage;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dramaInfo = (DramaInfo) getArguments().getSerializable("dramaInfo");
        if(dramaInfo!=null)
        Log.d("","Detail Fragment dramaInfo "+dramaInfo);

        dramaId= (int) getArguments().getInt("dramaId");

        dramaInfoDAO = new DramaInfoDAOImpl(getActivity());
        View view = inflater.inflate(R.layout.fragment_dramadetai, container, false);

        propertyReader = new PropertyReader(getActivity());
        properties = propertyReader.getMyProperties(PROPERTIES_FILENAME);

        dramaImage=(ImageView) view.findViewById(R.id.dramaImage);
        tvDramaName=(TextView) view.findViewById(R.id.tvDramaName);
        tvDramaLength=(TextView) view.findViewById(R.id.tvDramaLength);
        tvDramaDate=(TextView) view.findViewById(R.id.tvDramaDate);
        tvDramaMusic=(TextView) view.findViewById(R.id.tvDramaMusic);
        tvDramaTiming=(TextView) view.findViewById(R.id.tvDramaTiming);
        tvDramaLanguage=(TextView) view.findViewById(R.id.tvDramaLanguage);
        tvDramaGenre=(TextView) view.findViewById(R.id.tvDramaGenre);
        textsynopsys=(TextView) view.findViewById(R.id.textsynopsys);
        textDirector=(TextView) view.findViewById(R.id.textDirector);
        textWriter=(TextView) view.findViewById(R.id.textWriter);
        textStarcast=(TextView) view.findViewById(R.id.textStarcast);
        textGroupname =(TextView) view.findViewById(R.id.textGroupname);
        rbRatingBar = (RatingBar) view.findViewById(R.id.rbRatingBar);
        textRatingvalue=(TextView) view.findViewById(R.id.textRatingvalue);
        rbRatingBar.setRating(4.0F);
        textRatingvalue.setText("4.0");
        if(dramaInfo!= null){
            displayDramaDetail(dramaInfo,0);
        }
        else{
            displayDramaDetail(null,dramaId);
        }
        viewPager=(ViewPager)view.findViewById(R.id.viewpager);
        btnbook=(Button)view.findViewById(R.id.btnbook);
        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dramaInfo !=null && dramaInfo.getId()!=0){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dramaInfoId", dramaInfo.getId());
                    Fragment fragment = new AuditoriumListFragment();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                }
            }
        });
        //recycler view for users review

        tvRateDrama = (TextView) view.findViewById(R.id.tvRateDrama);
        tvAllreviewUsers=(TextView)view.findViewById(R.id.tvAllReviewUsers);
        tvAllreviewCritics=(TextView)view.findViewById(R.id.tvAllReviewCritics);


        tvAllreviewCritics=(TextView)view.findViewById(R.id.tvAllReviewCritics);
        tvRateDrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(KalravApplication.getInstance().getPrefs().getCustomer_id()!=null) {


                    LayoutInflater inflater = getActivity().getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.dialog_rate_now, null);
                final ImageView ivDramaImage = (ImageView) convertView.findViewById(R.id.ivDramaImage);
                final Button btnsubmit = (Button) convertView.findViewById(R.id.btnsubmit);

                final TextView tvRatingStar = (TextView) convertView.findViewById(R.id.tvRatingStar);
                final RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.rbRating);
                Log.d("","onRatingChanged dramaInfo.getLink_photo() " +dramaInfo.getLink_photo());
                if(dramaInfo.getLink_photo()!=null){
                    Picasso.with(getContext()).load(dramaInfo.getLink_photo()).into(ivDramaImage);
                }
                else{
                    Picasso.with(getContext()).load(String.valueOf(getResources().getDrawable(R.drawable.stub,null))).into(ivDramaImage);
                }


                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser) {
                        Log.d("","onRatingChanged ratingBar.getRating() "+ratingBar.getRating()+"  rating " +rating+" fromUser "+fromUser);
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
                btnsubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callAPI=new CallAPI((AppCompatActivity) getActivity());
                       Log.d("","Rest onSubmit KalravApplication.getInstance().getPrefs().getCustomer_id() "+KalravApplication.getInstance().getPrefs().getCustomer_id());
                        if(KalravApplication.getInstance().getPrefs().getCustomer_id()!=null) {

                            String ratedrama=properties.getProperty(Constants.API_URL_DRAMA.ratedrama.name());
                            Log.d("","Rest onSubmit ratedrama "+ratedrama);

                            callAPI.rateDrama(Double.parseDouble(tvRatingStar.getText().toString()), dramaInfo,
                                    Integer.parseInt(KalravApplication.getInstance().getPrefs().getCustomer_id()),ratedrama);
                            dialog.dismiss();

                        }
                        else{
                            Intent intent =new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }

                    }
                });
                }
                else{
                    Intent intent =new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        TextView tv = (TextView) view.findViewById(R.id.textsynopsys);
        makeTextViewResizable(tv, 2, "View More", true);
        return view;
    }












    public void displayDramaDetail(DramaInfo dramaInfo, int dramaId) {
        String getdramadetailbyid=properties.getProperty(Constants.API_URL_DRAMA.getdramadetailbyid.name());
        String dramaDetailURLByID = null;
        if(dramaInfo!=null){
            String groupname=dramaInfo.getGroup_name();
            textGroupname.setText(groupname);
            dramaDetailURLByID=getdramadetailbyid+dramaInfo.getId();
        }
        else{
            if(dramaId>0)
            dramaDetailURLByID=getdramadetailbyid+dramaId;

        }

        Log.d("","Rest displayDramaDetail dramaDetailURLByID "+dramaDetailURLByID);

        KalravApplication.getInstance().getPrefs().showDialog(getActivity());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                dramaDetailURLByID, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("","displayDramaDetail response "+ response.toString());

                try {
                    DramaInfo dramaInfo=new DramaInfo();

                    // Parsing json object response
                    // response will be a json object
                    int id = response.getInt("id");
                    dramaInfo.setId(id);

                    String title = response.getString("title");
                    dramaInfo.setTitle(title);
                    if(dramaInfo.getTitle()!=null){
                        tvDramaName.setText(dramaInfo.getTitle());
                    }

                    String imageurl = response.getString("imageurl");
                    dramaInfo.setLink_photo(imageurl);
                    if(dramaInfo.getLink_photo()!=null){
                        Picasso.with(getContext()).load(dramaInfo.getLink_photo()).into(dramaImage);
                    }
                    else{
                        Picasso.with(getContext()).load(String.valueOf(getResources().getDrawable(R.drawable.stub,null))).into(dramaImage);
                    }

                    String drama_length = response.getString("drama_length");
                    dramaInfo.setDrama_length(drama_length);
                    if(dramaInfo.getDrama_length()!=null){
                        tvDramaLength.setText("Drama Length : \n "+dramaInfo.getDrama_length());
                    }


                    String date = response.getString("date");
                    String[] splited=null;

                    dramaInfo.setDatetime(date);
                    if(dramaInfo.getDatetime()!=null){
                        tvDramaDate.setText(dramaInfo.getDatetime());
                        splited = dramaInfo.getDatetime().split(" ");
                        String datevalue  = splited[0]==null?"":splited[0];
                        tvDramaDate.setText(datevalue);
                        String timevalue  = splited[1]==null?"":splited[1] ;
                        tvDramaTiming.setText(timevalue);
                    }


                    String genre = response.getString("genre");
                    dramaInfo.setGenre(genre);
                    if(dramaInfo.getGenre()!=null){
                        tvDramaGenre.setText("Genre : "+dramaInfo.getGenre());
                    }

                    String star_cast = response.getString("star_cast");
                    dramaInfo.setStar_cast(star_cast);
                    if(dramaInfo.getStar_cast()!=null){
                        textStarcast.setText(dramaInfo.getStar_cast());
                    }

                    String description = response.getString("description");
                    dramaInfo.setDescription(description);
                    if(dramaInfo.getDescription()!=null){
                        textsynopsys.setText(dramaInfo.getDescription());
                    }

                    String director = response.getString("director");
                    dramaInfo.setDirector(director);
                    if(dramaInfo.getDirector()!=null){
                        textDirector.setText(dramaInfo.getDirector());
                    }


                    String writer = response.getString("writer");
                    dramaInfo.setWriter(writer);
                    if(dramaInfo.getWriter()!=null){
                        textWriter.setText(dramaInfo.getWriter());
                    }

                    String music = response.getString("music");
                    dramaInfo.setMusic(music);
                    if(dramaInfo.getMusic()!=null && dramaInfo.getMusic().length()>0){
                        tvDramaMusic.setText("Music : "+dramaInfo.getMusic());
                    }
                    String avg_rating = response.getString("avg_rating");
                    dramaInfo.setAvg_rating(avg_rating);
                    rbRatingBar.setRating(Float.parseFloat(dramaInfo.getAvg_rating()));
                    textRatingvalue.setText(dramaInfo.getAvg_rating());

                    String drama_language = response.getString("drama_language");
                    dramaInfo.setDrama_language(drama_language);

                    if(dramaInfo.getDrama_language()!=null && dramaInfo.getDrama_language().length()>0){
                        tvDramaLanguage.setText("Language : "+dramaInfo.getDrama_language());
                    }
                    dramaInfoDAO.updateDrama(dramaInfo);
                    KalravApplication.getInstance().getPrefs().hidepDialog(getActivity());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("","displayDramaDetail JSONException "+e);

                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    KalravApplication.getInstance().getPrefs().hidepDialog(getActivity());

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("","displayDramaDetail VolleyError "+error);

                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                KalravApplication.getInstance().getPrefs().hidepDialog(getContext());

            }
        });
        KalravApplication.getInstance().getPrefs().hidepDialog(getContext());

        // Adding request to request queue
        KalravApplication.getInstance().addToRequestQueue(jsonObjReq);

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

/*
    @Override
    public void onClick(View v) {


        *//*

// Created a new Dialog
        final Dialog dialog = new Dialog(getActivity());

// Set the title
        dialog.setTitle("Booking Time Slots");

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
                args.putSerializable("dramaInfo",dramaInfo);
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
//                Log.i("RecyclerView Item ", String.valueOf(getLayoutPosition()));
                dialog.dismiss();
            }
        });
// Display the dialog
        dialog.show();*//*
    }*/
}

