package com.lognsys.kalrav.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.lognsys.kalrav.util.KalravApplication;

import java.util.Timer;

/**
 * Created by admin on 5/20/2017.
 */

public class KalravDramaByGroupService extends Service {
    private final int UPDATE_INTERVAL = 60 * 1000;
    private Timer timer = new Timer();


    @Override
    public void onCreate() {
        // Code to execute when the service is first created
        Log.d("Service"," Service onCreate ");
//
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }

        Log.d("Service"," Service onDestroy ");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        if(KalravApplication.getInstance().getPrefs().getCustomer_id()!=null){
            Log.d("Service"," Service onStartCommand ");
//            getDramaGroup();
        }
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Service"," Service onBind ");
        //TODO for communication return IBinder implementation
        return null;
    }


}