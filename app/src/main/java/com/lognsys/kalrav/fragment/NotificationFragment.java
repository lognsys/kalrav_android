package com.lognsys.kalrav.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.adapter.CustomListAdapter;
import com.lognsys.kalrav.model.Notification;
import com.lognsys.kalrav.util.KalravApplication;
import com.lognsys.kalrav.util.MockObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {


    // Log tag
    private static final String TAG = NotificationFragment.class.getSimpleName();
    private ProgressDialog pDialog;
   // private List<Notification> notificationList = new ArrayList<Notification>();
    private ListView listView;
    private CustomListAdapter adapter;



    @Override
    public View  onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        listView = (ListView) view.findViewById(R.id.list);
        adapter = new CustomListAdapter(this.getContext(), MockObjects.getNotificationList());
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(getContext());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

//        // Creating volley request obj
//        JsonArrayRequest movieReq = new JsonArrayRequest(url,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());
//                        hidePDialog();

//                        // Parsing json
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//
//                                JSONObject obj = response.getJSONObject(i);
//                                Movie movie = new Movie();
//                                movie.setTitle(obj.getString("title"));
//                                movie.setThumbnailUrl(obj.getString("image"));
//                                movie.setRating(((Number) obj.get("rating"))
//                                        .doubleValue());
//                                movie.setYear(obj.getInt("releaseYear"));
//
//                                // Genre is json array
//                                JSONArray genreArry = obj.getJSONArray("genre");
//                                ArrayList<String> genre = new ArrayList<String>();
//                                for (int j = 0; j < genreArry.length(); j++) {
//                                    genre.add((String) genreArry.get(j));
//                                }
//                                movie.setGenre(genre);
//
//                                // adding movie to movies array
//                                movieList.add(movie);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
//                        adapter.notifyDataSetChanged();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                hidePDialog();
//
//            }
//        });

        // Adding request to request queue
  //      KalravApplication.getInstance().addToRequestQueue(movieReq);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
