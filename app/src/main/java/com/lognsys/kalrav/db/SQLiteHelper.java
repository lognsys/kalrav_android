package com.lognsys.kalrav.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIMESTAMP = "last_edit";

    //user table
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PROFILELINK = "profile_link";
    public static final String COLUMN_USER_BIRTHDAY = "birthday";
    public static final String COLUMN_USER_LOGGEDIN = "logged";
    public static final String COLUMN_USER_LOCATION = "location";
    public static final String COLUMN_USER_FBID = "fb_id";

    private static final String DATABASE_CREATE_USER = "create table if not exists "
            + TABLE_USER + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_USER_FBID + " TEXT, "
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_EMAIL + " TEXT, "
            + COLUMN_USER_BIRTHDAY + " TEXT, "
            + COLUMN_USER_LOCATION + " TEXT, "
            + COLUMN_USER_LOGGEDIN + " BOOLEAN NOT NULL CHECK ("+COLUMN_USER_LOGGEDIN+" IN (0,1)), "
            + COLUMN_TIMESTAMP+ " DEFAULT CURRENT_TIMESTAMP NOT NULL);";

    //Drama Column
    public static final String TABLE_DRAMA = "drama";
    public static final String COLUMN_GROUP_NAME = "group_name";
    public static final String COLUMN_DRAMA_NAME = "drama_name";
    public static final String COLUMN_LINK_PHOTO = "photo_link";
    public static final String COLUMN_DATETIME = "datetime";

    //drama table
    private static final String DATABASE_CREATE_DRAMA = "create table if not exists "
            + TABLE_DRAMA + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_GROUP_NAME + " TEXT, "
            + COLUMN_DRAMA_NAME + " TEXT, "
            + COLUMN_LINK_PHOTO + " TEXT, "
            + COLUMN_DATETIME + " TEXT, "
            + COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP NOT NULL);";


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "Creating database "+DATABASE_NAME+" ...");

        db.execSQL(DATABASE_CREATE_USER);
        db.execSQL(DATABASE_CREATE_DRAMA);

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


