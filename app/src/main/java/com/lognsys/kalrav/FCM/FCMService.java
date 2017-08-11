package com.lognsys.kalrav.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lognsys.kalrav.HomeActivity;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.db.NotificationDAO;
import com.lognsys.kalrav.db.NotificationDAOImpl;
import com.lognsys.kalrav.model.NotificationInfo;
import com.lognsys.kalrav.util.KalravApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by admin on 3/29/2017.
 */

public class FCMService extends FirebaseMessagingService {
    NotificationDAOImpl notificationDAOImpl;
    NotificationInfo notificationInfo;

    static long when ;
     NotificationManager notificationManager;
    static PendingIntent resultPendingIntent;
    Intent notificationIntent=null;
    String notificationTitle = null, notificationBody = null, message=null;

    int dramaId;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("FCM","FCM remoteMessage "+remoteMessage);
        Log.d("FCM","FCM remoteMessage.getNotification().getBody() "+remoteMessage.getNotification().getBody());

        notificationDAOImpl=new NotificationDAOImpl(FCMService.this);
        try {
            JSONArray jsonArrayNotify=new JSONArray(remoteMessage.getNotification().getBody());
            Log.d("FCM","FCM remoteMessage jsonArrayNotify.len "+jsonArrayNotify.length());
//            [{"dramaId":2,"dramaTitle":"The Kite Runner","message":"New drama release","userId":26,"realname":"Monika Sharma"}]

            if(jsonArrayNotify!=null && jsonArrayNotify.length()>0){
                for(int i=0;i<jsonArrayNotify.length();i++){

                    JSONObject object=jsonArrayNotify.getJSONObject(i);
                    notificationInfo=new NotificationInfo();
                    int dramaId=object.getInt("dramaId");
                    notificationInfo.setDramaId(dramaId);

                    int userId=object.getInt("userId");
                    notificationInfo.setUserId(userId);

                    String dramaTitle=object.getString("dramaTitle");
                    notificationInfo.setDramaTitle(dramaTitle);
                    String realname=null;
                    if(object.getString("realname").length()>1)
                    {
                        realname=object.getString("realname");
                        notificationInfo.setRealname(realname);

                    }
                    else{
                        notificationInfo.setRealname(realname);

                    }

                    String message=object.getString("message");
                    notificationInfo.setMessage(message);

                    notificationDAOImpl.addNotificationInfo(notificationInfo);


                }
                generateNotification(notificationInfo);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("FCM","FCM remoteMessage JSONException "+e);

        }

/*
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
        notificationManager.notify(0,builder.build());*/
    }

    public void generateNotification(NotificationInfo notificationInfo) {
        notificationIntent = new Intent(this, HomeActivity.class);

        if(KalravApplication.getInstance().getPrefs().getIsLogin() &&  notificationInfo!=null){

            when = System.currentTimeMillis();


            if(notificationInfo.getMessage()!=null && notificationInfo.getDramaTitle()!=null ){

                message=notificationInfo.getMessage() +" "+ notificationInfo.getDramaTitle();
                Log.d("FCM","FCM generateNotification message "+message);

                dramaId = notificationInfo.getDramaId();
                Log.d("FCM","FCM generateNotification dramaId "+dramaId);
                boolean isNotification=true;
                int navItemIndex=5;

                notificationIntent.putExtra("id", dramaId);
                notificationIntent.putExtra("navItemIndex", navItemIndex);
                notificationIntent.putExtra("isNotification", isNotification);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            }
            else if(notificationInfo.getMessage()!=null && notificationInfo.getDramaTitle()!=null  && notificationInfo.getRealname().length()<1){

                message=notificationInfo.getMessage() +" "+ notificationInfo.getDramaTitle();
                Log.d("FCM","FCM generateNotification message "+message);

                dramaId = notificationInfo.getDramaId();
                Log.d("FCM","FCM generateNotification dramaId "+dramaId);
                boolean isNotification=true;
                int navItemIndex=5;

                notificationIntent.putExtra("id", dramaId);
                notificationIntent.putExtra("navItemIndex", navItemIndex);
                notificationIntent.putExtra("isNotification", isNotification);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            }
            else{

//                default notification
                message=notificationInfo.getMessage() ;
                Log.d("FCM","FCM generateNotification message "+message);
                  Log.d("FCM","FCM generateNotification dramaId "+dramaId);
                boolean isNotification=true;
                int navItemIndex=5;

                notificationIntent.putExtra("isNotification", isNotification);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            }

                // Use NotificationCompat.Builder to set up our notification.
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            String title = getApplicationContext().getString(R.string.app_name);

            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

            resultPendingIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_ONE_SHOT);
            Log.d("FCM","FCM generateNotification message ========================================== "+message);

        /*    Notification notification = mBuilder
                    .setSmallIcon(R.drawable.kalrav_logo).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setWhen(when)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))

                    .setContentIntent(resultPendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.kalrav_logo));
            notificationManager.notify((int)when, notification .build());
*/

            NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
            builder.setContentTitle(title);
            builder.setContentText(message);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.drawable.kalrav_logo); // mipmap  is small  icon type
            builder.setContentIntent(resultPendingIntent);
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            builder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.kalrav_logo));
            NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,builder.build());
        }
    }
}
