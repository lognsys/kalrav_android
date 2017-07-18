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
import com.lognsys.kalrav.FCM.FCMInstanceIdService;
import com.lognsys.kalrav.HomeActivity;
import com.lognsys.kalrav.LoginActivity;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.RegisterActivity;
import com.lognsys.kalrav.SettingFragment;
import com.lognsys.kalrav.db.UserInfoDAOImpl;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.Ratings;
import com.lognsys.kalrav.model.UserInfo;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
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

    public static final String TAG = "CallAPI";
    public static final String KEY_DEVICE_TOKEN = "deviceToken";

    //Properties
    private PropertyReader propertyReader;
    private Properties properties;
    public static final String PROPERTIES_FILENAME = "kalrav_android.properties";
    public Context mContext;
    UserInfoDAOImpl userDaoImpl;

    public CallAPI(AppCompatActivity activity) {
        mContext=activity;
    }




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

    //   alreadyExist user checking
    public void alReadyExsistUser(UserInfo userInfo, final String fb_id, final String google_id) {
        propertyReader = new PropertyReader(getApplicationContext());
        properties = propertyReader.getMyProperties(PROPERTIES_FILENAME);

        userDaoImpl = new UserInfoDAOImpl(mContext);
        String alReadyExsistUser=properties.getProperty(Constants.API_URL_USER.get_userdetails_already_exist_url.name())+userInfo.getEmail();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,alReadyExsistUser,
                null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    if (jsonObject != null) {
                        Log.d(TAG, "Rest   alReadyExsistUser jsonObject..."+jsonObject);

                        UserInfo  userInfo = new UserInfo();
                        userInfo.setId(jsonObject.getInt("id"));
                        userInfo.setName(jsonObject.getString("realname"));
                        userInfo.setEmail(jsonObject.getString("username"));
                        if (fb_id != null){
                            userInfo.setFb_id(fb_id);
                            KalravApplication.getInstance().getPrefs().setUser_id(fb_id);
                        }
                        if (google_id != null) {
                            userInfo.setGoogle_id(google_id);
                            KalravApplication.getInstance().getPrefs().setUser_id(google_id);
                        }
                        userInfo.setPhoneNo(jsonObject.getString("phone"));
                        userInfo.setAddress(jsonObject.getString("address"));
                        userInfo.setCity(jsonObject.getString("city"));
                        userInfo.setState(jsonObject.getString("state"));
                        userInfo.setZipcode(jsonObject.getString("zipcode"));
                        userInfo.setGroupname(jsonObject.getString("group"));
                        userInfo.setLoggedIn(Constants.LOG_IN);

//                      save to the database
                        KalravApplication.getInstance().getPrefs().setUser_Group_Name(userInfo.getGroupname());
                        KalravApplication.getInstance().getPrefs().setEmail(userInfo.getEmail());

                        KalravApplication.getInstance().getPrefs().setCustomer_id(String.valueOf(userInfo.getId()));
                        KalravApplication.getInstance().setGlobalUserObject(userInfo);
                        Log.d("", "Rest alReadyExsistUser Global object  " + KalravApplication.getInstance().getGlobalUserObject());

                        if(KalravApplication.getInstance().getPrefs().getDevice_token()!=null)
                            userInfo.setDevice(KalravApplication.getInstance().getPrefs().getDevice_token());
                        Log.d("", "Rest alReadyExsistUser userInfo getDevice " + userInfo.getDevice());

                        userDaoImpl.addUser(userInfo);
                        KalravApplication.getInstance().getPrefs().setIsLogin(true);

                        Intent i = new Intent(mContext, HomeActivity.class);
                        mContext.startActivity(i);
//                        finish();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("","JSonException docallApi Exception "+e);

                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.no_data_available),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("","Google docallApi Error: volly Exception " + error.getMessage());
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                mContext.startActivity(i);
//                finish();
                KalravApplication.getInstance().getPrefs().hidepDialog(getApplicationContext());
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        KalravApplication.getInstance().addToRequestQueue(req);

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

    // update user
    public void updateUser(String Id, final String realname, final String username,final String auth_id,final String phone,
                        final String address,final String city,final String state,final String zipcode){
        int id=Integer.parseInt(Id);
        String put_update_user_url=properties.getProperty(Constants.API_URL_USER.put_update_user_url.name())+id;

        Log.d("realname","put_update_user_url   "  +put_update_user_url);
        KalravApplication.getInstance().getPrefs().showDialog(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            String firstname,lastname,device;

//            params.put("id", Integer.parseInt(Id));
            params.put("realname", realname);
            String[] splited = null;

            Log.d("realname","realname   "  +realname);

            if (realname != null  && realname.contains(" ")) {
                splited =realname.split(" ");
                firstname = splited[0] == null ? "" : splited[0];
                lastname = splited[1] == null ? "" : splited[1];
            } else {
                firstname = "";
                lastname = "";
            }
            params.put("username", username);
            if(auth_id!=null && auth_id.length()>0){
                params.put("auth_id", auth_id);
            }
            else{
                params.put("auth_id", "auth_id");
            }
            params.put("phone", phone);
            params.put("address", address);
            params.put( "city" ,city);
            params.put("state", state);
            params.put("zipcode", zipcode);
            params.put("provenance", "Google");
            params.put("role", "GUEST");
            params.put("group", "NONE");
            params.put("notification", String.valueOf(false));
            params.put("enabled", String.valueOf(false));
            device=KalravApplication.getInstance().getPrefs().getDevice_token();
            if(device!=null && device.length()>0){
                params.put("device", device);
            }
            else{
                params.put("device", "device");
            }
            params.put("firstname", firstname);
            params.put("lastname", lastname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT,
                put_update_user_url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response","Rest response " +response);
                        try{
                            KalravApplication.getInstance().getPrefs().hidepDialog(getApplicationContext());
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));

                            UserInfo userInfo = new UserInfo();

                            userInfo.setId(jsonObject.getInt("id"));

                            userInfo.setName(jsonObject.getString("realname"));

                            userInfo.setEmail(jsonObject.getString("username"));

//                            if (fb_id != null)
//                                userInfo.setFb_id(fb_id);
//
//                            if (google_id != null)
//                                userInfo.setGoogle_id(google_id);

                            userInfo.setPhoneNo(jsonObject.getString("phone"));

                            KalravApplication.getInstance().getPrefs().setMobile(userInfo.getPhoneNo());
                            userInfo.setAddress(jsonObject.getString("address"));

                            userInfo.setCity(jsonObject.getString("city"));

                            userInfo.setState(jsonObject.getString("state"));

                            userInfo.setZipcode(jsonObject.getString("zipcode"));

                            userInfo.setGroupname(jsonObject.getString("group"));

                            userInfo.setRole(jsonObject.getString("role"));

                            userInfo.setDevice(jsonObject.getString("device"));

                            userInfo.setProvenance(jsonObject.getString("provenance"));

                            userInfo.setEnabled(jsonObject.getBoolean("enabled"));

                            userInfo.setNotification(jsonObject.getBoolean("notification"));


                            userInfo.setLoggedIn(Constants.LOG_IN);
//                        //save to the database
                            userDaoImpl.addUser(userInfo);

//                          setting customer id
                            KalravApplication.getInstance().getPrefs().setCustomer_id(String.valueOf(userInfo.getId()));
                            KalravApplication.getInstance().getPrefs().setUser_Group_Name(userInfo.getGroupname());
                            KalravApplication.getInstance().getPrefs().setEmail(userInfo.getEmail());

                            KalravApplication.getInstance().setGlobalUserObject(userInfo);

                            Log.d("", "Global object Reg " + KalravApplication.getInstance().getGlobalUserObject());
                            KalravApplication.getInstance().getPrefs().setIsLogin(true);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                        //  YOUR RESPONSE
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d("Response","Rest volleyError" +volleyError);

                String json = null;
                String str=null;
                byte[] response=null;
                if(volleyError.networkResponse.data!=null)
                    response = volleyError.networkResponse.data;
                Log.d("Response","Rest volleyError response " +response);
                try {
                    if (response != null && response.length > 0) {
                        str = new String(response, "UTF-8");
                        Log.d("Response", "Rest volleyError str toString  " + str.toString());

                        try {
                            JSONObject object = new JSONObject(str.toString());
                            Log.d("Response", "Rest inside object  " + object);

                            int statusCode = object.getInt("statusCode");
                            Log.d("Response", "Rest inside statusCode  " + statusCode);

                            if (statusCode == 400) {
                                String msg = object.getString("msg");
                                displayMessage(msg);
                            } else if (statusCode == 406) {
                                String msg = object.getString("msg");
                                displayMessage(msg);
                            } else if (statusCode == 404) {
                                String msg = object.getString("msg");
                                displayMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                KalravApplication.getInstance().getPrefs().hidepDialog(getApplicationContext());

            }

            //Somewhere that has access to a context
            public void displayMessage(String toastString){
                Log.d("Response","Rest volleyError toastString  " +toastString );

                Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_LONG).show();
            }


        });
        KalravApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }

}
