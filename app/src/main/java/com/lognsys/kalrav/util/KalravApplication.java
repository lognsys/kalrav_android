package com.lognsys.kalrav.util;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.model.UserInfo;
import com.lognsys.kalrav.service.KalravDramaByGroupService;

/**
 * Created by pdoshi on 03/01/17.
 */

public class KalravApplication extends Application {
    public static final String TAG = KalravApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private UserInfo userInfo;
    Preference prefs;
    private static KalravApplication mInstance;
    // Progress dialog
    private ProgressDialog pDialog;

    ConnectivityManager cm;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        prefs = new Preference(this);
         cm =  (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

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

    public boolean isConnectedToInternet() {

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    return isConnected;
    }
public  void invokeService(Context context){
    // use this to start and trigger a service
    Log.d(TAG, "Service OnCreate invokeService context- " +context);

    Intent i= new Intent(context, KalravDramaByGroupService.class);
    context.startService(i);
}
public void showDialog(Context context, String message){
    AlertDialog alertDialog = new AlertDialog.Builder(
            context).create();

    // Setting Dialog Title
    alertDialog.setTitle("Kalrav");

    // Setting Dialog Message
    alertDialog.setMessage(message);

    // Setting Icon to Dialog
    alertDialog.setIcon(R.drawable.kalrav_logo);

    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    });


    // Showing Alert Message
    alertDialog.show();
}
}
