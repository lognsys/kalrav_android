package com.lognsys.kalrav.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.widget.Toast;

import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.FavouritesInfo;
import com.lognsys.kalrav.model.UserInfo;
import com.lognsys.kalrav.util.KalravApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        // If user exists update user
        Log.d("","Test displaydrama  isDramaExist(dramaInfo) "+isDramaExist(dramaInfo));
        if (isDramaExist(dramaInfo)) {

            updateDrama(dramaInfo);

        } else {  //Create user if new

            ContentValues values = new ContentValues();
            values.put(DramaInfo.COLUMN_ID, dramaInfo.getId());
            Log.d("","Test displaydrama dramaInfo.getId() === "+ dramaInfo.getId());

            values.put(DramaInfo.COLUMN_GROUP_NAME, dramaInfo.getGroup_name());
            Log.d("","Test displaydrama dramaInfo.getGroup_name() === "+ dramaInfo.getGroup_name());

            values.put(DramaInfo.COLUMN_DRAMA_NAME, dramaInfo.getTitle());
            Log.d("","Test displaydrama dramaInfo.getTitle() === "+ dramaInfo.getTitle());

            values.put(DramaInfo.COLUMN_LINK_PHOTO, dramaInfo.getLink_photo());
            Log.d("","Test displaydrama dramaInfo.getLink_photo() === "+ dramaInfo.getLink_photo());

            values.put(DramaInfo.COLUMN_DATETIME, dramaInfo.getDatetime());
            Log.d("","Test displaydrama dramaInfo.getDatetime() === "+ dramaInfo.getDatetime());

            values.put(DramaInfo.COLUMN_DRAMA_LENGTH, dramaInfo.getDrama_length());
            Log.d("","Test displaydrama dramaInfo.getDrama_length() === "+ dramaInfo.getDrama_length());

            values.put(DramaInfo.COLUMN_DRAMA_LANGUAGE, dramaInfo.getDrama_language());
            Log.d("","Test displaydrama dramaInfo.getDrama_language() === "+ dramaInfo.getDrama_language());

            values.put(DramaInfo.COLUMN_DRAMA_GENRE, dramaInfo.getGenre());
            Log.d("","Test displaydrama dramaInfo.getGenre() === "+ dramaInfo.getGenre());

            values.put(DramaInfo.COLUMN_DRAMA_TIME, dramaInfo.getTime());
            Log.d("","Test displaydrama dramaInfo.getTime() === "+ dramaInfo.getTime());

            values.put(DramaInfo.COLUMN_DRAMA_DESCRIPTION, dramaInfo.getDescription());
            Log.d("","Test displaydrama dramaInfo.getDescription() === "+ dramaInfo.getDescription());

            values.put(DramaInfo.COLUMN_DRAMA_CAST, dramaInfo.getStar_cast());
            Log.d("","Test displaydrama dramaInfo.getStar_cast() === "+ dramaInfo.getStar_cast());

            values.put(DramaInfo.COLUMN_DRAMA_WRITER, dramaInfo.getWriter());
            Log.d("","Test displaydrama dramaInfo.getWriter() === "+ dramaInfo.getWriter());

            values.put(DramaInfo.COLUMN_DRAMA_DIRECTOR, dramaInfo.getDirector());
            Log.d("","Test displaydrama dramaInfo.getDirector() === "+ dramaInfo.getDirector());

            values.put(DramaInfo.COLUMN_DRAMA_AVG_RATING, dramaInfo.getAvg_rating());
            Log.d("","Test displaydrama dramaInfo.getAvg_rating() === "+ dramaInfo.getAvg_rating());

            values.put(DramaInfo.COLUMN_DRAMA_MUSIC, dramaInfo.getMusic());
            Log.d("","Test displaydrama dramaInfo.getMusic() === "+ dramaInfo.getMusic());

            values.put(DramaInfo.COLUMN_DRAMA_ISFAV, dramaInfo.getIsfav());
            Log.d("","Test displaydrama dramaInfo.getIsfav() === "+ dramaInfo.getIsfav());


            // Inserting Row
            db.insert(DramaInfo.TABLE_DRAMA, null, values);
//            db.close(); // Closing database connection
        }
    }

    @Override
    public boolean isDramaExist(DramaInfo dramaInfo) {
            db = sqLiteHelper.getReadableDatabase();
        if(dramaInfo!=null){
            Cursor cur = db.rawQuery("SELECT * FROM drama where "+DramaInfo.COLUMN_ID+" = ? ",new String[]{String.valueOf(dramaInfo.getId())});
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


        }
        return false;


    }

    @Override
    public List<DramaInfo> getAllDramaByUserGroup(String group_name) {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        DramaInfo dramaInfo = null;
        Log.d("","Rest getAllDramaByUserGroup group_name "+ group_name);


       ArrayList<DramaInfo> dramaInfos=new ArrayList<DramaInfo>();
                Cursor c = db.query(DramaInfo.TABLE_DRAMA,new String[]{ DramaInfo.COLUMN_ID + "," +
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
                        DramaInfo.COLUMN_DRAMA_ISFAV },DramaInfo.COLUMN_GROUP_NAME +" =? ",new String[]{group_name},null,null,DramaInfo.COLUMN_DATETIME+" desc");
        if(c!=null)
            Log.d("","Rest getAllDramaByUserGroup  c.getCount() "+ c.getCount());

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
            return null;
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
        if(dramaInfo.getGroup_name()!=null){
            values.put(DramaInfo.COLUMN_GROUP_NAME, dramaInfo.getGroup_name());
            Log.d(TAG,"THIS updateDrama COLUMN_GROUP_NAME dramaInfo.getGroup_name()=== "+dramaInfo.getGroup_name());
        }
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
        if(dramaInfo.getIsfav()!=null){
            values.put(DramaInfo.COLUMN_DRAMA_ISFAV, dramaInfo.getIsfav());
            Log.d(TAG,"THIS updateDrama COLUMN_DRAMA_ISFAV dramaInfo.getIsfav()=== "+dramaInfo.getIsfav());
        }

        // updating row
        int updateCount= db.update(DramaInfo.TABLE_DRAMA, values, DramaInfo.COLUMN_ID + " = ?",
                new String[]{String.valueOf(dramaInfo.getId())});
        Log.d(TAG,"Rest updateDrama updateCount "+updateCount);

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
        // (3) create a new String using the date format we want
        String date  = KalravApplication.getInstance().getCurrentDate() ;
        Log.d(TAG,"Rest getAllDrama date "+date);

//        db.query(true, DATABASE_TABLE, ALL_KEYS, KEY_DATE + "=?", new String[] {date}, null, null, null, null);
        ArrayList<DramaInfo> dramaInfos=new ArrayList<DramaInfo>();
 /*       Cursor c = db.query(true, DramaInfo.TABLE_DRAMA, new String[]{DramaInfo.COLUMN_ID + "," +
                DramaInfo.COLUMN_GROUP_NAME + "," +
                DramaInfo.COLUMN_DRAMA_NAME + "," +
                DramaInfo.COLUMN_LINK_PHOTO + "," +
                DramaInfo.COLUMN_DATETIME + "," +
                DramaInfo.COLUMN_DRAMA_LENGTH + "," +
                DramaInfo.COLUMN_DRAMA_LANGUAGE + "," +
                DramaInfo.COLUMN_DRAMA_GENRE + "," +
                DramaInfo.COLUMN_DRAMA_TIME + "," +
                DramaInfo.COLUMN_DRAMA_DESCRIPTION + "," +
                DramaInfo.COLUMN_DRAMA_CAST + "," +
                DramaInfo.COLUMN_DRAMA_WRITER + "," +
                DramaInfo.COLUMN_DRAMA_DIRECTOR + "," +
                DramaInfo.COLUMN_DRAMA_AVG_RATING + "," +
                DramaInfo.COLUMN_DRAMA_MUSIC + "," +
                DramaInfo.COLUMN_DRAMA_ISFAV}, DramaInfo.COLUMN_DATETIME  + ">= '"+date+"'", null, null, null, null, null);
*/

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
                " FROM drama order by "+ DramaInfo.COLUMN_DATETIME +" desc ",null);

        if (c != null && c.getCount()>0) {

            Log.d(TAG,"Rest getAllDrama  c.getCount() "+ c.getCount());

            while (c.moveToNext()){
                dramaInfo = new DramaInfo();
                String id = c.getString(0);
                String groupname = c.getString(1);
                Log.d(TAG,"Rest getAllDrama  groupname "+ (groupname));

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

                dramaInfo.setId(Integer.parseInt(id));
                dramaInfo.setGroup_name(groupname);
                Log.d(TAG,"Rest getAllDrama  dramaInfo.getGroupname "+ (dramaInfo.getGroup_name()));

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
            Log.d(TAG,"Rest getAllDrama  dramaInfos.size() "+dramaInfos.size());

            c.close();
            return dramaInfos;
        }
        else {
            return null;
        }

    }



    public ArrayList<DramaInfo> getDramaListByFavId(ArrayList<FavouritesInfo> favouritesInfos) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        DramaInfo dramaInfo = null;
        Log.d(TAG,"Rest getDramaListByFavId  favouritesInfos.size() "+favouritesInfos.size());

        ArrayList<DramaInfo> dramaInfos=new ArrayList<DramaInfo>();
        if(favouritesInfos!= null && favouritesInfos.size()>0){
            for(int i=0;i<favouritesInfos.size();i++){
                FavouritesInfo fav=favouritesInfos.get(i);
                Cursor c = db.rawQuery("SELECT * FROM drama where "+ DramaInfo.COLUMN_ID +" = ? ",
                        new String[]{String.valueOf(fav.getDrama_id())});

                if (c != null && c.getCount()>0) {
                    Log.d(TAG,"Rest getDramaListByFavId  c.getCount() "+ c.getCount());

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
        }
        Log.d(TAG,"Rest getDramaListByFavId  dramaInfos.size() "+dramaInfos.size());
        return dramaInfos;
    }
}