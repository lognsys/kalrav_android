package com.lognsys.kalrav.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lognsys.kalrav.HomeActivity;
import com.lognsys.kalrav.LoginActivity;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.RegisterActivity;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.Ratings;
import com.lognsys.kalrav.model.UserInfo;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by admin on 6/29/2017.
 */


public class CallAPI {


   @RequiresApi(api = Build.VERSION_CODES.N)
   public void rateDrama(final double rating, DramaInfo dramaInfo, int customer_id, String url){
      Ratings ratings=new Ratings();
      ratings.setRating(rating);
      // (1) get today's date
      Date today = Calendar.getInstance().getTime();

      // (2) create a date "formatter" (the date format we want)
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

      // (3) create a new String using the date format we want
      String rating_date = formatter.format(today);

      ratings.setRating_date(rating_date);
      ratings.setUsers_id(customer_id);
      ratings.setDramas_id(dramaInfo.getId());


      url=url+0+"/"+ratings.getRating()+"/"+ratings.getRating_date()+"/"+ratings.getUsers_id()+"/"+ratings.getDramas_id();
      url=url.replace(" ","%20");

      new RateDramaTask(url).execute("");
   }

   private class RateDramaTask extends AsyncTask<String, Void, String> {
      String url;
      String values;
      public RateDramaTask(String url) {
         this.url = url;
      }
      @Override
      protected String doInBackground(String... place) {
         Log.d("","rateDrama url " + url);
         int statusCode;


         try {
            URL urlToRequest = new URL(this.url);
            Log.d("", "rateDrama doInBackground this.url " + this.url);

            HttpURLConnection urlConnection =
                    (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type",
                    "application/json");
            Log.d("", "rateDrama doInBackground urlConnection " + urlConnection);
            urlConnection.setFixedLengthStreamingMode(
                    this.url.getBytes().length);
            Log.d("", "rateDrama doInBackground this.url " + this.url);

            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(this.url);

            out.close();
            // handle issues
            statusCode = urlConnection.getResponseCode();


            if (statusCode > 0) {
               Log.d("", "rateDrama doInBackground  " + statusCode);

               InputStream in =
                       new BufferedInputStream(urlConnection.getInputStream());
                   values = getResponseText(in);
                  Log.d("", "Test doInBackground  values " + values);



            }


         } catch (Exception e) {
            Log.d("", "Test doInBackground Exception " + e);
         }
         return values;
      }
      private String getResponseText(InputStream inputStream) throws IOException {
         BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

         StringBuilder total = new StringBuilder();
         String line;
         while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
         }
         String ResponseString = total.toString();
         Log.d("", "Test getResponseText ResponseString " + ResponseString);

         r.close();
         return ResponseString;
      }
      @Override
      protected void onPostExecute(String result) {
         super.onPostExecute(result);
         Log.d("", "Test onPostExecute result " + result);
         Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
      }
   }
   }
