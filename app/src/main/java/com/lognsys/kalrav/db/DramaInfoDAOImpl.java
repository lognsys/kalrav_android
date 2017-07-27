package com.lognsys.kalrav.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
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
            values.put(DramaInfo.COLUMN_ID, dramaInfo.getId());
            values.put(DramaInfo.COLUMN_GROUP_NAME, dramaInfo.getGroup_name());
            values.put(DramaInfo.COLUMN_DRAMA_NAME, dramaInfo.getTitle());
            values.put(DramaInfo.COLUMN_LINK_PHOTO, dramaInfo.getLink_photo());
            values.put(DramaInfo.COLUMN_DATETIME, dramaInfo.getDatetime());
            values.put(DramaInfo.COLUMN_DRAMA_LENGTH, dramaInfo.getDrama_length());
            values.put(DramaInfo.COLUMN_DRAMA_LANGUAGE, dramaInfo.getDrama_language());
            values.put(DramaInfo.COLUMN_DRAMA_GENRE, dramaInfo.getGenre());
            values.put(DramaInfo.COLUMN_DRAMA_TIME, dramaInfo.getTime());
            values.put(DramaInfo.COLUMN_DRAMA_DESCRIPTION, dramaInfo.getDescription());
            values.put(DramaInfo.COLUMN_DRAMA_CAST, dramaInfo.getStar_cast());
            values.put(DramaInfo.COLUMN_DRAMA_WRITER, dramaInfo.getWriter());
            values.put(DramaInfo.COLUMN_DRAMA_DIRECTOR, dramaInfo.getDirector());
            values.put(DramaInfo.COLUMN_DRAMA_AVG_RATING, dramaInfo.getAvg_rating());
            values.put(DramaInfo.COLUMN_DRAMA_MUSIC, dramaInfo.getMusic());
            values.put(DramaInfo.COLUMN_DRAMA_ISFAV, dramaInfo.getIsfav());

            // Inserting Row
            db.insert(DramaInfo.TABLE_DRAMA, null, values);
//            db.close(); // Closing database connection
        }
    }

    @Override
    public boolean isDramaExist(DramaInfo dramaInfo) {
            db = sqLiteHelper.getReadableDatabase();
            Cursor cur = db.rawQuery("SELECT * FROM Drama where "+DramaInfo.COLUMN_ID+" = ? ",new String[]{String.valueOf(dramaInfo.getId())});
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
    public List<DramaInfo> getAllDramaByUserGroup(String group_name) {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        DramaInfo dramaInfo = null;


        ArrayList<DramaInfo> dramaInfos=new ArrayList<DramaInfo>();
        Cursor c = db.rawQuery("SELECT " + DramaInfo.COLUMN_ID + "," +
                DramaInfo.COLUMN_GROUP_NAME + "," +
                DramaInfo.COLUMN_DRAMA_NAME + "," +
                DramaInfo.COLUMN_LINK_PHOTO + "," +
                DramaInfo.COLUMN_DATETIME +"," +
                DramaInfo.COLUMN_DRAMA_LENGTH +"," +
                DramaInfo.COLUMN_DRAMA_LANGUAGE +"," +
                DramaInfo.COLUMN_DRAMA_GENRE +"," +
                DramaInfo.COLUMN_DRAMA_TIME +"," +
                DramaInfo.COLUMN_DRAMA_DESCRIPTION +"," +
                DramaInfo.COLUMN_DRAMA_CAST +"," +
                DramaInfo.COLUMN_DRAMA_WRITER +"," +
                DramaInfo.COLUMN_DRAMA_DIRECTOR +"," +
                DramaInfo.COLUMN_DRAMA_AVG_RATING +"," +
                DramaInfo.COLUMN_DRAMA_MUSIC +"," +
                DramaInfo.COLUMN_DRAMA_ISFAV +
                " FROM drama where "+ DramaInfo.COLUMN_GROUP_NAME +" =? ", new String[]{group_name});
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
                String cast= c.getString(10);
                String writer= c.getString(11);
                String director= c.getString(12);
                String avg_rating= c.getString(13);
                String music= c.getString(14);
                String isFav= c.getString(15);
                Log.d("","Fragment getAllDrama isFav "+isFav);

                dramaInfo.setId(Integer.parseInt(id));
                dramaInfo.setGroup_name(groupname);
                dramaInfo.setTitle(dramaname);
                dramaInfo.setLink_photo(linkphoto);
                dramaInfo.setDatetime(datatime);;
                dramaInfo.setDrama_length(dramaLength);;
                dramaInfo.setDrama_language(dramaLanguage);;
                dramaInfo.setGenre(dramaGenre);;
                dramaInfo.setTime(dramaTime);
                dramaInfo.setDescription(briefDescription);
                dramaInfo.setStar_cast(cast);
                dramaInfo.setWriter(writer);
                dramaInfo.setDirector(director);
                dramaInfo.setAvg_rating(avg_rating);
                dramaInfo.setMusic(music);
                dramaInfo.setIsfav(isFav);

                Log.d("","Fragment getAllDrama getIsfav "+dramaInfo.getIsfav());
                dramaInfos.add(dramaInfo);
            }

            Log.d("","Fragment getAllDrama dramaInfos.size "+dramaInfos.size());

            c.close();
        }
        else {
            addDrama(dramaInfo);
        }
        return dramaInfos;
    }

    @Override
    public DramaInfo getDramaByDramaId(int id) {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        DramaInfo dramaInfo = null;


        ArrayList<DramaInfo> dramaInfos=new ArrayList<DramaInfo>();
        Cursor c = db.rawQuery("SELECT  * FROM drama where "+ DramaInfo.COLUMN_ID +" =? ", new String[]{String.valueOf(id)});
        if(c!=null)
            Log.d("","Test getAllDrama  c.getCount() "+ c.getCount());

        if (c != null && c.getCount()>0) {

            while (c.moveToNext()){
                dramaInfo = new DramaInfo();
                 id = Integer.parseInt(c.getString(0));
                String groupname = c.getString(1);
                String dramaname = c.getString(2);
                String linkphoto = c.getString(3);
                String datatime = c.getString(4);
                String dramaLength = c.getString(5);
                String dramaLanguage = c.getString(6);
                String dramaGenre = c.getString(7);
                String dramaTime= c.getString(8);
                String briefDescription= c.getString(9);
                String cast= c.getString(10);
                String writer= c.getString(11);
                String director= c.getString(12);
                String avg_rating= c.getString(13);
                String music= c.getString(14);
                String isFav= c.getString(15);
                Log.d("","Fragment getAllDrama isFav "+isFav);

                dramaInfo.setId(id);
                dramaInfo.setGroup_name(groupname);
                dramaInfo.setTitle(dramaname);
                dramaInfo.setLink_photo(linkphoto);
                dramaInfo.setDatetime(datatime);;
                dramaInfo.setDrama_length(dramaLength);;
                dramaInfo.setDrama_language(dramaLanguage);;
                dramaInfo.setGenre(dramaGenre);;
                dramaInfo.setTime(dramaTime);
                dramaInfo.setDescription(briefDescription);
                dramaInfo.setStar_cast(cast);
                dramaInfo.setWriter(writer);
                dramaInfo.setDirector(director);
                dramaInfo.setAvg_rating(avg_rating);
                dramaInfo.setMusic(music);
                dramaInfo.setIsfav(isFav);


            }
            return dramaInfo;
        }
        return null;
    }

    @Override
    public int getDramaCount() {
        db = sqLiteHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM Drama ",null);
        if (cur != null && cur.getCount()>0)
            return cur.getCount();
        else
            return 0;
    }

    @Override
    public int updateDrama(DramaInfo dramaInfo) {


        db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DramaInfo.COLUMN_GROUP_NAME, dramaInfo.getGroup_name());
        values.put(DramaInfo.COLUMN_DRAMA_NAME, dramaInfo.getTitle());
        values.put(DramaInfo.COLUMN_LINK_PHOTO, dramaInfo.getLink_photo());
        values.put(DramaInfo.COLUMN_DATETIME, dramaInfo.getDatetime());
        values.put(DramaInfo.COLUMN_DRAMA_LENGTH, dramaInfo.getDrama_length());
        values.put(DramaInfo.COLUMN_DRAMA_LANGUAGE, dramaInfo.getDrama_language());
        values.put(DramaInfo.COLUMN_DRAMA_GENRE, dramaInfo.getGenre());
        values.put(DramaInfo.COLUMN_DRAMA_TIME, dramaInfo.getTime());
        values.put(DramaInfo.COLUMN_DRAMA_DESCRIPTION, dramaInfo.getDescription());
        values.put(DramaInfo.COLUMN_DRAMA_CAST, dramaInfo.getStar_cast());
        values.put(DramaInfo.COLUMN_DRAMA_WRITER, dramaInfo.getWriter());
        values.put(DramaInfo.COLUMN_DRAMA_DIRECTOR, dramaInfo.getDirector());
        values.put(DramaInfo.COLUMN_DRAMA_AVG_RATING, dramaInfo.getAvg_rating());
        values.put(DramaInfo.COLUMN_DRAMA_MUSIC, dramaInfo.getMusic());
        values.put(DramaInfo.COLUMN_DRAMA_ISFAV, dramaInfo.getIsfav());
        Log.d("DramaFragment","Fragment updateCount dramaInfo.getIsfav() "+ dramaInfo.getIsfav());

        // updating row
        int updateCount= db.update(DramaInfo.TABLE_DRAMA, values, DramaInfo.COLUMN_ID + " = ?",
                new String[]{String.valueOf(dramaInfo.getId())});
        Log.d("DramaFragment","Fragment updateCount "+ updateCount);

        return updateCount;

    }

    @Override
    public boolean deleteDrama() {
        return false;
    }

    @Override
    public List<DramaInfo> getAllDrama() {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        DramaInfo dramaInfo = null;


        ArrayList<DramaInfo> dramaInfos=new ArrayList<DramaInfo>();
        Cursor c = db.rawQuery("SELECT " +
                DramaInfo.COLUMN_ID + "," +
                DramaInfo.COLUMN_GROUP_NAME + "," +
                DramaInfo.COLUMN_DRAMA_NAME + "," +
                DramaInfo.COLUMN_LINK_PHOTO + "," +
                DramaInfo.COLUMN_DATETIME +"," +
                DramaInfo.COLUMN_DRAMA_LENGTH +"," +
                DramaInfo.COLUMN_DRAMA_LANGUAGE +"," +
                DramaInfo.COLUMN_DRAMA_GENRE +"," +
                DramaInfo.COLUMN_DRAMA_TIME +"," +
                DramaInfo.COLUMN_DRAMA_DESCRIPTION +"," +
                DramaInfo.COLUMN_DRAMA_CAST +"," +
                DramaInfo.COLUMN_DRAMA_WRITER +"," +
                DramaInfo.COLUMN_DRAMA_DIRECTOR +"," +
                DramaInfo.COLUMN_DRAMA_AVG_RATING +"," +
                DramaInfo.COLUMN_DRAMA_MUSIC +"," +
                DramaInfo.COLUMN_DRAMA_ISFAV +
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
                String cast= c.getString(10);
                String writer= c.getString(11);
                String director= c.getString(12);
                String avg_rating= c.getString(13);
                String music= c.getString(14);
                String isFav= c.getString(15);
                Log.d("","Fragment getAllDrama isFav "+isFav);

                dramaInfo.setId(Integer.parseInt(id));
                dramaInfo.setGroup_name(groupname);
                dramaInfo.setTitle(dramaname);
                dramaInfo.setLink_photo(linkphoto);
                dramaInfo.setDatetime(datatime);;
                dramaInfo.setDrama_length(dramaLength);;
                dramaInfo.setDrama_language(dramaLanguage);;
                dramaInfo.setGenre(dramaGenre);;
                dramaInfo.setTime(dramaTime);
                dramaInfo.setDescription(briefDescription);
                dramaInfo.setStar_cast(cast);
                dramaInfo.setWriter(writer);
                dramaInfo.setDirector(director);
                dramaInfo.setAvg_rating(avg_rating);
                dramaInfo.setMusic(music);
                dramaInfo.setIsfav(isFav);

                Log.d("","Fragment getAllDrama getIsfav "+dramaInfo.getIsfav());
                dramaInfos.add(dramaInfo);
            }
            Log.d("","Fragment getAllDrama dramaInfos.size "+dramaInfos.size());

            c.close();
        }
        return dramaInfos;
    }



    public ArrayList<DramaInfo> getDramaListByFavId(ArrayList<FavouritesInfo> favouritesInfos) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        DramaInfo dramaInfo = null;
        Log.d("","Test getDramaListByFavId drama favouritesInfo "+favouritesInfos.size());


        ArrayList<DramaInfo> dramaInfos=new ArrayList<DramaInfo>();
        for(int i=0;i<favouritesInfos.size();i++){
            FavouritesInfo fav=favouritesInfos.get(i);
            Cursor c = db.rawQuery("SELECT * FROM drama where "+ DramaInfo.COLUMN_ID +" = ? ",
                    new String[]{String.valueOf(fav.getDrama_id())});

            if (c != null && c.getCount()>0) {
                Log.d("","Test getDramaListByFavId drama c.getCount() "+ c.getCount());

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
                    String cast= c.getString(10);
                    String writer= c.getString(11);
                    String director= c.getString(12);
                    String avg_rating= c.getString(13);
                    String music= c.getString(14);
                    String isFav= c.getString(15);
                    Log.d("","Fragment getAllDrama isFav "+isFav);

                    dramaInfo.setId(Integer.parseInt(id));
                    dramaInfo.setGroup_name(groupname);
                    dramaInfo.setTitle(dramaname);
                    dramaInfo.setLink_photo(linkphoto);
                    dramaInfo.setDatetime(datatime);;
                    dramaInfo.setDrama_length(dramaLength);;
                    dramaInfo.setDrama_language(dramaLanguage);;
                    dramaInfo.setGenre(dramaGenre);;
                    dramaInfo.setTime(dramaTime);
                    dramaInfo.setDescription(briefDescription);
                    dramaInfo.setStar_cast(cast);
                    dramaInfo.setWriter(writer);
                    dramaInfo.setDirector(director);
                    dramaInfo.setAvg_rating(avg_rating);
                    dramaInfo.setMusic(music);
                    dramaInfo.setIsfav(isFav);
                    dramaInfos.add(dramaInfo);
                }


            }

            c.close();
        }
        Log.d("","Test getDramaListByFavId dramaInfos.size "+dramaInfos.size());
        return dramaInfos;
    }

}
