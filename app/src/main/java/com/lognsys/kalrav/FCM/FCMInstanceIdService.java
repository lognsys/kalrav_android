package com.lognsys.kalrav.FCM;

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

    CallAPI callAPI;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String recentToken= FirebaseInstanceId.getInstance().getToken();
        REG_TOKEN=recentToken;
        Log.d("FCM","FCM REG_TOKEN===================="+REG_TOKEN);
        KalravApplication.getInstance().getPrefs().setDevice_token(REG_TOKEN);
        propertyReader = new PropertyReader(getApplicationContext());
        properties = propertyReader.getMyProperties(PROPERTIES_FILENAME);


        String sendREGTOKENURL=properties.getProperty(Constants.API_URL_USER.sendDeviceToken.name());

        Log.d("FCM","FCM sendREGTOKENURL===================="+sendREGTOKENURL);

        callAPI=new CallAPI(FCMInstanceIdService.this);
        callAPI.sendDeviceToken(KalravApplication.getInstance().getPrefs().getDevice_token(),sendREGTOKENURL);
    }
}
