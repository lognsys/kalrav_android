package com.lognsys.kalrav.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lognsys.kalrav.model.BookingInfo;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.FavouritesInfo;
import com.lognsys.kalrav.model.NotificationInfo;
import com.lognsys.kalrav.model.UserInfo;

/**
 * Created by pdoshi on 27/12/16.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();

    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        Log.d(TAG, "Instantiating SQLIteHelper");
    }

    //database values
    private static final String DATABASE_NAME = "kalrav.db";
    private static final int DATABASE_VERSION = 1;
//    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIMESTAMP = "last_edit";

 /*   //user table
          "id": 21,
         "auth_id": "abcd786876bbnns",
         "username": "lognsystems@gmail.com",
         "realname": "Omkar Gude",
         "phone": "4987765678",
         "provenance": "",
         "birthdate": null,
         "enabled": true,
         "notification": false,
         "device": "",
         "address": "Fremont California",
         "city": "mumbai",
         "state": "maharashtra",
         "zipcode": "400064",
         "firstname": "Omkar",
         "lastname": "Gude",
         "role": "GROUP_USER",
         "group": "NONE"
    */
// USER TABLE CREATE
  public static final String TABLE_USER = "user";
    private static final String DATABASE_CREATE_USER = "create table if not exists "
            + UserInfo.TABLE_USER + "("
            + UserInfo.COLUMN_ID + " integer primary key , "
            + UserInfo.COLUMN_USER_FBID + " TEXT , "
            + UserInfo.COLUMN_USER_GOOGLEID + " TEXT , "
            + UserInfo.COLUMN_USER_NAME + " TEXT , "
            + UserInfo.COLUMN_USER_REALNAME+ " TEXT , "
            + UserInfo.COLUMN_USER_PHONENO + " TEXT , "
            + UserInfo.COLUMN_USER_PROVENANCE + " TEXT , "
            + UserInfo.COLUMN_USER_BIRTHDAY + " TEXT , "
            + UserInfo.COLUMN_USER_ENABLED + "  BOOLEAN ,  "
            + UserInfo.COLUMN_USER_IS_NOTIFICATION + "  BOOLEAN , "
            + UserInfo.COLUMN_USER_DEVICE_TOKEN + " TEXT , "
            + UserInfo.COLUMN_USER_ADDRESS + " TEXT , "
            + UserInfo.COLUMN_USER_CITY + " TEXT , "
            + UserInfo.COLUMN_USER_STATE + " TEXT , "
            + UserInfo.COLUMN_USER_ZIPCODE + " TEXT , "
            + UserInfo.COLUMN_USER_ROLE + " TEXT , "
            + UserInfo.COLUMN_USER_GROUP_NAME + " TEXT , "
            + UserInfo.COLUMN_USER_LOGGEDIN + " BOOLEAN NOT NULL CHECK ("+UserInfo.COLUMN_USER_LOGGEDIN+" IN (0,1)), "
            + COLUMN_TIMESTAMP+ " DEFAULT CURRENT_TIMESTAMP NOT NULL);";

    //drama table
    private static final String DATABASE_CREATE_DRAMA = "create table if not exists "
            + DramaInfo.TABLE_DRAMA + "("
            + DramaInfo.COLUMN_ID + " integer primary key autoincrement, "
            + DramaInfo.COLUMN_GROUP_NAME + " TEXT, "
            + DramaInfo.COLUMN_DRAMA_NAME + " TEXT, "
            + DramaInfo.COLUMN_LINK_PHOTO + " TEXT, "
            + DramaInfo.COLUMN_DATETIME + " TEXT, "
            + DramaInfo.COLUMN_DRAMA_LENGTH + " TEXT, "
            + DramaInfo.COLUMN_DRAMA_LANGUAGE + " TEXT, "
            + DramaInfo.COLUMN_DRAMA_GENRE + " TEXT, "
            + DramaInfo.COLUMN_DRAMA_TIME + " TEXT, "
            + DramaInfo.COLUMN_DRAMA_DESCRIPTION + " TEXT, "
            + DramaInfo.COLUMN_DRAMA_CAST + " TEXT, "
            + DramaInfo.COLUMN_DRAMA_WRITER + " TEXT, "
            + DramaInfo.COLUMN_DRAMA_DIRECTOR + " TEXT, "
            + DramaInfo.COLUMN_DRAMA_AVG_RATING + " TEXT, "
            + DramaInfo.COLUMN_DRAMA_MUSIC + " TEXT, "
            + DramaInfo.COLUMN_DRAMA_ISFAV + " TEXT, "
            + COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP NOT NULL);";



    //fav table
    private static final String DATABASE_CREATE_FAVOURITE = "create table if not exists "
            + FavouritesInfo.TABLE_FAVOURITE + "(" + FavouritesInfo.COLUMN_FAVOURITE_ID + " integer primary key autoincrement, "
            + FavouritesInfo.COLUMN_DRAMA_ID +  " integer, "
            + FavouritesInfo.COLUMN_ISFAV + " TEXT, "
            + COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP NOT NULL , "
            + " FOREIGN KEY ("+FavouritesInfo.COLUMN_DRAMA_ID+") REFERENCES "+DramaInfo.TABLE_DRAMA+"("+DramaInfo.COLUMN_ID+"));";





    //drama table
    private static final String DATABASE_CREATE_TICKET = "create table if not exists "
            + BookingInfo.TABLE_TICKET + "("
            + BookingInfo.COLUMN_ID + " integer primary key autoincrement, "
            + BookingInfo.COLUMN_DRAMA_ID +  " integer, "
            + BookingInfo.COLUMN_USER_ID +  " integer, "
            + BookingInfo.COLUMN_DRAMA_NAME + " TEXT, "
            + BookingInfo.COLUMN_GROUP_NAME + " TEXT, "
            + BookingInfo.COLUMN_LINK_PHOTO + " TEXT, "
            + BookingInfo.COLUMN_DATETIME + " TEXT, "
            + BookingInfo.COLUMN_DRAMA_TIME + " TEXT, "
            + BookingInfo.COLUMN_BOOKED_TIME + " TEXT, "
            + BookingInfo.COLUMN_BOOKED_DATE + " TEXT, "
            + BookingInfo.COLUMN_CONFIRMATION_CODE + " TEXT, "
            + BookingInfo.COLUMN_SEAT_TOTAL_PRICE + " TEXT, "
            + BookingInfo.COLUMN_NO_OF_SEATS_BOOKED + " TEXT, "
            + BookingInfo.COLUMN_SEAT_NO + " TEXT, "
            + BookingInfo.COLUMN_AUDITORIUM_NAME + " TEXT, "
            + BookingInfo.COLUMN_USER_NAME + " TEXT, "
            + BookingInfo.COLUMN_USER_EMAIL_ID + " TEXT, "
            + BookingInfo.COLUMN_QRCODE_BITMAP + " TEXT, "
            + COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP NOT NULL);";


    // NOTIFICATION TABLE CREATE
    public static final String TABLE_NOTIFICATION = "notification";
    private static final String DATABASE_CREATE_NOTIFICATION = "create table if not exists "
            + NotificationInfo.TABLE_NOTIFICATION + "("
            + NotificationInfo.COLUMN_ID + " integer primary key autoincrement , "
            + NotificationInfo.COLUMN_NOTIFICATION_DRAMA_ID + " integer , "
            + NotificationInfo.COLUMN_NOTIFICATION_USER_ID + " integer , "
            + NotificationInfo.COLUMN_NOTIFICATION_USER_REALNAME + " TEXT , "
            + NotificationInfo.COLUMN_NOTIFICATION_DRAMA_TITLE + " TEXT , "
            + NotificationInfo.COLUMN_NOTIFICATION_MESSAGE + " TEXT , "
            + COLUMN_TIMESTAMP+ " DEFAULT CURRENT_TIMESTAMP NOT NULL);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "Creating database "+DATABASE_NAME+" ...");

        db.execSQL(DATABASE_CREATE_USER);
        db.execSQL(DATABASE_CREATE_DRAMA);
        db.execSQL(DATABASE_CREATE_FAVOURITE);
        db.execSQL(DATABASE_CREATE_TICKET);
        db.execSQL(DATABASE_CREATE_NOTIFICATION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion < 2) {
//            db.execSQL(DATABASE_ALTER_TEAM_1);
//        }
//        if (oldVersion < 3) {
//            db.execSQL(DATABASE_ALTER_TEAM_2);
//        }
    }
}


