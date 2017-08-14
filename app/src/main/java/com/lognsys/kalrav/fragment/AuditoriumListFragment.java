package com.lognsys.kalrav.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.model.Auditorium;
import com.lognsys.kalrav.model.AuditoriumPriceRange;
import com.lognsys.kalrav.model.BookingInfo;
import com.lognsys.kalrav.model.SeatExample;
import com.lognsys.kalrav.schemes.SchemeBhaidasFragment;
import com.lognsys.kalrav.schemes.SchemePrabhodhanFragment;
import com.lognsys.kalrav.schemes.SchemeWithAspee;
import com.lognsys.kalrav.util.Constants;
import com.lognsys.kalrav.util.KalravApplication;
import com.lognsys.kalrav.util.PropertyReader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import noman.weekcalendar.WeekCalendar;
import noman.weekcalendar.listener.OnDateClickListener;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AuditoriumListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AuditoriumListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuditoriumListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "AuditoriumListFragment";
    List<SeatExample> seatesItems;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int dramaInfoId;
    RecyclerView myRecyclerView;
    List<Auditorium> auditoriumList;
    List<AuditoriumPriceRange> auditoriumPriceRangeList;
    Auditorium auditorium;
    MyAdapter adapter;
    TextView textError;
    static List<SeatExample> itemsList;


    //Properties
    private PropertyReader propertyReader;
    private Properties properties;
    public static final String PROPERTIES_FILENAME = "kalrav_android.properties";

    private WeekCalendar weekCalendar;
    private OnFragmentInteractionListener mListener;
    String strDate=null;
    public AuditoriumListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuditoriumListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AuditoriumListFragment newInstance(String param1, String param2) {
        AuditoriumListFragment fragment = new AuditoriumListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auditorium_list, container, false);
        dramaInfoId = (int) getArguments().getSerializable("dramaInfoId");
        propertyReader = new PropertyReader(getActivity());
        properties = propertyReader.getMyProperties(PROPERTIES_FILENAME);


        final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

         strDate = fmt.print(new DateTime());
         textError=(TextView) view.findViewById(R.id.textError);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        myRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
         auditorium=new Auditorium();

        myRecyclerView.setLayoutManager(MyLayoutManager);

        weekCalendar = (WeekCalendar)view.findViewById(R.id.weekCalendar);
        weekCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(DateTime dateTime) {
                if(KalravApplication.getInstance().isConnectedToInternet()){
                    myRecyclerView.removeAllViews();

                    String strDate = fmt.print(dateTime);
                    KalravApplication.getInstance().getPrefs().showDialog(getActivity());

                    requestAuditoriumDateTime(dramaInfoId,strDate);
                    Toast.makeText(getActivity(), "You Selected " + strDate, Toast
                            .LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getContext(),getString(R.string.network_connection),Toast.LENGTH_SHORT).show();

                }
            }

        });

        if(KalravApplication.getInstance().isConnectedToInternet()) {

            KalravApplication.getInstance().getPrefs().showDialog(getActivity());
            requestAuditoriumDateTime(dramaInfoId, strDate);
            Log.d(TAG, "requestAuditoriumDateTime strDate " + strDate);
        }
        else{
            Toast.makeText(getContext(),getString(R.string.network_connection),Toast.LENGTH_SHORT).show();

        }
        return view;
    }

    private void requestAuditoriumDateTime(int dramaInfoId, String strDate) {

        final String auditoriumlist=properties.getProperty(Constants.API_URL_AUDITORIUM_LIST.getauditoriumlist.name())+dramaInfoId+"/"+strDate;
        Log.d(TAG, "requestAuditoriumDateTime auditoriumlist..."+auditoriumlist);

        JsonArrayRequest req = new JsonArrayRequest(auditoriumlist,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if(response!=null)
                            {
                            Log.d(TAG, "requestAuditoriumDateTime response " + response.toString());
                                auditoriumList = new ArrayList<Auditorium>();
                                myRecyclerView.setVisibility(View.VISIBLE);
                                for (int i = 0; i < response.length(); i++) {
                                auditorium = new Auditorium();
                                auditoriumPriceRangeList = new ArrayList<AuditoriumPriceRange>();

                                JSONObject jsonObject = response.getJSONObject(i);

                                int auditoriumid = jsonObject.getInt("id");
                                auditorium.setAudiId(String.valueOf(auditoriumid));

                                String auditoriumname = jsonObject.getString("auditorium_name");
                                auditorium.setAudiName(auditoriumname);

                                String time = jsonObject.getString("time");

//                        JSONArray jsontimeArray=new JSONArray(time);
//                        StringBuilder sb=new StringBuilder();
//                            for(int j=0;j<jsontimeArray.length();j++)
//                            {
//                                String timevalue= jsontimeArray.getString(j);
//                                sb.append(timevalue+" ");
//                            }
                                auditorium.setDatetime(time);

//                        String auditoriumpricerange = ;
                                JSONArray jsonArrayprice = new JSONArray(jsonObject.getString("auditoriumpricelist"));
                                Log.d("", "requestAuditoriumDateTime jsonArrayprice =======  length " + jsonArrayprice.length());

                                for (int p = 0; p < jsonArrayprice.length(); p++) {
                                    JSONObject jsonObjectpricerange = jsonArrayprice.getJSONObject(p);
                                    AuditoriumPriceRange auditoriumPriceRange = new AuditoriumPriceRange();


                                    int istart = jsonObjectpricerange.getInt("istart");
                                    auditoriumPriceRange.setIstart(istart);

                                    Log.d(TAG, "requestAuditoriumDateTime jsonObjectpricerange ======= istart " + istart);

                                    int iend = jsonObjectpricerange.getInt("iend");
                                    auditoriumPriceRange.setIend(iend);

                                    Log.d(TAG, "requestAuditoriumDateTime jsonObjectpricerange ======= iend " + iend);

                                    int price = jsonObjectpricerange.getInt("price");
                                    auditoriumPriceRange.setPrice(price);

                                    auditoriumPriceRange.setAudiId(auditorium.getAudiId());
                                    Log.d(TAG, "requestAuditoriumDateTime jsonObjectpricerange ======= price " + price);
                                    auditoriumPriceRangeList.add(auditoriumPriceRange);
                                }
                                auditorium.setAuditoriumPriceRanges((ArrayList<AuditoriumPriceRange>) auditoriumPriceRangeList);

                                auditoriumList.add(auditorium);

                            }
                            if (auditoriumList.size() > 0 & auditoriumList != null) {
                                adapter = new MyAdapter(auditoriumList);
                                myRecyclerView.setAdapter(adapter);
                            }

                            KalravApplication.getInstance().getPrefs().hidepDialog(getActivity());
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG,"requestAuditoriumDateTime JSONException "+e);

                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    KalravApplication.getInstance().getPrefs().hidepDialog(getActivity());

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                myRecyclerView.setVisibility(View.GONE);
                String json = null;
                String str=null;
                byte[] response=null;
                if(volleyError.networkResponse.data!=null)
                    response = volleyError.networkResponse.data;
                Log.d(TAG,"Rest volleyError response " +response);
                try {
                    if (response != null && response.length > 0) {
                        str = new String(response, "UTF-8");
                        Log.d(TAG, "Rest volleyError str toString  " + str.toString());

                        try {
                            JSONObject object = new JSONObject(str.toString());
                            Log.d(TAG, "Rest inside object  " + object);

                            int statusCode = object.getInt("statusCode");
                            Log.d(TAG, "Rest inside statusCode  " + statusCode);

                            if (statusCode == 400) {
                                String msg = object.getString("msg");
                                Toast.makeText(getActivity(),
                                        msg, Toast.LENGTH_SHORT).show();
                               textError.setText(msg);
                               textError.setVisibility(View.VISIBLE);

                            } else if (statusCode == 406) {
                                String msg = object.getString("msg");
                                 Toast.makeText(getActivity(),
                                        msg, Toast.LENGTH_SHORT).show();
                               textError.setText(msg);
                               textError.setVisibility(View.VISIBLE);
                            } else if (statusCode == 404) {
                                String msg = object.getString("msg");
                               Toast.makeText(getActivity(),
                                        msg, Toast.LENGTH_SHORT).show();
                               textError.setText(msg);
                               textError.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }



                // hide the progress dialog
                KalravApplication.getInstance().getPrefs().hidepDialog(getActivity());

            }
        });
        KalravApplication.getInstance().getPrefs().hidepDialog(getActivity());

        // Adding request to request queue
        KalravApplication.getInstance().addToRequestQueue(req);

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setSeatesItems(List<SeatExample> seatesItems) {
        this.seatesItems = seatesItems;
    }
    public List<SeatExample> getSeatesItems() {
       return this.seatesItems;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }





    public class MyAdapter extends RecyclerView.Adapter<AuditoriumListFragment.MyAdapter.MyViewHolder> {
        private List<Auditorium> list;

        public MyAdapter(List<Auditorium> Data) {
            list = Data;
//            Log.d("", "MyAdapter constructore list "+list+" list size ==="+list.size());

        }
        @Override
        public AuditoriumListFragment.MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            // create a new view
            Log.d("", "MyAdapter onCreateViewHolder ");
            View view=null;
            Auditorium auditorium =list.get(position);

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.auditoriumdetailfragment, parent, false);
            AuditoriumListFragment.MyAdapter.MyViewHolder holder = new AuditoriumListFragment.MyAdapter.MyViewHolder(view);


            return holder;


        }

        @Override
        public void onBindViewHolder(final AuditoriumListFragment.MyAdapter.MyViewHolder holder, final int position) {
//            Log.d("", "MyAdapter onBindViewHolder ");

             final Auditorium auditorium = list.get(position);
            holder.textAuditoriumName.setText(auditorium.getAudiName());
            String  timeArray = auditorium.getDatetime();
            Log.d("", "MyAdapter onBindViewHolder timeArray "+timeArray);
            final String[] split= timeArray.split(" ");
                      Log.d("", "MyAdapter onBindViewHolder auditorium.getAuditoriumPriceRanges() "+auditorium.getAuditoriumPriceRanges());

            List<TextView> textList = new ArrayList<TextView>();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(30, 0, 30, 0);
            for(int i = 0; i < split.length; i++)
            {
                TextView newTV = new TextView(getContext());
                newTV.setText(split[i]);
                newTV.setBackground(getResources().getDrawable(R.drawable.textviewbackgroundwithborder));

                newTV.setLayoutParams(layoutParams);
                final int finalI = i;
                newTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),"Time"+split[finalI]+" Auditorium  name "+auditorium.getAudiName()+"Auditorium  id"+auditorium.getAudiId(),Toast.LENGTH_LONG).show();
                        final String bookedseatsurl=properties.getProperty(Constants.API_URL_BOOKING.bookedseats.name());
                        Log.d(TAG, "requestAuditoriumDateTime bookedseatsurl..."+bookedseatsurl);
                        KalravApplication.getInstance().getPrefs().showDialog(getActivity());
                        requestWebService(bookedseatsurl,split[finalI],strDate);
//                        new RequestItemsServiceTask(auditorium,split[finalI],strDate).execute();

                    }
                });
                /**** Any other text view setup code ****/

                holder.linearTime.addView(newTV);
                textList.add(newTV);
            }

            Log.d("", "MyAdapter onBindViewHolder textTime "+holder.textTime.getText().toString());
        }

        public  void requestWebService(String bookedseatsurl, final String time, final String strDate) {
            Log.e("","RequestItemsServiceTask requestWebService bookedseatsurl  "+bookedseatsurl);
            JSONObject params = new JSONObject();
            try {

                params.put("dramas_id", dramaInfoId);
                params.put("auditoriums_id",Integer.parseInt(auditorium.getAudiId()));
                Log.d("Confirm Fragment","bookedSeats params " +params);

            } catch (Exception e) {
                Log.d(TAG,"bookedSeats Exception "+e  );

                e.printStackTrace();
            }


            final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                    bookedseatsurl, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            List<SeatExample> foundItems = new ArrayList<SeatExample>(10000);

                            Log.d(TAG,"bookedSeats response " +response);
                            try {
                                JSONArray jsonseatnumberdetails=response.getJSONArray("seatnumberdetails");
                                Log.d(TAG,"bookedSeats jsonseatnumberdetails " +jsonseatnumberdetails);
                                Log.d(TAG,"bookedSeats jsonseatnumberdetails length " +jsonseatnumberdetails.length());
//                                    [{"i":1,"j":8},{"i":1,"j":7},{"i":1,"j":6},{"i":1,"j":5}]

                                for(int i=0;i<jsonseatnumberdetails.length();i++){
                                        SeatExample seatExample=new SeatExample();
                                        Log.d("Confirm Fragment","bookedSeats jsonseatnumberdetails.get(i) " +jsonseatnumberdetails.get(i));
                                        JSONObject jsonObject=(JSONObject)jsonseatnumberdetails.getJSONObject(i);
                                        Log.d("Confirm Fragment","bookedSeats jsonObject " +jsonObject);

                                        int ith=jsonObject.getInt("i");
                                        Log.d("Confirm Fragment","bookedSeats ith " +ith);
                                        seatExample.setIrow(ith);

                                        int jth=jsonObject.getInt("j");
                                        Log.d("Confirm Fragment","bookedSeats jth " +jth);
                                        seatExample.setJrow(jth);

                                        foundItems.add(seatExample);
                                    }
                                Log.e("","RequestItemsServiceTask bookedseatsurl foundItems  "+foundItems);
                                Log.e("","RequestItemsServiceTask bookedseatsurl foundItems.length()  "+foundItems.size());


                                if(foundItems!=null && foundItems.size()>0){
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("itemsList", (Serializable) foundItems);
                                    bundle.putInt("dramaInfoId", dramaInfoId);
                                    bundle.putString("time", time);
                                    bundle.putString("strDate", strDate);

                                    KalravApplication.getInstance().getPrefs().hidepDialog(getActivity());
                                    if(auditorium.getAudiName().equalsIgnoreCase("Aspee")){

                                        if( auditorium.getAuditoriumPriceRanges()!= null){

                                            bundle.putSerializable("auditorium",auditorium);

                                        }
                                        Fragment fragment = new SchemeWithAspee();
                                        fragment.setArguments(bundle);
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                                    }
                                    else if(auditorium.getAudiName().equalsIgnoreCase("Bhaidas")){
                                        if(auditorium.getAuditoriumPriceRanges()!= null){

                                            bundle.putSerializable("auditorium",auditorium);

                                        }
                                        Fragment fragment = new SchemeBhaidasFragment();
                                        fragment.setArguments(bundle);
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                                    }
                                    else{
                                        if(auditorium.getAuditoriumPriceRanges()!= null){

                                            bundle.putSerializable("auditorium",auditorium);

                                        }
                                        Fragment fragment = new SchemePrabhodhanFragment();
                                        fragment.setArguments(bundle);
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
//                Log.d("Response","Rest volleyError networkResponse.data " +volleyError.networkResponse.data);

                    KalravApplication.getInstance().getPrefs().hidepDialog(getActivity());
                    String json = null;
                    String str=null;
                    byte[] response=null;
                    if(volleyError.networkResponse.data!=null)
                        response = volleyError.networkResponse.data;
                    Log.d("Response","bookedSeats volleyError response " +response);
                    try {
                        str = new String(response, "UTF-8");
                        Log.d("Response","bookedSeats volleyError str toString  " +str.toString() );

                        try {
                            JSONObject object=new JSONObject(str.toString());
                            Log.d("Response","exception inside object  " +object);

                            int  statusCode=object.getInt("statusCode");
                            Log.d("Response","Rest inside statusCode  " +statusCode);

                            if(statusCode==400){
                                String msg=object.getString("msg");
                                displayMessage(msg);
                            }
                            else if(statusCode==406){
                                String msg=object.getString("msg");
                                displayMessage(msg);
                            } else if(statusCode==404){
                                String msg=object.getString("msg");
                                displayMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                //Somewhere that has access to a context
                public void displayMessage(String toastString){
                    Log.d("Response","Rest volleyError toastString  " +toastString );

                    Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_LONG).show();
                }
            });
            KalravApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        }



        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView textAuditoriumName;
            LinearLayout linearTime,linearblock;
            public TextView textTime;


            public MyViewHolder(View itemView) {
                super(itemView);
                textAuditoriumName = (TextView) itemView.findViewById(R.id.textAuditoriumName);
                linearTime = (LinearLayout) itemView.findViewById(R.id.linearTime);
                textTime=new TextView(getActivity());
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition(); // gets item position
               /* if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                    DramaInfo dramaInfo = list.get(position);
                    // We can access the data within the views
                    if(dramaInfo!=null) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("dramaInfo", dramaInfo);
                        Fragment fragment = new FragmentDramaDetail();
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                    }
                }*/
            }
        }
    }
}
