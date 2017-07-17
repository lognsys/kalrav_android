package com.lognsys.kalrav.FCM;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.lognsys.kalrav.util.CallAPI;
import com.lognsys.kalrav.util.Constants;
import com.lognsys.kalrav.util.KalravApplication;
import com.lognsys.kalrav.util.PropertyReader;

import java.util.Properties;

/**
 * Created by admin on 3/29/2017.
 */

public class FCMInstanceIdService extends FirebaseInstanceIdService {
private static String REG_TOKEN="REG_TOKEN";

//    each  time  when new token is created the system
//    will  call onTokenRefresh()
//Properties
    private PropertyReader propertyReader;
    private Properties properties;
    public static final String PROPERTIES_FILENAME = "kalrav_android.properties";
    public static final String TAG = "FCMInstanceIdService";


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String recentToken= FirebaseInstanceId.getInstance().getToken();
        REG_TOKEN=recentToken;

        KalravApplication.getInstance().getPrefs().setDevice_token(REG_TOKEN);
        propertyReader = new PropertyReader(getApplicationContext());
        properties = propertyReader.getMyProperties(PROPERTIES_FILENAME);


        String sendREGTOKENURL=properties.getProperty(Constants.API_URL_USER.sendDeviceToken.name());
        Log.d(TAG, "Rest onTokenRefresh getDevice_token- " +KalravApplication.getInstance().getPrefs().getDevice_token());

//        CallAPI.sendDeviceToken(KalravApplication.getInstance().getPrefs().getDevice_token(),sendREGTOKENURL);
    }
}
