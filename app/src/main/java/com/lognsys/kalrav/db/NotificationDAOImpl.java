package com.lognsys.kalrav.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lognsys.kalrav.model.NotificationInfo;
import com.lognsys.kalrav.model.UserInfo;
import com.lognsys.kalrav.util.KalravApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 7/18/2017.
 */

public class NotificationDAOImpl implements NotificationDAO {

    private final String TAG = getClass().getSimpleName();
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;
    public NotificationDAOImpl(Context context) {

        sqLiteHelper = new SQLiteHelper(context);
        db = sqLiteHelper.getWritableDatabase();
    }
    @Override
    public int addNotificationInfo(NotificationInfo notificationInfo) {
        db = sqLiteHelper.getWritableDatabase();
        Log.d(TAG, "REST addNotificationInfo method called... " + notificationInfo.toString());

         //Create notificationInfo

            Log.d(TAG, "Rest Adding New NotificationInfo - " + notificationInfo.toString());
            ContentValues values = new ContentValues();
            values.put(NotificationInfo.COLUMN_NOTIFICATION_DRAMA_ID, notificationInfo.getDramaId());
            values.put(NotificationInfo.COLUMN_NOTIFICATION_USER_ID, notificationInfo.getUserId());
            values.put(NotificationInfo.COLUMN_NOTIFICATION_USER_REALNAME, notificationInfo.getRealname());
            values.put(NotificationInfo.COLUMN_NOTIFICATION_DRAMA_TITLE, notificationInfo.getDramaTitle());
            values.put(NotificationInfo.COLUMN_NOTIFICATION_MESSAGE, notificationInfo.getMessage());
            values.put(NotificationInfo.COLUMN_NOTIFICATION_CONFIRMATION_CODE, notificationInfo.getConfirmationCode());
            values.put(NotificationInfo.COLUMN_NOTIFICATION_BOOKING_ID, notificationInfo.getBookingId());
            values.put(NotificationInfo.COLUMN_TIMESTAMP, KalravApplication.getInstance().getCurrentDate());
            // Inserting Row
          int id= (int) db.insert(NotificationInfo.TABLE_NOTIFICATION, null, values);
        return id;
//            db.close(); // Closing database connection

    }

    @Override
    public ArrayList<NotificationInfo> getAllNotificationInfo() {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        List<NotificationInfo>  infos=new ArrayList<NotificationInfo>();
        NotificationInfo notificationInfo = null;

        Cursor c = db.rawQuery("SELECT " + NotificationInfo.COLUMN_ID + "," +
                NotificationInfo.COLUMN_NOTIFICATION_DRAMA_ID + "," +
                NotificationInfo.COLUMN_NOTIFICATION_USER_ID + "," +
                NotificationInfo.COLUMN_NOTIFICATION_USER_REALNAME + "," +
                NotificationInfo.COLUMN_NOTIFICATION_DRAMA_TITLE +"," +
                NotificationInfo.COLUMN_NOTIFICATION_MESSAGE +"," +
                NotificationInfo.COLUMN_NOTIFICATION_CONFIRMATION_CODE +"," +
                NotificationInfo.COLUMN_NOTIFICATION_BOOKING_ID +"," +
                NotificationInfo.COLUMN_TIMESTAMP +
                " FROM notification", null);
        if(c!=null)
            Log.d(TAG,"Rest getAllNotificationInfo getCount - "+c.getCount());

        if (c != null && c.getCount()>0 ) {
            //assing values
            while (c.moveToNext()) {
                notificationInfo = new NotificationInfo();
                int id = c.getInt(0);
                notificationInfo.set_id(id);

                int drama_id = c.getInt(1);
                notificationInfo.setDramaId(drama_id);

                int user_id = c.getInt(2);
                notificationInfo.setUserId(user_id);

                String realName = c.getString(3);
                notificationInfo.setRealname(realName);

                String dramaTitle = c.getString(4);
                notificationInfo.setDramaTitle(dramaTitle);

                String message = c.getString(5);
                notificationInfo.setMessage(message);

                String confirmationCode = c.getString(6);
                notificationInfo.setConfirmationCode(confirmationCode);

                int bookingId = c.getInt(7);
                notificationInfo.setBookingId(bookingId);

                String last_edit = c.getString(8);
                notificationInfo.setLast_edit(last_edit);
                infos.add(notificationInfo);

            }
        }
        return (ArrayList<NotificationInfo>) infos;

    }

    @Override
    public int deleteNotificationInfo() {
//        DELETE
//        FROM LoginTime
//        WHERE user_id = 1
//        ORDER BY datetime ASC
//        LIMIT 5
        int count;
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        NotificationInfo notificationInfo = null;
//        DELETE FROM notification WHERE dateEntered < DATE_SUB(NOW(), INTERVAL 3 MONTH);
//        Cursor c = db.rawQuery("DELETE FROM notification LIMIT 15", null);

        Cursor c = db.rawQuery("DELETE FROM notification LIMIT 10", null);
        if(c!=null)
            Log.d(TAG,"Rest getAllNotificationInfo getCount - "+c.getCount());

        count=+c.getCount();
        if (c != null && c.getCount()>0 ) {

        return c.getCount();
        }
        return count;
    }

    @Override
    public int deleteNotificationInfoById(int id) {
        int count;
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        NotificationInfo notificationInfo = null;

        Cursor c = db.rawQuery("DELETE FROM notification where id = "+id, null);
        if(c!=null)
            Log.d(TAG,"Rest deleteNotificationInfoById getCount - "+c.getCount());

        count=+c.getCount();
        if (c != null && c.getCount()>0 ) {

            return c.getCount();
        }
        return count;
    }

    @Override
    public int countNotificationAfterDelete() {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        List<NotificationInfo>  infos=new ArrayList<NotificationInfo>();
        NotificationInfo notificationInfo = null;

        Cursor c = db.rawQuery("SELECT * FROM notification", null);
        if(c!=null)
            Log.d(TAG,"Rest getAllNotificationInfo getCount - "+c.getCount());

        if (c != null && c.getCount()>0 ) {
            return c.getCount();
        }
        return 0;
    }
}