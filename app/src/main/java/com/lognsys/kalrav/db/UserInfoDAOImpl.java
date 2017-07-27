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
        Log.d(TAG, "Rest adduser dao method called isUserExist(user)... " +isUserExist(user));

        // If user exists update user
        if (isUserExist(user)) {

            Log.d(TAG, "Rest Updating User - " + user.toString());
            updateUserInfo(user);

        } else {  //Create user if new

            Log.d(TAG, "Rest Adding New User - " + user.toString());
            ContentValues values = new ContentValues();
            values.put(UserInfo.COLUMN_ID, user.getId());
            values.put(UserInfo.COLUMN_USER_FBID, user.getFb_id());
            values.put(UserInfo.COLUMN_USER_GOOGLEID, user.getGoogle_id());
            values.put(UserInfo.COLUMN_USER_NAME, user.getEmail());
            values.put(UserInfo.COLUMN_USER_REALNAME, user.getName());
            values.put(UserInfo.COLUMN_USER_PHONENO, user.getPhoneNo());
            values.put(UserInfo.COLUMN_USER_PROVENANCE, user.getGroupname());
            values.put(UserInfo.COLUMN_USER_BIRTHDAY, user.getBirthday());
            values.put(UserInfo.COLUMN_USER_ENABLED, user.isEnabled());
            values.put(UserInfo.COLUMN_USER_IS_NOTIFICATION, user.isNotification());
            values.put(UserInfo.COLUMN_USER_DEVICE_TOKEN, user.getDevice());
            values.put(UserInfo.COLUMN_USER_ADDRESS, user.getAddress());
            values.put(UserInfo.COLUMN_USER_CITY, user.getCity());
            values.put(UserInfo.COLUMN_USER_STATE, user.getState());
            values.put(UserInfo.COLUMN_USER_ZIPCODE, user.getZipcode());
            values.put(UserInfo.COLUMN_USER_ROLE, user.getRole());
            values.put(UserInfo.COLUMN_USER_GROUP_NAME, user.getGroupname());
            values.put(UserInfo.COLUMN_USER_LOGGEDIN, user.getLoggedIn());


            // Inserting Row
            db.insert(UserInfo.TABLE_USER, null, values);
//            db.close(); // Closing database connection
        }
    }

    @Override
    public UserInfo findUserByUsername(String username) {

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        UserInfo user = null;
        Log.d(TAG,"Rest  findUserByUsername username - "+username);

        Cursor c = db.rawQuery("SELECT " + UserInfo.COLUMN_ID + "," +
                UserInfo.COLUMN_USER_FBID + "," +
                UserInfo.COLUMN_USER_GOOGLEID  + "," +
                UserInfo.COLUMN_USER_REALNAME + "," +
                UserInfo.COLUMN_USER_PHONENO +"," +
                UserInfo.COLUMN_USER_PROVENANCE + "," +
                UserInfo.COLUMN_USER_BIRTHDAY + "," +
                UserInfo.COLUMN_USER_ENABLED + "," +
                UserInfo.COLUMN_USER_IS_NOTIFICATION + "," +
                UserInfo.COLUMN_USER_DEVICE_TOKEN + "," +
                UserInfo.COLUMN_USER_ADDRESS +"," +
                UserInfo.COLUMN_USER_CITY + "," +
                UserInfo.COLUMN_USER_STATE + "," +
                UserInfo.COLUMN_USER_ZIPCODE + "," +
                UserInfo.COLUMN_USER_ROLE + "," +
                UserInfo.COLUMN_USER_GROUP_NAME + "," +
                UserInfo.COLUMN_USER_LOGGEDIN +
                " FROM user where username =? ", new String[]{username});

        if(c!=null)
            Log.d(TAG,"Rest findUserByUsername getCount - "+c.getCount());

        if (c != null && c.getCount()>0) {
//            do {
            //assing values
            while (c.moveToFirst()){
                user =  new UserInfo();
                int id = c.getInt(0);
                String fb_id = c.getString(1);
                String google_id = c.getString(2);
                String realname = c.getString(3);
                String phone = c.getString(4);
                String provenance = c.getString(5);
                String birthday = c.getString(6);
                boolean enabled = new Boolean(c.getString(7));
                boolean notification = new Boolean( c.getString(8));
                String device = c.getString(9);
                String address = c.getString(10);
                String city = c.getString(11);
                String state = c.getString(12);
                String zipcode = c.getString(13);
                String role = c.getString(14);
                String group = c.getString(15);
                int loggedIn = c.getInt(16);

                user.setId(id);
                user.setFb_id(fb_id);
                user.setGoogle_id(google_id);
                user.setName(realname);
                user.setPhoneNo(phone);
                user.setProvenance(provenance);
                user.setBirthday(birthday);
                user.setEnabled(enabled);
                user.setNotification(notification);
                user.setDevice(device);
                user.setAddress(address);
                user.setCity(city);
                user.setState(state);
                user.setZipcode(zipcode);
                user.setRole(role);
                user.setGroupname(group);
                user.setLoggedIn(loggedIn);
            }

            return user;
        }

        c.close();
        db.close();

        return user;
    }


    /**
     * @param userArg
     * @return
     */
    @Override
    public UserInfo findUserBy(UserInfo userArg) {

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        UserInfo user = null;
        Log.d(TAG,"Rest  findUserBy userArg.getEmail() - "+userArg.getEmail());

        Cursor c = db.rawQuery("SELECT " + UserInfo.COLUMN_ID + "," +
                UserInfo.COLUMN_USER_FBID + "," +
                UserInfo.COLUMN_USER_GOOGLEID  + "," +
                UserInfo.COLUMN_USER_REALNAME + "," +
                UserInfo.COLUMN_USER_PHONENO +"," +
                UserInfo.COLUMN_USER_PROVENANCE + "," +
                UserInfo.COLUMN_USER_BIRTHDAY + "," +
                UserInfo.COLUMN_USER_ENABLED + "," +
                UserInfo.COLUMN_USER_IS_NOTIFICATION + "," +
                UserInfo.COLUMN_USER_DEVICE_TOKEN + "," +
                UserInfo.COLUMN_USER_ADDRESS +"," +
                UserInfo.COLUMN_USER_CITY + "," +
                UserInfo.COLUMN_USER_STATE + "," +
                UserInfo.COLUMN_USER_ZIPCODE + "," +
                UserInfo.COLUMN_USER_ROLE + "," +
                UserInfo.COLUMN_USER_GROUP_NAME + "," +
                UserInfo.COLUMN_USER_LOGGEDIN +
                " FROM user where username =? ", new String[]{userArg.getEmail()});

        if(c!=null)
            Log.d(TAG,"Rest findUserBy getCount - "+c.getCount());

        if (c != null && c.getCount()>0) {
//            do {
            //assing values
            while (c.moveToFirst()){
                user =  new UserInfo();
                int id = c.getInt(0);
                 String fb_id = c.getString(1);
                String google_id = c.getString(2);
                String realname = c.getString(3);
                String phone = c.getString(4);
                String provenance = c.getString(5);
                String birthday = c.getString(6);
                boolean enabled = new Boolean(c.getString(7));
                boolean notification = new Boolean( c.getString(8));
                String device = c.getString(9);
                String address = c.getString(10);
                String city = c.getString(11);
                String state = c.getString(12);
                String zipcode = c.getString(13);
                String role = c.getString(14);
                String group = c.getString(15);
                int loggedIn = c.getInt(16);

                user.setId(id);
                user.setFb_id(fb_id);
                user.setGoogle_id(google_id);
                user.setName(realname);
                user.setPhoneNo(phone);
                user.setProvenance(provenance);
                user.setBirthday(birthday);
                user.setEnabled(enabled);
                user.setNotification(notification);
                user.setDevice(device);
                user.setAddress(address);
                user.setCity(city);
                user.setState(state);
                user.setZipcode(zipcode);
                user.setRole(role);
                user.setGroupname(group);
                user.setLoggedIn(loggedIn);
            }

            return user;
        }

        c.close();
        db.close();

        return user;
    }

    @Override
    public int findUser(UserInfo userArg) {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        UserInfo user = null;

        Cursor c = db.rawQuery("SELECT " + UserInfo.COLUMN_ID + "," +
                UserInfo.COLUMN_USER_FBID + "," +
                UserInfo.COLUMN_USER_GOOGLEID  + "," +
                UserInfo.COLUMN_USER_REALNAME + "," +
                UserInfo.COLUMN_USER_PHONENO +"," +
                UserInfo.COLUMN_USER_PROVENANCE + "," +
                UserInfo.COLUMN_USER_BIRTHDAY + "," +
                UserInfo.COLUMN_USER_ENABLED + "," +
                UserInfo.COLUMN_USER_IS_NOTIFICATION + "," +
                UserInfo.COLUMN_USER_DEVICE_TOKEN + "," +
                UserInfo.COLUMN_USER_ADDRESS +"," +
                UserInfo.COLUMN_USER_CITY + "," +
                UserInfo.COLUMN_USER_STATE + "," +
                UserInfo.COLUMN_USER_ZIPCODE + "," +
                UserInfo.COLUMN_USER_ROLE + "," +
                UserInfo.COLUMN_USER_GROUP_NAME + "," +
                UserInfo.COLUMN_USER_LOGGEDIN +
                " FROM user where username =? ", new String[]{userArg.getEmail()});
        Log.d(TAG,"LOGGEDIN findUserBy userArg.getEmail() - "+userArg.getEmail());
        if(c!=null)
            Log.d(TAG,"LOGGEDIN findUserBy getCount - "+c.getCount());

        if (c != null && c.getCount()>0) {
            return c.getCount();
        }

        c.close();
        db.close();

        return 0;
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
        Cursor c = db.rawQuery("SELECT * FROM user where id=? ", new String[]{String.valueOf(user.getId())});
        if(c!= null && c.getCount()>0){
            return true;
        }
        else
            return false;


       /* Cursor cur = db.rawQuery("SELECT COUNT(*) FROM USER", null);
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

        return true;*/

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
        values.put(UserInfo.COLUMN_USER_FBID, user.getFb_id());
        values.put(UserInfo.COLUMN_USER_GOOGLEID, user.getGoogle_id());
        values.put(UserInfo.COLUMN_USER_REALNAME, user.getName());
        values.put(UserInfo.COLUMN_USER_PHONENO, user.getPhoneNo());
        values.put(UserInfo.COLUMN_USER_PROVENANCE, user.getGroupname());
        values.put(UserInfo.COLUMN_USER_BIRTHDAY, user.getBirthday());
        values.put(UserInfo.COLUMN_USER_ENABLED, user.getGoogle_id());
        values.put(UserInfo.COLUMN_USER_IS_NOTIFICATION, user.getEmail());
        values.put(UserInfo.COLUMN_USER_DEVICE_TOKEN, user.getDevice());
        values.put(UserInfo.COLUMN_USER_ADDRESS, user.getAddress());
        values.put(UserInfo.COLUMN_USER_CITY, user.getCity());
        values.put(UserInfo.COLUMN_USER_STATE, user.getState());
        values.put(UserInfo.COLUMN_USER_ZIPCODE, user.getZipcode());
        values.put(UserInfo.COLUMN_USER_ROLE, user.getRole());
        values.put(UserInfo.COLUMN_USER_GROUP_NAME, user.getGroupname());
        values.put(UserInfo.COLUMN_USER_LOGGEDIN, user.getLoggedIn());
        // updating row
        return db.update(UserInfo.TABLE_USER, values, UserInfo.COLUMN_USER_NAME + " = ?",
                new String[]{String.valueOf(user.getEmail())});

    }


    /**
     * @param user
     * @return
     */
    public boolean isLoggedIn(UserInfo user) {

        boolean isLoggedIn = false;

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        int loggedIn = 0;

        Cursor c = db.rawQuery("SELECT " + UserInfo.COLUMN_USER_LOGGEDIN + "," +
                " FROM user where username=? ", new String[]{user.getEmail()});

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

        Cursor c = db.rawQuery("SELECT " + UserInfo.COLUMN_ID + "," +
                UserInfo.COLUMN_USER_FBID + "," +
                UserInfo.COLUMN_USER_GOOGLEID  + "," +
                UserInfo.COLUMN_USER_REALNAME + "," +
                UserInfo.COLUMN_USER_PHONENO +"," +
                UserInfo.COLUMN_USER_PROVENANCE + "," +
                UserInfo.COLUMN_USER_BIRTHDAY + "," +
                UserInfo.COLUMN_USER_ENABLED + "," +
                UserInfo.COLUMN_USER_IS_NOTIFICATION + "," +
                UserInfo.COLUMN_USER_DEVICE_TOKEN + "," +
                UserInfo.COLUMN_USER_ADDRESS +"," +
                UserInfo.COLUMN_USER_CITY + "," +
                UserInfo.COLUMN_USER_STATE + "," +
                UserInfo.COLUMN_USER_ZIPCODE + "," +
                UserInfo.COLUMN_USER_ROLE + "," +
                UserInfo.COLUMN_USER_GROUP_NAME + "," +
                UserInfo.COLUMN_USER_NAME + "," +
                UserInfo.COLUMN_USER_LOGGEDIN +
                " FROM user where logged = 1 ORDER BY last_edit DESC ", null);
        if(c!=null)
        Log.d(TAG,"LOGGEDIN lastUserLoggedIn getCount - "+c.getCount());

        if (c != null && c.getCount()>0 ) {
            //assing values
            while (c.moveToFirst()) {
                user = new UserInfo();
                int id = c.getInt(0);
                String fb_id = c.getString(1);
                String google_id = c.getString(2);
                String realname = c.getString(3);
                String phone = c.getString(4);
                String provenance = c.getString(5);
                String birthday = c.getString(6);
                boolean enabled = new Boolean(c.getString(7));
                boolean notification = new Boolean(c.getString(8));
                String device = c.getString(9);
                String address = c.getString(10);
                String city = c.getString(11);
                String state = c.getString(12);
                String zipcode = c.getString(13);
                String role = c.getString(14);
                String group = c.getString(15);
                int loggedIn = c.getInt(16);
                String username = c.getString(17);

                user.setId(id);
                user.setFb_id(fb_id);
                user.setGoogle_id(google_id);
                user.setName(realname);
                user.setPhoneNo(phone);
                user.setProvenance(provenance);
                user.setBirthday(birthday);
                user.setEnabled(enabled);
                user.setNotification(notification);
                user.setDevice(device);
                user.setAddress(address);
                user.setCity(city);
                user.setState(state);
                user.setZipcode(zipcode);
                user.setRole(role);
                user.setGroupname(group);
                user.setLoggedIn(loggedIn);
                user.setEmail(username);
            }
        }
         return user;
     }


    @Override
    public int logOut(UserInfo user) {
        Log.d(TAG, "LogOut User - " + user.getEmail());

        db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserInfo.COLUMN_USER_LOGGEDIN, user.getLoggedIn());

        // updating row
        return db.update(UserInfo.TABLE_USER, values, UserInfo.COLUMN_USER_NAME + " = ?",
                new String[]{String.valueOf(user.getEmail())});
    }
}
