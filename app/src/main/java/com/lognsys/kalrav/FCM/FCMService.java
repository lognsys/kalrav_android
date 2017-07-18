package com.lognsys.kalrav.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lognsys.kalrav.HomeActivity;
import com.lognsys.kalrav.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 3/29/2017.
 */

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("FCM","FCM remoteMessage "+remoteMessage);
        Log.d("FCM","FCM remoteMessage.getNotification().getBody() "+remoteMessage.getNotification().getBody());
        String notificationTitle = null, notificationBody = null;
        try {
            JSONArray jsonArrayNotify=new JSONArray(remoteMessage.getNotification().getBody());
            Log.d("FCM","FCM remoteMessage jsonArrayNotify.len "+jsonArrayNotify.length());
//            [{"dramaId":2,"dramaTitle":"The Kite Runner","message":"New drama release","userId":26,"realname":"Monika Sharma"}]

            if(jsonArrayNotify!=null && jsonArrayNotify.length()>0){
                for(int i=0;i<jsonArrayNotify.length();i++){

                    JSONObject object=jsonArrayNotify.getJSONObject(i);
                    int dramaId=object.getInt("dramaId");
                    int userId=object.getInt("userId");

                    if(dramaId>0 && userId>0){
                        String dramaTitle=object.getString("dramaTitle");
                        String realname=object.getString("realname");
                        if(realname!=null)
                            notificationTitle=realname;
                        else
                            notificationTitle=dramaTitle;

                        String message=object.getString("message");
                        if(dramaTitle!=null)
                            notificationBody=message+ " "+dramaTitle;
                        else
                            notificationBody=message;
                    }
                    else{
                        String message=object.getString("message");
                        notificationBody=message;
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("FCM","FCM remoteMessage JSONException "+e);

        }


        Intent intent=new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setContentTitle(notificationTitle);
        builder.setContentText(notificationBody);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.kalrav_logo); // mipmap  is small  icon type
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());
    }
}
