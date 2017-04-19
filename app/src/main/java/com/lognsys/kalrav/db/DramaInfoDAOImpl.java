package com.lognsys.kalrav.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.FavouritesInfo;
import com.lognsys.kalrav.model.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pdoshi on 27/12/16.
 */

public class DramaInfoDAOImpl implements DramaInfoDAO {

    private final String TAG = getClass().getSimpleName();
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    public DramaInfoDAOImpl(Context context) {

        sqLiteHelper = new SQLiteHelper(context);
        db = sqLiteHelper.getWritableDatabase();
    }



    @Override
    public void addDrama(DramaInfo dramaInfo) {
        db = sqLiteHelper.getWritableDatabase();
        Log.d("","Test addDrama isDramaExist(dramaInfo) "+isDramaExist(dramaInfo));

        // If user exists update user
        if (isDramaExist(dramaInfo)) {

            updateDrama(dramaInfo);

        } else {  //Create user if new

            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_ID, dramaInfo.getId());
            values.put(SQLiteHelper.COLUMN_GROUP_NAME, dramaInfo.getGroup_name());
            values.put(SQLiteHelper.COLUMN_DRAMA_NAME, dramaInfo.getDrama_name());
            values.put(SQLiteHelper.COLUMN_LINK_PHOTO, dramaInfo.getLink_photo());
            values.put(SQLiteHelper.COLUMN_DATETIME, dramaInfo.getDatetime());
            values.put(SQLiteHelper.COLUMN_DRAMA_LENGTH, dramaInfo.getDrama_length());
            values.put(SQLiteHelper.COLUMN_DRAMA_LANGUAGE, dramaInfo.getDrama_language());
            values.put(SQLiteHelper.COLUMN_DRAMA_GENRE, dramaInfo.getGenre());
            values.put(SQLiteHelper.COLUMN_DRAMA_TIME, dramaInfo.getTime());
            values.put(SQLiteHelper.COLUMN_DRAMA_DESCRIPTION, dramaInfo.getBriefDescription());
            Log.d("","Test addDrama dramaInfo.getId() "+dramaInfo.getId());
            Log.d("","Test addDrama dramaInfo.getGroup_name() "+dramaInfo.getGroup_name());
            Log.d("","Test addDrama dramaInfo.getDrama_name() "+dramaInfo.getDrama_name());
            Log.d("","Test addDrama dramaInfo.getLink_photo() "+dramaInfo.getLink_photo());
            Log.d("","Test addDrama dramaInfo.getDatetime() "+dramaInfo.getDatetime());

            // Inserting Row
            db.insert(SQLiteHelper.TABLE_DRAMA, null, values);
//            db.close(); // Closing database connection
        }
    }

    @Override
    public boolean isDramaExist(DramaInfo dramaInfo) {
            db = sqLiteHelper.getReadableDatabase();
            Cursor cur = db.rawQuery("SELECT * FROM Drama where "+SQLiteHelper.COLUMN_ID+" = ? ",new String[]{String.valueOf(dramaInfo.getId())});
            if (cur != null && cur.getCount()>0) {
                Log.d(TAG, "DB_isUserExists cur "+cur.getCount());
                cur.moveToFirst();                       // Always one row returned.
                if (cur.getInt(0) == 0) {               // Zero count means empty table.
                    Log.d(TAG, "DB_isUserExists - NOT FOUND...");
                    return false;
                }
            }
            else{
                return false;
            }



            return true;

    }
    @Override
    public int updateDrama(DramaInfo dramaInfo) {

        Log.d(TAG, "Update User - " );

        db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_GROUP_NAME, dramaInfo.getGroup_name());
        values.put(SQLiteHelper.COLUMN_DRAMA_NAME, dramaInfo.getDrama_name());
        values.put(SQLiteHelper.COLUMN_LINK_PHOTO, dramaInfo.getLink_photo());
        values.put(SQLiteHelper.COLUMN_DATETIME, dramaInfo.getDatetime());
        values.put(SQLiteHelper.COLUMN_DRAMA_LENGTH, dramaInfo.getDrama_length());
        values.put(SQLiteHelper.COLUMN_DRAMA_LANGUAGE, dramaInfo.getDrama_language());
        values.put(SQLiteHelper.COLUMN_DRAMA_GENRE, dramaInfo.getGenre());
        values.put(SQLiteHelper.COLUMN_DRAMA_TIME, dramaInfo.getTime());
        values.put(SQLiteHelper.COLUMN_DRAMA_DESCRIPTION, dramaInfo.getBriefDescription());
        // updating row
        return db.update(SQLiteHelper.TABLE_DRAMA, values, SQLiteHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(dramaInfo.getId())});

    }

    @Override
    public boolean deleteDrama() {
        return false;
    }

    @Override
    public List<DramaInfo> getAllDrama() {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        DramaInfo dramaInfo = null;
        Log.d("","Test getAllDrama  ");


        ArrayList<DramaInfo> dramaInfos=new ArrayList<DramaInfo>();
        Cursor c = db.rawQuery("SELECT " + SQLiteHelper.COLUMN_ID + "," +
                SQLiteHelper.COLUMN_GROUP_NAME + "," +
                SQLiteHelper.COLUMN_DRAMA_NAME + "," +
                SQLiteHelper.COLUMN_LINK_PHOTO + "," +
                SQLiteHelper.COLUMN_DATETIME +"," +
                SQLiteHelper.COLUMN_DRAMA_LENGTH +"," +
                SQLiteHelper.COLUMN_DRAMA_LANGUAGE +"," +
                SQLiteHelper.COLUMN_DRAMA_GENRE +"," +
                SQLiteHelper.COLUMN_DRAMA_TIME +"," +
                SQLiteHelper.COLUMN_DRAMA_DESCRIPTION +
                " FROM drama", null);
        if(c!=null)
        Log.d("","Test getAllDrama  c.getCount() "+ c.getCount());

        if (c != null && c.getCount()>0) {

            while (c.moveToNext()){
                dramaInfo = new DramaInfo();
                String id = c.getString(0);
                String groupname = c.getString(1);
                String dramaname = c.getString(2);
                String linkphoto = c.getString(3);
                String datatime = c.getString(4);
                String dramaLength = c.getString(5);
                String dramaLanguage = c.getString(6);
                String dramaGenre = c.getString(7);
                String dramaTime= c.getString(8);
                String briefDescription= c.getString(9);

                dramaInfo.setId(Integer.parseInt(id));
                dramaInfo.setGroup_name(groupname);
                dramaInfo.setDrama_name(dramaname);
                dramaInfo.setLink_photo(linkphoto);
                dramaInfo.setDatetime(datatime);;
                dramaInfo.setDrama_length(dramaLength);;
                dramaInfo.setDrama_language(dramaLanguage);;
                dramaInfo.setGenre(dramaGenre);;
                dramaInfo.setTime(dramaTime);
                dramaInfo.setBriefDescription(briefDescription);
                dramaInfos.add(dramaInfo);
            }
            Log.d("","Test getAllDrama dramaInfos.size "+dramaInfos.size());

            c.close();
        }
        return dramaInfos;
    }



    public List<DramaInfo> getDramaListByFavId(int favouritesInfo) {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        DramaInfo dramaInfo = null;
        Log.d("","Test getDramaListByFavId  ");


        ArrayList<DramaInfo> dramaInfos=new ArrayList<DramaInfo>();
        Cursor c = db.rawQuery("SELECT * FROM drama where "+ SQLiteHelper.COLUMN_ID +" = ? ",
                new String[favouritesInfo]);
        if(c!=null)
            Log.d("","Test getDramaListByFavId  c.getCount() "+ c.getCount());

        if (c != null && c.getCount()>0) {

            while (c.moveToNext()){
                dramaInfo = new DramaInfo();
                String id = c.getString(0);
                String groupname = c.getString(1);
                String dramaname = c.getString(2);
                String linkphoto = c.getString(3);
                String datatime = c.getString(4);
                String dramaLength = c.getString(5);
                String dramaLanguage = c.getString(6);
                String dramaGenre = c.getString(7);
                String dramaTime= c.getString(8);
                String briefDescription= c.getString(9);

                dramaInfo.setId(Integer.parseInt(id));
                dramaInfo.setGroup_name(groupname);
                dramaInfo.setDrama_name(dramaname);
                dramaInfo.setLink_photo(linkphoto);
                dramaInfo.setDatetime(datatime);;
                dramaInfo.setDrama_length(dramaLength);;
                dramaInfo.setDrama_language(dramaLanguage);;
                dramaInfo.setGenre(dramaGenre);;
                dramaInfo.setTime(dramaTime);
                dramaInfo.setBriefDescription(briefDescription);
                dramaInfos.add(dramaInfo);
            }
            Log.d("","Test getDramaListByFavId dramaInfos.size "+dramaInfos.size());

            c.close();
        }
        return dramaInfos;
    }

}
