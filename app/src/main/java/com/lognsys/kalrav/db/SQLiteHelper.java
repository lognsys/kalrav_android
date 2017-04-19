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
    public static final String COLUMN_USER_GROUPNAME = "groupname";
    public static final String COLUMN_USER_PHONENO = "phoneno";
    public static final String COLUMN_USER_GOOGLEID = "google_id";

    private static final String DATABASE_CREATE_USER = "create table if not exists "
            + TABLE_USER + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_USER_FBID + " TEXT, "
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_EMAIL + " TEXT, "
            + COLUMN_USER_BIRTHDAY + " TEXT, "
            + COLUMN_USER_LOCATION + " TEXT, "
            + COLUMN_USER_GROUPNAME + " TEXT, "
            + COLUMN_USER_PHONENO + " TEXT, "
            + COLUMN_USER_GOOGLEID + " TEXT, "
            + COLUMN_USER_LOGGEDIN + " BOOLEAN NOT NULL CHECK ("+COLUMN_USER_LOGGEDIN+" IN (0,1)), "
            + COLUMN_TIMESTAMP+ " DEFAULT CURRENT_TIMESTAMP NOT NULL);";

    //Drama Column
    public static final String TABLE_DRAMA = "drama";
    public static final String COLUMN_GROUP_NAME = "group_name";
    public static final String COLUMN_DRAMA_NAME = "drama_name";
    public static final String COLUMN_LINK_PHOTO = "photo_link";
    public static final String COLUMN_DATETIME = "datetime";
    public static final String COLUMN_DRAMA_LENGTH = "drama_length";
    public static final String COLUMN_DRAMA_LANGUAGE = "drama_language";
    public static final String COLUMN_DRAMA_GENRE = "drama_genre";
    public static final String COLUMN_DRAMA_TIME = "time";
    public static final String COLUMN_DRAMA_DESCRIPTION = "briefDescription";
    //drama table
    private static final String DATABASE_CREATE_DRAMA = "create table if not exists "
            + TABLE_DRAMA + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_GROUP_NAME + " TEXT, "
            + COLUMN_DRAMA_NAME + " TEXT, "
            + COLUMN_LINK_PHOTO + " TEXT, "
            + COLUMN_DATETIME + " TEXT, "
            + COLUMN_DRAMA_LENGTH + " TEXT, "
            + COLUMN_DRAMA_LANGUAGE + " TEXT, "
            + COLUMN_DRAMA_GENRE + " TEXT, "
            + COLUMN_DRAMA_TIME + " TEXT, "
            + COLUMN_DRAMA_DESCRIPTION + " TEXT, "
            + COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP NOT NULL);";



    //favourite Column
    public static final String TABLE_FAVOURITE = "favourite";
//    public static final String COLUMN_GROUP_NAME = "group_name";
    public static final String COLUMN_FAVOURITE_ID = "_id";
    public static final String COLUMN_DRAMA_ID = "drama_id";
    /* public static final String COLUMN_DRAMA_NAME = "drama_name";
     public static final String COLUMN_LINK_PHOTO = "photo_link";
     public static final String COLUMN_DATETIME = "datetime";
     public static final String COLUMN_DRAMA_LENGTH = "drama_length";
     public static final String COLUMN_DRAMA_LANGUAGE = "drama_language";
     public static final String COLUMN_DRAMA_GENRE = "drama_genre";
     public static final String COLUMN_DRAMA_TIME = "time";
     public static final String COLUMN_DRAMA_DESCRIPTION = "briefDescription";*/
    //drama table
    private static final String DATABASE_CREATE_FAVOURITE = "create table if not exists "
            + TABLE_FAVOURITE + "(" + COLUMN_FAVOURITE_ID + " integer primary key autoincrement, "
            + COLUMN_DRAMA_ID +  " integer, "/*
            + COLUMN_GROUP_NAME + " TEXT, "
            + COLUMN_DRAMA_NAME + " TEXT, "
            + COLUMN_LINK_PHOTO + " TEXT, "
            + COLUMN_DATETIME + " TEXT, "
            + COLUMN_DRAMA_LENGTH + " TEXT, "
            + COLUMN_DRAMA_LANGUAGE + " TEXT, "
            + COLUMN_DRAMA_GENRE + " TEXT, "
            + COLUMN_DRAMA_TIME + " TEXT, "
            + COLUMN_DRAMA_DESCRIPTION + " TEXT, "*/
            + COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP NOT NULL , "
            + " FOREIGN KEY ("+COLUMN_DRAMA_ID+") REFERENCES "+TABLE_DRAMA+"("+COLUMN_ID+"));";


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "Creating database "+DATABASE_NAME+" ...");

        db.execSQL(DATABASE_CREATE_USER);
        db.execSQL(DATABASE_CREATE_DRAMA);
        db.execSQL(DATABASE_CREATE_FAVOURITE);

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


