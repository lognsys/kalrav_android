package com.lognsys.kalrav.FCM;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by admin on 3/29/2017.
 */

public class FCMInstanceIdService extends FirebaseInstanceIdService {
private static String REG_TOKEN="REG_TOKEN";

//    each  time  when new token is created the system
//    will  call onTokenRefresh()


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String recentToken= FirebaseInstanceId.getInstance().getToken();
        REG_TOKEN=recentToken;
        Log.d("FCM","FCM REG_TOKEN "+REG_TOKEN);
    }
}
