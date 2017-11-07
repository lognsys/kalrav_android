package com.lognsys.kalrav.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.lognsys.kalrav.FCM.FCMInstanceIdService;
import com.lognsys.kalrav.LoginActivity;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.fragment.DramaFragment;
import com.lognsys.kalrav.model.SeatsDetailInfo;
import com.lognsys.kalrav.model.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import static android.app.PendingIntent.getActivity;

/**
 * Created by pdoshi on 03/01/17.
 */

public class KalravApplication extends Application {
    public static final String TAG = KalravApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private UserInfo userInfo;
    SeatsDetailInfo seatsDetailInfo;
    Preference prefs;
    private static KalravApplication mInstance;
    // Progress dialog
    private ProgressDialog pDialog;

    ConnectivityManager cm;
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        prefs = new Preference(this);
         cm =  (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    DrawerLayout drawerLayout;
    public void setDrawer(DrawerLayout drawerLayout){
        this.drawerLayout=drawerLayout;
    }

    public DrawerLayout getDrawerLayout() {
        return this.drawerLayout;
    }

    public void setToolBar(){}

    public Toolbar getToolbBar() {
        return null;
    }

    public Preference getPrefs() {
        return prefs;
    }

    public static synchronized KalravApplication getInstance() {
        return mInstance;
    }

    //    we initialize all the volley core objects
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    public UserInfo getGlobalUserObject() {
        return this.userInfo;
    }

    public void setGlobalUserObject(UserInfo userInfo) {

        this.userInfo = userInfo;

    }
    public SeatsDetailInfo getGlobalSeatsDetailInfo() {
        return this.seatsDetailInfo;
    }

    public void setGlobalSeatsDetailInfo(SeatsDetailInfo seatsDetailInfo) {

        this.seatsDetailInfo = seatsDetailInfo;

    }
    public boolean isConnectedToInternet() {

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    return isConnected;
    }
    public static boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
                try {
                    URL url = new URL("http://kalravapi.lognsys.com:8080/kalravweb/getalldramaandgroup");
                    HttpURLConnection urlc = (HttpURLConnection) url
                            .openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
        return false;
    }
public void showDialog(final Context context, String message){

    Log.d("Response","alReadyExsistUser volleyError context  " +context+ " message ==="+message );
    AlertDialog alertDialog = new AlertDialog.Builder(
            context).create();
    // Setting Dialog Title
    alertDialog.setTitle("Kalrav");

    // Setting Dialog Message
    alertDialog.setMessage(message);

    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            boolean isServerDown=true;
        Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra("isServerDown",isServerDown);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Log.d("Response","alReadyExsistUser volleyError isServerDown  " +isServerDown );

            context.startActivity(intent);
        }
    });
    // Showing Alert Message
    alertDialog.show();
    }



    @SuppressLint("ResourceAsColor")
    public AlertDialog.Builder buildDialog(Context c) {

        @SuppressLint("RestrictedApi") AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(c, null));
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");
        AlertDialog dialog = builder.create();
        Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        if(b != null) {
            b.setTextColor(R.color.colorPrimary);
            b.setTextSize(12.5f);
        }
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return builder;
    }

    public String getCurrentDate()
    {
        try{

            // (1) get today's date
            Date today =new Date();
            Log.d("","TODAY"+today);
           // (2) create a date "formatter" (the date format we want)
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Log.d("","TODAY formatter"+formatter);

            // (3) create a new String using the date format we want
            String date  = formatter.format(today);
            Log.d("","TODAY date"+date);

            return date;
        }catch (Exception e){
            Log.d("","TODAY       e "+ e);

            return  e.toString();

        }
      }
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
    public static class NetCheck extends AsyncTask<String, Void, Boolean> {
        String exsistUserUrl;
        String email;
        UserInfo userInfo;
        String fb_id;
        String google_id;
        String seatAuth;
        Activity activity;

        public NetCheck(String email, UserInfo userInfo, String fb_id, String google_id, String seatAuth,Activity activity) {
            this.email=email;
            this.userInfo=userInfo;
            this.fb_id=fb_id;
            this.google_id=google_id;
            this.seatAuth=seatAuth;
            this.activity=activity;
            }

        @Override
        protected Boolean doInBackground(String... args) {
            // get Internet status
            Log.d(TAG, "Rest Google NetCheck ");

            exsistUserUrl=args[0];
            Log.d(TAG, "Rest Google NetCheck exsistUserUrl "+exsistUserUrl);
            Log.d(TAG, "Rest Google NetCheck isConnectingToInternet() "+isConnectingToInternet());

            return isConnectingToInternet();
        }

        protected void onPostExecute(Boolean th) {
            Log.d(TAG, "Rest Google NetCheck onPostExecute th() "+th);

            if (th == true) {
                CallAPI callAPI=new CallAPI(KalravApplication.getInstance());
                callAPI.alReadyExsistUser(this.userInfo, this.fb_id, this.google_id,this.exsistUserUrl,this.seatAuth, this.activity);
            } else {
                KalravApplication.getInstance().showDialog(this.activity,getInstance().getString(R.string.unknown_error));
            }
        }
    }
}
