package com.lognsys.kalrav.fragment;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.model.Auditorium;
import com.lognsys.kalrav.model.AuditoriumPriceRange;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int dramaInfoId;
    RecyclerView myRecyclerView;
    List<Auditorium> auditoriumList;
    List<AuditoriumPriceRange> auditoriumPriceRangeList;
    Auditorium auditorium;
    MyAdapter adapter;

    static List<SeatExample> itemsList;


    //Properties
    private PropertyReader propertyReader;
    private Properties properties;
    public static final String PROPERTIES_FILENAME = "kalrav_android.properties";

    private WeekCalendar weekCalendar;
    private OnFragmentInteractionListener mListener;

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
        // Inflate the layout for this fragment
        /*SimpleDateFormat curFormater = new SimpleDateFormat("EEE dd");
        GregorianCalendar date = new GregorianCalendar();
        String[] dateStringArray = new String[7];
        date.set(GregorianCalendar.WEEK_OF_MONTH, date.get(GregorianCalendar.DATE)-date.get(GregorianCalendar.DAY_OF_WEEK)+1);
        for (int day = 0; day < 7; day++) {
            dateStringArray[day] = curFormater.format(date.getTime());
            date.setFirstDayOfWeek(day);
            date.roll(Calendar.DAY_OF_MONTH, true);
            Log.d("","HELLO WORLD DAYS: "+dateStringArray[day]);
        }*/
        String strDate = fmt.print(new DateTime());

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
                myRecyclerView.removeAllViews();
               String strDate = fmt.print(dateTime);
                KalravApplication.getInstance().getPrefs().showpDialog(getActivity());

                requestAuditoriumDateTime(dramaInfoId,strDate);
                Toast.makeText(getActivity(), "You Selected " + strDate, Toast
                        .LENGTH_SHORT).show();
            }

        });
        /*weekCalendar.setOnWeekChangeListener(new OnWeekChangeListener() {
            @Override
            public void onWeekChange(DateTime firstDayOfTheWeek, boolean forward) {
                Toast.makeText(getActivity(), "Week changed: " + firstDayOfTheWeek +
                        " Forward: " + forward, Toast.LENGTH_SHORT).show();
            }
        });*/
        KalravApplication.getInstance().getPrefs().showpDialog(getActivity());
        requestAuditoriumDateTime(dramaInfoId,strDate);
        Log.d(TAG,"requestAuditoriumDateTime strDate "+strDate);

        return view;
    }

    private void requestAuditoriumDateTime(int dramaInfoId, String strDate) {
        auditoriumList=new ArrayList<Auditorium>();

        final String auditoriumlist=properties.getProperty(Constants.API_URL_AUDITORIUM_LIST.getauditoriumlist.name());
        Log.d(TAG, "requestAuditoriumDateTime auditoriumlist..."+auditoriumlist);

        JsonArrayRequest req = new JsonArrayRequest(auditoriumlist,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                    Log.d("","requestAuditoriumDateTime response "+ response.toString());
                    for(int i=0;i<response.length();i++){
                        auditorium=new Auditorium();
                        auditoriumPriceRangeList=new ArrayList<AuditoriumPriceRange>();

                        JSONObject jsonObject= response.getJSONObject(i);

                        int auditoriumid = jsonObject.getInt("auditoriumid");
                        auditorium.setAudiId(String.valueOf(auditoriumid));

                        String auditoriumname = jsonObject.getString("auditoriumname");
                        auditorium.setAudiName(auditoriumname);

                        String time=jsonObject.getString("time");

                        JSONArray jsontimeArray=new JSONArray(time);
                        StringBuilder sb=new StringBuilder();
                            for(int j=0;j<jsontimeArray.length();j++)
                            {
                                String timevalue= jsontimeArray.getString(j);
                                sb.append(timevalue+" ");
                            }
                        auditorium.setDatetime(sb.toString());

//                        String auditoriumpricerange = ;
                        JSONArray jsonArrayprice=new JSONArray(jsonObject.getString("auditoriumpricerange"));
                        Log.d("","requestAuditoriumDateTime jsonArrayprice =======  length "+jsonArrayprice.length());

                        for(int p=0;p<jsonArrayprice.length();p++){
                            JSONObject jsonObjectpricerange= jsonArrayprice.getJSONObject(p);
                            AuditoriumPriceRange auditoriumPriceRange=new AuditoriumPriceRange();


                            int istart = jsonObjectpricerange.getInt("istart");
                            auditoriumPriceRange.setIstart(istart);

                            Log.d("","requestAuditoriumDateTime jsonObjectpricerange ======= istart "+istart);

                            int iend = jsonObjectpricerange.getInt("iend");
                            auditoriumPriceRange.setIend(iend);

                            Log.d("","requestAuditoriumDateTime jsonObjectpricerange ======= iend "+iend);

                            int price = jsonObjectpricerange.getInt("price");
                            auditoriumPriceRange.setPrice(price);

                            auditoriumPriceRange.setAudiId(auditorium.getAudiId());
                            Log.d("","requestAuditoriumDateTime jsonObjectpricerange ======= price "+price);
                            auditoriumPriceRangeList.add(auditoriumPriceRange);
                        }
                        auditorium.setAuditoriumPriceRanges((ArrayList<AuditoriumPriceRange>) auditoriumPriceRangeList);

                        auditoriumList.add(auditorium);

                    }
                    if (auditoriumList.size() > 0 & auditoriumList != null) {
                        adapter= new MyAdapter(auditoriumList);
                        myRecyclerView.setAdapter(adapter);
                    }

                    KalravApplication.getInstance().getPrefs().hidepDialog(getActivity());


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("","requestAuditoriumDateTime JSONException "+e);

                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    KalravApplication.getInstance().getPrefs().hidepDialog(getActivity());

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("","requestAuditoriumDateTime VolleyError "+error);

                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        //                future implementation with  auditorium id or name and with  date time too
                          //                future implementation with  auditorium id or name and with  date time too

                        new RequestItemsServiceTask(auditorium).execute();

//                        Fragment fragment = new SchemePrabhodhanFragment();
//                        getActivity().getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                    }
                });
                /**** Any other text view setup code ****/

                holder.linearTime.addView(newTV);
                textList.add(newTV);
            }

            Log.d("", "MyAdapter onBindViewHolder textTime "+holder.textTime.getText().toString());
        }
        private class RequestItemsServiceTask
                extends AsyncTask<Void, Void, Void> {
            private ProgressDialog dialog =
                    new ProgressDialog(getActivity());
            Auditorium auditorium;
            public RequestItemsServiceTask(Auditorium auditorium) {
            this.auditorium=auditorium;
            }

            @Override
            protected void onPreExecute() {
                // TODO i18n
                dialog.setMessage("Please wait..");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... unused) {
                // The ItemService would contain the method showed
                // in the previous paragraph
//            ItemService itemService = ItemService.getCurrentInstance();
                try {
                    Log.e("","RequestItemsServiceTask ");

                    itemsList = findAllItems();

                } catch (Throwable e) {
                    Log.e("","RequestItemsServiceTask Throwable "+e);

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("itemsList", (Serializable) itemsList);


                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
               if(this.auditorium.getAudiName().equalsIgnoreCase("Aspee")){

                   Toast.makeText(getActivity(),"auditorium.getAuditoriumPriceRanges() "+this.auditorium.getAuditoriumPriceRanges(),Toast.LENGTH_LONG).show();
                   if( this.auditorium.getAuditoriumPriceRanges()!= null){

                       bundle.putSerializable("auditorium",this.auditorium);

                   }
                   Fragment fragment = new SchemeWithAspee();
                   fragment.setArguments(bundle);
                   getActivity().getSupportFragmentManager().beginTransaction()
                           .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

               }
               else if(this.auditorium.getAudiName().equalsIgnoreCase("Bhaidas")){
                   Toast.makeText(getActivity(),"auditorium.getAuditoriumPriceRanges() "+this.auditorium.getAuditoriumPriceRanges(),Toast.LENGTH_LONG).show();
                   if( this.auditorium.getAuditoriumPriceRanges()!= null){

                       bundle.putSerializable("auditorium",this.auditorium);

                   }
                   Fragment fragment = new SchemeBhaidasFragment();
                   fragment.setArguments(bundle);
                   getActivity().getSupportFragmentManager().beginTransaction()
                           .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

               }
                else{
                   Toast.makeText(getActivity(),"auditorium.getAuditoriumPriceRanges() "+this.auditorium.getAuditoriumPriceRanges(),Toast.LENGTH_LONG).show();
                   if( this.auditorium.getAuditoriumPriceRanges()!= null){

                       bundle.putSerializable("auditorium",this.auditorium);

                   }
                   Fragment fragment = new SchemePrabhodhanFragment();
                   fragment.setArguments(bundle);
                   getActivity().getSupportFragmentManager().beginTransaction()
                           .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

               }

            }

        }
        public List<SeatExample> findAllItems() {
            JSONObject serviceResult = requestWebService(
                    "http://www.json-generator.com/api/json/get/cfqTqBmHUy?indent=2");

            List<SeatExample> foundItems = new ArrayList<SeatExample>(10000);

            try {

                JSONArray items = serviceResult.getJSONArray("seatsdetails");

//                Log.e("","RequestItemsServiceTask findAllItems items  "+items);
                Log.e("","RequestItemsServiceTask findAllItems items.length()  "+items.length());

                for (int i = 0; i < items.length(); i++) {
                    JSONObject obj = items.getJSONObject(i);
                    SeatExample seatExample=new SeatExample();
                    seatExample.setIrow(obj.getInt("i"));
                    seatExample.setJrow(obj.getInt("j"));

                    foundItems.add(seatExample);
                }

            } catch (JSONException e) {
                Log.e("","RequestItemsServiceTask findAllItems JSONException  "+e);
            }

            return foundItems;
        }

        private  String getResponseText(InputStream inStream) {
            // very nice trick from
            // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
            return new Scanner(inStream).useDelimiter("\\A").next();
        }
        private  void disableConnectionReuseIfNecessary() {
            // see HttpURLConnection API doc
            if (Integer.parseInt(Build.VERSION.SDK)
                    < Build.VERSION_CODES.FROYO) {
                System.setProperty("http.keepAlive", "false");
            }
        }
        public JSONObject requestWebService(String serviceUrl) {
            Log.e("","RequestItemsServiceTask requestWebService serviceUrl  "+serviceUrl);

            disableConnectionReuseIfNecessary();

            HttpURLConnection urlConnection = null;
            try {
                // create connection
                URL urlToRequest = null;
                try {
                    urlToRequest = new URL(serviceUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e("","RequestItemsServiceTask requestWebService MalformedURLException  "+e);

                }
                urlConnection = (HttpURLConnection)
                        urlToRequest.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);

                // handle issues
                int statusCode = urlConnection.getResponseCode();
                Log.e("","RequestItemsServiceTask requestWebService statusCode  "+statusCode);


                if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    // handle unauthorized (if service requires user login)
                } else if (statusCode != HttpURLConnection.HTTP_OK) {
                    // handle any other errors, like 404, 500,..
                }

                // create JSON object from content
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                return new JSONObject(getResponseText(in));

            } catch (MalformedURLException e) {
                // URL is invalid
                Log.e("","RequestItemsServiceTask requestWebService MalformedURLException  "+e);

            } catch (SocketTimeoutException e) {
                // data retrieval or connection timed out
                Log.e("","RequestItemsServiceTask requestWebService SocketTimeoutException  "+e);

            } catch (IOException e) {
                // could not read response body
                // (could not create input stream)
                Log.e("","RequestItemsServiceTask requestWebService IOException  "+e);

            } catch (JSONException e) {
                // response body is no valid JSON string
                Log.e("","RequestItemsServiceTask requestWebService JSONException  "+e);

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;
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
