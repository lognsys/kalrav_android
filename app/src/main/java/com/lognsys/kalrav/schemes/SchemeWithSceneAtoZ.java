package com.lognsys.kalrav.schemes;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lognsys.kalrav.HomeActivity;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.fragment.ConfirmFragment;
import com.lognsys.kalrav.model.SeatExample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.ScenePosition;
import by.anatoldeveloper.hallscheme.hall.Seat;
import by.anatoldeveloper.hallscheme.hall.SeatListener;
import by.anatoldeveloper.hallscheme.view.ZoomableImageView;

/**
 * Created by Nublo on 05.12.2015.
 * Copyright Nublo
 */
//http://www.json-generator.com/api/json/get/cuGNiUpDeG?indent=2
public class SchemeWithSceneAtoZ extends Fragment {
    int AtoF,GtoO,PtoZ;
    List<SeatExample> itemsList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.basic_scheme_fragment, container, false);
        AtoF=getArguments().getInt("AtoF");
        GtoO=getArguments().getInt("GtoO");
        PtoZ=getArguments().getInt("PtoZ");
        itemsList= (List<SeatExample>) getArguments().getSerializable("itemsList");
        Log.e("","RequestItemsServiceTask itemsList "+itemsList+ "itemsList size "+itemsList.size());


        ZoomableImageView imageView = (ZoomableImageView) rootView.findViewById(R.id.zoomable_image);
        Button btnProceed = (Button) rootView.findViewById(R.id.btnProceed);
        final HallScheme scheme = new HallScheme(imageView, schemeWithScene(), getActivity());
        scheme.setScenePosition(ScenePosition.SOUTH);
        // call AsynTask to perform network operation on separate thread

//        new RequestItemsServiceTask().execute();
        scheme.setSeatListener(new SeatListener() {

            @Override
            public void selectSeat(int id) {
                Toast.makeText(getActivity(), "select seat " + id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unSelectSeat(int id) {
                Toast.makeText(getActivity(), "unSelect seat " + id, Toast.LENGTH_SHORT).show();
            }

        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Seat> seatList=scheme.getListOfSelectedSeats();
                if (seatList != null && seatList.size() > 0) {
                    for (int i=0;i<seatList.size();i++){
                        Seat seat=seatList.get(i);
                        Toast.makeText(getActivity(), "Your seat number : "+seat.marker()+seat.id()+"Your seat total price : "+seat.getTotal(), Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(getActivity(), "Please select atleast one seat to proceed further", Toast.LENGTH_SHORT).show();

                }
                ArrayList<Seat> items=(ArrayList<Seat>)seatList;
                Fragment fff=new ConfirmFragment();
                Bundle args = new Bundle();
//                args.putInt("totalPrice", totalPrice);
//                args.putSerializable("timeSlot",timeSlot);
//                args.putSerializable("dramaInfo",dramaInfo);
                args.putSerializable("seats", (ArrayList<Seat>) items);
                fff.setArguments(args);
                if (fff != null){
                    switchFragment(fff);
                  }
                else {
                    Toast.makeText(rootView.getContext(), "Please select your seat no. ", Toast.LENGTH_SHORT).show();
                }
            }
            });
        return rootView;
    }
    private void switchFragment(Fragment fff) {
        if (getActivity() == null)
            return;
        if (getActivity() instanceof HomeActivity) {
            HomeActivity feeds = (HomeActivity) getActivity();
            feeds.switchContent(fff);
        }
    }

    private class RequestItemsServiceTask
            extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog =
                new ProgressDialog(getActivity());
         List<SeatExample> itemsList;

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
//                Log.e("","RequestItemsServiceTask itemsList "+itemsList+ "itemsList size "+itemsList.size());

            } catch (Throwable e) {
                Log.e("","RequestItemsServiceTask Throwable "+e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

            // setListAdapter must not be called at doInBackground()
                     // since it would be executed in separate Thread
           /* setListAdapter(
                    new ArrayAdapter<MyItem>(ItemsListActivity.this,
                            R.layout.list_item, itemsList));
*/
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
    public List<SeatExample> findAllItems() {
        JSONObject serviceResult = SchemeWithSceneAtoZ.requestWebService(
                "http://www.json-generator.com/api/json/get/cuGNiUpDeG?indent=2");

        List<SeatExample> foundItems = new ArrayList<SeatExample>(20);

        try {
            Log.e("","RequestItemsServiceTask findAllItems serviceResult  "+serviceResult);

            JSONArray items = serviceResult.getJSONArray("seatsdetails");
            Log.e("","RequestItemsServiceTask findAllItems items  "+items);

            for (int i = 0; i < items.length(); i++) {
                JSONObject obj = items.getJSONObject(i);
                foundItems.add(
                        new SeatExample(obj.getInt("irow"),
                                obj.getInt("jrow"),
                                obj.getString("status")));
            }

        } catch (JSONException e) {
            Log.e("","RequestItemsServiceTask findAllItems JSONException  "+e);

        }

        return foundItems;
    }
    public static JSONObject requestWebService(String serviceUrl) {
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

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK)
                < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
    public Seat[][] schemeWithScene() {
        Seat seats[][] = new Seat[35][33];
        int n=91;
//      middle seat last row : Z
        int midSeatsZrow=15;
        int midSeatsGrow=15;

//      middle seat row from Y to M
        int midSeatsYtoMrow=30;

//      middle seat row from L to G
        int midSeatsLtoGrow=26;

//      LEFT - TOP seats from row Y to M
        int colLeftTopSeatsYtoMrow=27;

//     SPACE LEFT - TOP seats from row L to H
        int colSpaceLeftTopSeatsLtoHrow=23;
//        int jrow=11;
//        int irow=30;

//      SPACE RIGHT - TOP seats from row Y to M
        int colSpaceRightTopSeatsYtoMrow=33;

//      SPACE - RIGHT - SPACE - TOP seats from row L to H
        int colSpaceRightSpaceTopSeatsLtoHrow=29;


//      BOTTOM LEFT seats from row F to C
        int bottomLeftSeatsFtoCrow=28;

//      BOTTOM LEFT seats row B
        int bottomLeftSeatsBrow=27;

//      BOTTOM LEFT seats row A
        int bottomLeftSeatsArow=25;



//      BOTTOM RIGHT seats from row F to C
        int bottomRightSeatsFtoCrow=31;

//      BOTTOM RIGHT seats row B
        int bottomRightSeatsBrow=30;

//      BOTTOM RIGHT seats row A
        int bottomRightSeatsArow=28;


        for (int i = 0; i < 35; i++) {
            for (int j = 0; j < 33; j++) {
                SeatExample seat = new SeatExample();
                seat.status = HallScheme.SeatStatus.EMPTY;
                seats[i][j] = seat;

                if (i == 0) {
//                    Row P to  Z 250
                    if (j == 17) {
                        seat.marker = " Row P to Z-RS ." + PtoZ;
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                } else if (i > 0) {

                    if (i < 12) {//                row name

                        if (j > 0 || j < 33) {

                            int values = (n - (i));
                            char character = (char) values;

                            seat.marker = String.valueOf(character);
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.price = PtoZ;
                        }
                    } else if (i > 13 && i < 23) {//                row name
                        if (j > 0 || j < 33) {
                            int values = (n - (i - 2));
                            char character = (char) values;

                            seat.marker = String.valueOf(character);
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.price = GtoO;
                        }
                    } else if (i > 24 && i < 31) {//                row name

                        if (j > 0 || j < 33) {
                            int values = (n - (i - 4));
                            char character = (char) values;

                            seat.marker = String.valueOf(character);
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.price = AtoF;
                        }
                    } else if (i == 13) {
//                    Row G to  O 350
                        if (j == 17) {
                            seat.marker = " Row G to O-RS ." + GtoO;
                            seat.status = HallScheme.SeatStatus.INFO;
                        }
                    } else if (i == 24) {
//                    Row A to F-RS .500
                        if (j == 17) {
                            seat.marker = " Row A to F-RS ." + AtoF;
                            seat.status = HallScheme.SeatStatus.INFO;
                        }
                    }

                    //    space & seats left-top
                    if (j > 0 && j < 10) {

//                        seats left-top
                        if (i > 1 && i < 12 && j < 7) {
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.color = Color.argb(255, 60, 179, 113);
                            seat.id=colLeftTopSeatsYtoMrow-(1*j);

                            seat.selectedSeatMarker = String.valueOf(colLeftTopSeatsYtoMrow-(1*j));
                        } else if (i > 13 && i <= 16 && j < 7) {

                            seat.id = colLeftTopSeatsYtoMrow - (1 * j);
//                       top space + below seats left-top
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.color = Color.argb(255, 60, 179, 113);
                            seat.selectedSeatMarker = String.valueOf(colLeftTopSeatsYtoMrow - (1 * j));
                        } else if (i > 16 && i < 22 && j > 4 && j < 7) {
                            seat.id = colSpaceLeftTopSeatsLtoHrow - (1 * j);
//                           space + seats left-top
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.color = Color.argb(255, 60, 179, 113);
                            seat.selectedSeatMarker = String.valueOf(colSpaceLeftTopSeatsLtoHrow - (1 * j));
                        } else {
                            //space after seats
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                    }

//                F to A with  3 space before 12 column(seats)
                    if (i > 24 && i < 31) {

//                F to C with  3 space before 12 column(seats)
                        if (i < 29) {
                            if (j > 3 && j < 16) {
                                seat.id = bottomLeftSeatsFtoCrow - (1 * j);
//                       space + seats left
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(bottomLeftSeatsFtoCrow - (1 * j));
                            }
                        } else if (i == 29) {
//                B row with 4 space before 11 column(seats)
                            if (j > 4 && j < 16) {
                                seat.id = bottomLeftSeatsBrow - (1 * j);
//                       space + seats left
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(bottomLeftSeatsBrow - (1 * j));

                            }
                        } else {
//                A row with 4 space before 9 column(seats)
                            if (j > 6 && j < 16) {
                                seat.id = bottomLeftSeatsArow - (1 * j);

//                       space + seats left
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(bottomLeftSeatsArow - (1 * j));
                            }
                        }
                        if (j > 15 && j < 19) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                    }

                    if (i >= 1) {

//                      seats middle row
                        if (i > 0 && j > 9 && j < 24) {
                            if (i == 1) {

//                                midSeats from  14 to 1
                                seat.id = --midSeatsZrow;
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(seat.id);

                            }
                            if (i > 1 && i < 12) {
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);

                                seat.id = midSeatsYtoMrow - (1 * j);
                                seat.selectedSeatMarker = String.valueOf(midSeatsYtoMrow - (1 * j));

                            } else if (i > 13 && i < 22) {
//                               seats  middle row

                                if (i > 13 && i < 17) {
                                    seat.id = midSeatsYtoMrow - (1 * j);
//                                    Log.d("","itemsList === seat.id "+(seat.id));

                                } else if (i > 17 && i < 22) {
                                    seat.id = midSeatsLtoGrow - (1 * j);
                                }
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else  if (i == 22) {
//                                Log.d("","itemsList === midSeatsGrow "+(midSeatsGrow));

//                                midSeats from  14 to 1
                                seat.id = --midSeatsGrow;
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(seat.id);

                            }
                        }
                    }

//                  space & seats right-top
                    if (j > 23 && j < 33) {

//                        seats right-top
                        if (i > 1 && i < 12 && j > 26) {
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.color = Color.argb(255, 60, 179, 113);
                            seat.id = colSpaceRightTopSeatsYtoMrow - (1 * j);
                            seat.selectedSeatMarker = String.valueOf(colSpaceRightTopSeatsYtoMrow - (1 * j));
                        } else if (i > 13 && i < 17 && j > 26) {

                            seat.id = colSpaceRightTopSeatsYtoMrow - (1 * j);
//                        seats right-top
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.color = Color.argb(255, 60, 179, 113);

                            seat.selectedSeatMarker = String.valueOf(colSpaceRightTopSeatsYtoMrow - (1 * j));
                        } else if (i > 16 && i < 22 && j > 26 && j < 29) {
//                       space + seats right-top
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.color = Color.argb(255, 60, 179, 113);
                            seat.id = colSpaceRightSpaceTopSeatsLtoHrow - (1 * j);

                            seat.selectedSeatMarker = String.valueOf(colSpaceRightSpaceTopSeatsLtoHrow - (1 * j));
                        } else {
                            //space before seats
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                    }

//                          space + seats right+space  from  F to  A row
                    if (i > 24 && i < 31) {

//                F to C with  3 space before 12 column(seats)
                        if (i < 29) {
//                            space + seats right+space
                            if (j > 18 && j < 31) {
                                seat.id = bottomRightSeatsFtoCrow - (1 * j);
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(bottomRightSeatsFtoCrow - (1 * j));
                            }
                        } else if (i == 29) {

//                        B row with 4 space before 11 column(seats)
                            if (j > 18 && j < 30) {
                                seat.id = bottomRightSeatsBrow - (1 * j);

//                       space + seats right+space
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(bottomRightSeatsBrow - (1 * j));
                            }
                        } else {

//                      A row with 4 space before 9 column(seats)
                            if (j > 18 && j < 28) {
                                seat.id = bottomRightSeatsArow - (1 * j);

//                          space + seats left
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(bottomRightSeatsArow - (1 * j));
                            }
                        }
                    }

                }
            }
//            incrementing by  2 rows range of rows 1 to 28
            int rows=0;
            for(int k=0;k<itemsList.size();k++){
                for(int m=0;m<itemsList.size();m++){
                    SeatExample seat = new SeatExample();
                    seat.status = HallScheme.SeatStatus.BUSY;
                    int colvalue=0;
                    if(itemsList.get(k).getIrow()>=12)
                        {
                            rows=itemsList.get(k).getIrow()+2;
                            Log.d("","itemsList === rows inside ===== "+rows);
//                   row (M to 0) start               <16                                  >13
                            if(rows>13 && rows<17 && itemsList.get(k).getIrow()<=rows)
                            {

                                if(itemsList.get(k).getJrow()>20 && itemsList.get(k).getJrow()<27)
                                {
                                    colvalue=33-itemsList.get(k).getJrow()-6;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else if(itemsList.get(k).getJrow()>6 && itemsList.get(k).getJrow()<21)
                                {
                                    colvalue=33-itemsList.get(k).getJrow()-3;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                    colvalue=33-itemsList.get(k).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                            }//                   row M to O ends

//                   row H to L start                   <=21                                >16
                            if(rows>16 && rows<22 && itemsList.get(k).getIrow()<=rows )
                            {

                                if(itemsList.get(k).getJrow()>16 && itemsList.get(k).getJrow()<19)
                                {
                                    colvalue=24-itemsList.get(k).getJrow()-1;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                                else if(itemsList.get(k).getJrow()>2 && itemsList.get(k).getJrow()<17)
                                {
                                    colvalue=26-itemsList.get(k).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                                else{
                                    colvalue=29-itemsList.get(k).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }//                   row H to L ends

//                   row G start
                            if(rows ==22 && itemsList.get(k).getIrow()<=rows)
                            {


                                if(itemsList.get(k).getJrow()>0 && itemsList.get(k).getJrow()<15)
                                {
                                    colvalue=26-itemsList.get(k).getJrow()-2;

                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }//                   row G ends

     //                   row F to C start          >=25                                    <29
                            if(rows>22 && rows<29 )
                            {
                                rows=rows+2;
                                Log.d("","itemsList === rows inside rows itemsList.get(k).getJrow() ===== "+(itemsList.get(k).getJrow()));

                                colvalue=32-itemsList.get(k).getJrow()-4;
//                        Log.d("","itemsList findAllItems colvalue ================ "+colvalue);
                                if(colvalue>3 && colvalue<16)
                                {
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                    if(itemsList.get(k).getJrow()==1){
                                        Log.d("","getJrow AT K ==========="+itemsList.get(k).getJrow()
                                                +" K ========== "+k
                                                +" k+1 ==="+(k+1)
                                                +" 32-itemsList.get(k).getJrow()-k+1 ==="+(32-itemsList.get(k).getJrow()-k+1));

                                        colvalue=32-itemsList.get(k).getJrow();
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }else{
                                        colvalue=31-itemsList.get(k).getJrow();
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    seat.color = Color.argb(255, 60, 179, 113);

                                }
                            }//                   row F to C ends

//                   row B start
                            if(rows == 29 && itemsList.get(k).getIrow()<=rows)
                            {

                                if(itemsList.get(k).getJrow()>11 && itemsList.get(k).getJrow()<23)
                                {

                                    //  Log.d("","itemsList === 30-itemsList.get(k).getJrow()-3 "+(30-itemsList.get(k).getJrow()-3));

                                    colvalue=30-itemsList.get(k).getJrow()-3;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                                else{
//                                    Log.d("","itemsList === itemsList.get(k).getJrow() "+(itemsList.get(k).getJrow()));

                                    if(itemsList.get(k).getJrow()==1){
                                        colvalue=30-itemsList.get(k).getJrow();
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    else{
                                        if(itemsList.get(k).getJrow()<11 && itemsList.get(k).getJrow()>1)
                                            colvalue=30-itemsList.get(k).getJrow();
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }

                                    seat.color = Color.argb(255, 60, 179, 113);

                                }
                            }//                   row B ends

                            Log.d("","row A start b4 ===== "+(rows));

//                   row A start
                            if(rows==30 && itemsList.get(k).getIrow()<=rows)
                            {
                                Log.d("","row A start after getJrow  ===== "+(rows));

                                Log.d("","row A start after row getJrow() "+(itemsList.get(k).getJrow()));

                                if(itemsList.get(k).getJrow()>9 && itemsList.get(k).getJrow()<19)
                                {
                                    colvalue=27-itemsList.get(k).getJrow()-2;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                                else{

                                    if(itemsList.get(k).getJrow()==1){
                                        colvalue=28-itemsList.get(k).getJrow();
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    else{
                                        if(itemsList.get(k).getJrow()<10 && itemsList.get(k).getJrow()>1)
                                            colvalue=28-itemsList.get(k).getJrow();
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }

                                    seat.color = Color.argb(255, 60, 179, 113);

                                }
                            }//                   row A ends
                    }else{

//                   row (P to Y) start
                        if(itemsList.get(k).getIrow()<=11 && itemsList.get(k).getIrow()>1)
                        {
                            rows= itemsList.get(k).getIrow();
                            if(itemsList.get(k).getJrow()>20 && itemsList.get(k).getJrow()<27)
                            {
                                colvalue=33-itemsList.get(k).getJrow()-6;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else if(itemsList.get(k).getJrow()>6 && itemsList.get(k).getJrow()<21)
                            {
                                colvalue=33-itemsList.get(k).getJrow()-3;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else{
                                colvalue=33-itemsList.get(k).getJrow();
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                        }//                   row P to Y ends


//                   row Z start
                        if(itemsList.get(k).getIrow()==1)
                        {
                            rows= itemsList.get(k).getIrow();

                            if(itemsList.get(k).getJrow()>0 && itemsList.get(k).getJrow()<15)
                            {
                                colvalue=26-itemsList.get(k).getJrow()-2;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                        }//                   row Z ends


                    }
                    seats[rows][colvalue]=seat;

                }
            }
        }

        return seats;
    }



    public static boolean rowEquals(int one, int two) {
          if(one==two)
              return true;
        else
            return false;
    }
}