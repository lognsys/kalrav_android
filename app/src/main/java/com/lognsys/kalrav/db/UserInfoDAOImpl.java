package com.lognsys.kalrav.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lognsys.kalrav.model.UserInfo;

/**
 * Created by pdoshi on 02/01/17.
 */

//TODO: Add additional fields to user table when adding form information

public class UserInfoDAOImpl implements UserInfoDAO {

    private final String TAG = getClass().getSimpleName();
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;


    public UserInfoDAOImpl(Context context) {

        sqLiteHelper = new SQLiteHelper(context);

    }

    @Override
    public void addUser(UserInfo user) {
        Log.d(TAG, "adduser dao method called... " + user.toString());
        db = sqLiteHelper.getWritableDatabase();
        Log.d(TAG, "adduser dao method called isUserExist(user)... " +isUserExist(user));

        // If user exists update user
        if (isUserExist(user)) {

            Log.d(TAG, "Updating User - " + user.toString());
            updateUserInfo(user);

        } else {  //Create user if new

            Log.d(TAG, "Adding New User - " + user.toString());
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_USER_FBID, user.getFb_id());
            values.put(SQLiteHelper.COLUMN_USER_EMAIL, user.getEmail());
            values.put(SQLiteHelper.COLUMN_USER_NAME, user.getName());
            values.put(SQLiteHelper.COLUMN_USER_BIRTHDAY, user.getBirthday());
            values.put(SQLiteHelper.COLUMN_USER_LOGGEDIN, user.getLoggedIn());
            values.put(SQLiteHelper.COLUMN_USER_LOCATION, user.getLocation());

            // Inserting Row
            db.insert(SQLiteHelper.TABLE_USER, null, values);
            db.close(); // Closing database connection
        }
    }

    /**
     * @param userArg
     * @return
     */
    @Override
    public UserInfo findUserBy(UserInfo userArg) {

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        UserInfo user = null;

        Cursor c = db.rawQuery("SELECT " + SQLiteHelper.COLUMN_USER_FBID + "," +
                SQLiteHelper.COLUMN_USER_EMAIL + "," +
                SQLiteHelper.COLUMN_USER_NAME + "," +
                SQLiteHelper.COLUMN_USER_BIRTHDAY + "," +
                SQLiteHelper.COLUMN_USER_LOGGEDIN + "," +
                SQLiteHelper.COLUMN_USER_LOCATION +
                " FROM user where email=? ", new String[]{userArg.getEmail()});

        if (c != null && c.moveToFirst()) {
//            do {
            //assing values

            user =  new UserInfo();
            String fb_id = c.getString(0);
            String email = c.getString(1);
            String name = c.getString(2);
            String birthday = c.getString(3);
            int loggedIn = c.getInt(4);
            String location = c.getString(5);

            user.setFb_id(fb_id);
            user.setEmail(email);
            user.setName(name);
            user.setBirthday(birthday);
            user.setLoggedIn(loggedIn);
            user.setLocation(location);


            //Do something Here with values
//            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return user;
    }


    /**
     * Check if user exists
     *
     * @param user
     * @return
     */
    @Override
    public boolean isUserExist(UserInfo user) {

        db = sqLiteHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM USER", null);
        if (cur != null) {
            cur.moveToFirst();                       // Always one row returned.
            if (cur.getInt(0) == 0) {               // Zero count means empty table.
                Log.d(TAG, "DB_isUserExists - NOT FOUND...");
                return false;
            }
        }

        if (findUserBy(user) == null) {
            Log.d(TAG, "DB_isUserExists - NOT FOUND...");
            return false;
        }

        return true;

    }


    /**
     * Update User by emailId
     *
     * @param user
     * @return
     */
    @Override
    public int updateUserInfo(UserInfo user) {

        Log.d(TAG, "Update User - " );

        db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_USER_FBID, user.getFb_id());
        values.put(SQLiteHelper.COLUMN_USER_EMAIL, user.getEmail());
        values.put(SQLiteHelper.COLUMN_USER_NAME, user.getName());
        values.put(SQLiteHelper.COLUMN_USER_BIRTHDAY, user.getBirthday());
        values.put(SQLiteHelper.COLUMN_USER_LOGGEDIN, user.getLoggedIn());
        values.put(SQLiteHelper.COLUMN_USER_LOCATION, user.getLocation());

        Log.d(TAG, "DB_updateUserInfo - Updating User... - "+ user.getEmail());
        // updating row
        return db.update(SQLiteHelper.TABLE_USER, values, SQLiteHelper.COLUMN_USER_EMAIL + " = ?",
                new String[]{String.valueOf(user.getEmail())});

    }

    //TODO : create sqlite query to delete user
    @Override
    public boolean deleteUser(UserInfo userInfo) {
        return false;
    }


    /**
     * @param user
     * @return
     */
    public boolean isLoggedIn(UserInfo user) {

        boolean isLoggedIn = false;

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        int loggedIn = 0;

        Cursor c = db.rawQuery("SELECT " + SQLiteHelper.COLUMN_USER_LOGGEDIN + "," +
                " FROM user where email=? ", new String[]{user.getEmail()});

        if (c != null && c.moveToFirst()) {
            loggedIn = c.getInt(0);
        }

        c.close();
        db.close();

        isLoggedIn = loggedIn == 1 ? true : false;



        return isLoggedIn;
    }

    /**
     * @return
     */
    @Override
    public UserInfo lastUserLoggedIn() {

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        UserInfo user = null;

        Cursor c = db.rawQuery("SELECT " + SQLiteHelper.COLUMN_USER_FBID + "," +
                SQLiteHelper.COLUMN_USER_EMAIL + "," +
                SQLiteHelper.COLUMN_USER_NAME + "," +
                SQLiteHelper.COLUMN_USER_BIRTHDAY + "," +
                SQLiteHelper.COLUMN_USER_LOGGEDIN + "," +
                SQLiteHelper.COLUMN_USER_LOCATION +
                " FROM user where logged = 1 ORDER BY last_edit DESC ", null);

        if (c != null && c.moveToFirst()) {

            Log.d(TAG,"LOGGEDIN CURSOR - "+c.toString());
            user = new UserInfo();

            String fb_id = c.getString(0);
            String email = c.getString(1);
            String name = c.getString(2);
            String birthday = c.getString(3);
            int loggedIn = c.getInt(4);
            String location = c.getString(5);

            user.setFb_id(fb_id);
            user.setEmail(email);
            user.setName(name);
            user.setBirthday(birthday);
            user.setLoggedIn(loggedIn);
            user.setLocation(location);

        }

        return user;
    }

    @Override
    public int logOut(UserInfo user) {
        Log.d(TAG, "LogOut User - " + user.getEmail());

        db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_USER_LOGGEDIN, user.getLoggedIn());


        // updating row
        return db.update(SQLiteHelper.TABLE_USER, values, SQLiteHelper.COLUMN_USER_EMAIL + " = ?",
                new String[]{String.valueOf(user.getEmail())});
    }
}
