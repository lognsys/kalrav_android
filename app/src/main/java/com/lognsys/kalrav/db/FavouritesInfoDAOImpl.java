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

public class FavouritesInfoDAOImpl implements FavouritesInfoDAO {

    private final String TAG = getClass().getSimpleName();
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    public FavouritesInfoDAOImpl(Context context) {

        sqLiteHelper = new SQLiteHelper(context);
        db = sqLiteHelper.getWritableDatabase();
    }



    @Override
    public void addFav(FavouritesInfo favouritesInfo) {
        db = sqLiteHelper.getWritableDatabase();
        Log.d("","Bookmark  dramaInfo isFavExist(favouritesInfo) "+isFavExist(favouritesInfo));

        /*// If user exists update user
        if (isFavExist(favouritesInfo) && favouritesInfo.getId()!=0) {

            int updateCount =updateFav(favouritesInfo);
            Log.d("","Bookmark  dramaInfo updateCount"+updateCount);

        } else */{  //Create user if new

            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_ID, favouritesInfo.getId());
            values.put(SQLiteHelper.COLUMN_DRAMA_ID, favouritesInfo.getDrama_id());
            values.put(String.valueOf(SQLiteHelper.COLUMN_ISFAV),favouritesInfo.isFav());

            /*

            values.put(SQLiteHelper.COLUMN_GROUP_NAME, favouritesInfo.getGroup_name());
            values.put(SQLiteHelper.COLUMN_DRAMA_NAME, favouritesInfo.getDrama_name());
            values.put(SQLiteHelper.COLUMN_LINK_PHOTO, favouritesInfo.getLink_photo());
            values.put(SQLiteHelper.COLUMN_DATETIME, favouritesInfo.getDatetime());
            values.put(SQLiteHelper.COLUMN_DRAMA_LENGTH, favouritesInfo.getDrama_length());
            values.put(SQLiteHelper.COLUMN_DRAMA_LANGUAGE, favouritesInfo.getDrama_language());
            values.put(SQLiteHelper.COLUMN_DRAMA_GENRE, favouritesInfo.getGenre());
            values.put(SQLiteHelper.COLUMN_DRAMA_TIME, favouritesInfo.getTime());
            values.put(SQLiteHelper.COLUMN_DRAMA_DESCRIPTION, favouritesInfo.getBriefDescription());*/

            // Inserting Row
            db.insert(SQLiteHelper.TABLE_FAVOURITE, null, values);
//            db.close(); // Closing database connection
        }
    }

    @Override
    public boolean isFavExist(FavouritesInfo favouritesInfo) {
        db = sqLiteHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM favourite", null);
        Log.d("", "Bookmark  dramaInfo isFavExist cur.getCount()) " + (cur.getCount()));
        if (cur != null && cur.getCount() < 0) {
            cur.moveToFirst();                       // Always one row returned.
            if (cur.getInt(0) == 0) {               // Zero count means empty table.
                Log.d(TAG, "DB_isUserExists - NOT FOUND...");
                return false;
            }

        } else {
            return true;
        }
        return false;
    }
    @Override
    public int updateFav(FavouritesInfo favouritesInfo) {
        Log.d("","Bookmark  dramaInfo updateFav(favouritesInfo) "+(favouritesInfo));
        Log.d("","Bookmark  dramaInfo updateFav (favouritesInfo).getId() "+(favouritesInfo).getId());

        db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_DRAMA_ID, favouritesInfo.getDrama_id());
        values.put(String.valueOf(SQLiteHelper.COLUMN_ISFAV),favouritesInfo.isFav());
        // updating row
        return db.update(SQLiteHelper.TABLE_FAVOURITE, values, SQLiteHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(favouritesInfo.getId())});

    }

    @Override
    public int deleteFav(FavouritesInfo favouritesInfo)
    {
        db = sqLiteHelper.getWritableDatabase();
        int countDelete=db.delete(SQLiteHelper.TABLE_FAVOURITE,
                SQLiteHelper.COLUMN_ID+" = ? ",
                new String[]{String.valueOf(favouritesInfo.getId())});
        Log.d("","Bookmark Delete count "+countDelete);
        return countDelete;
    }

    @Override
    public List<FavouritesInfo> getAllFav() {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        FavouritesInfo favouritesInfo = null;
        Log.d("","Test getAllFav  ");


        ArrayList<FavouritesInfo> favouritesInfos=new ArrayList<FavouritesInfo>();
        Cursor c = db.rawQuery("SELECT " + SQLiteHelper.COLUMN_ID + "," +
                SQLiteHelper.COLUMN_DRAMA_ID + "," +
                SQLiteHelper.COLUMN_ISFAV + /*"," +
                SQLiteHelper.COLUMN_DRAMA_NAME + "," +
                SQLiteHelper.COLUMN_LINK_PHOTO + "," +
                SQLiteHelper.COLUMN_DATETIME +"," +
                SQLiteHelper.COLUMN_DRAMA_LENGTH +"," +
                SQLiteHelper.COLUMN_DRAMA_LANGUAGE +"," +
                SQLiteHelper.COLUMN_DRAMA_GENRE +"," +
                SQLiteHelper.COLUMN_DRAMA_TIME +"," +
                SQLiteHelper.COLUMN_DRAMA_DESCRIPTION +*/
                " FROM favourite", null);
        if(c!=null)
        Log.d("","Test getAllFav  c.getCount() "+ c.getCount());

        if (c != null && c.getCount()>0) {

            while (c.moveToNext()){
                favouritesInfo = new FavouritesInfo();
                String id = c.getString(0);
                String drama_id = c.getString(1);
                Boolean isFav = Boolean.valueOf(c.getString(2));
                favouritesInfo.setDrama_id(Integer.parseInt(drama_id));
                               Log.d("","Test getAllFav drama_id "+drama_id);
                Log.d("","Test getAllFav favouritesInfo.getDrama_id() "+ favouritesInfo.getDrama_id());

                favouritesInfo.setId(Integer.parseInt(id));
                Log.d("","Test getAllFav id "+id);
                Log.d("","Test getAllFav favouritesInfo.getId() "+ favouritesInfo.getId());

                favouritesInfo.setFav(isFav);
                favouritesInfos.add(favouritesInfo);
            }
            Log.d("","Test getAllFav favouritesInfo.size "+ favouritesInfos.size());

            c.close();
        }
        return favouritesInfos;
    }

    @Override
    public FavouritesInfo findfavBy(FavouritesInfo favouritesInfo) {
        return null;
    }
}
