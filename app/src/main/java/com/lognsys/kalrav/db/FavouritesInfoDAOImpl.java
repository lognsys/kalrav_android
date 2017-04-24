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

       {
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_DRAMA_ID, favouritesInfo.getDrama_id());
            values.put(String.valueOf(SQLiteHelper.COLUMN_ISFAV), favouritesInfo.isFav());


            // Inserting Row
            db.insert(SQLiteHelper.TABLE_FAVOURITE, null, values);
            db.close(); // Closing database connection
        }
    }
    @Override
    public boolean isFavExits(FavouritesInfo favouritesInfo) {
        db = sqLiteHelper.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM favourite where "+SQLiteHelper.COLUMN_ID+" = ? ",new String[]{String.valueOf(favouritesInfo.getId())});
       if(cur != null )
        Log.d(TAG, "TABLE_FAVOURITE cur "+cur.getCount());

        if (cur != null && cur.getCount()>0) {
             return true;

        }
        else{
            return  false;
        }

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
        Log.d("","Bookmark Delete favouritesInfo.getDrama_id() "+favouritesInfo.getDrama_id());

        db = sqLiteHelper.getWritableDatabase();
        int countDelete=db.delete(SQLiteHelper.TABLE_FAVOURITE,
                SQLiteHelper.COLUMN_DRAMA_ID+" = ? ",
                new String[]{String.valueOf(favouritesInfo.getDrama_id())});
        Log.d("","Bookmark Delete count "+countDelete);
        return countDelete;
    }

    @Override
    public List<FavouritesInfo> getAllFav() {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        FavouritesInfo favouritesInfo = null;
        Log.d("","Test getAllFav  ");


        ArrayList<FavouritesInfo> favouritesInfos=new ArrayList<FavouritesInfo>();
        Cursor c = db.rawQuery("SELECT * FROM favourite", null);
        if(c!=null)
        Log.d("","Test getAllFav  c.getCount() "+ c.getCount());

        if (c != null && c.getCount()>0) {

            while (c.moveToNext()){
                favouritesInfo = new FavouritesInfo();
                String id = c.getString(0);
                String drama_id = c.getString(1);
                String isFav = String.valueOf(c.getString(2));
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
    public String findfavBy(int id) {
        String isFav;
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        DramaInfo dramaInfo = null;
        Log.d("","Test   findfavBy id "+id);


        ArrayList<DramaInfo> dramaInfos=new ArrayList<DramaInfo>();
            Cursor c = db.rawQuery("SELECT * FROM favourite where "+ SQLiteHelper.COLUMN_DRAMA_ID +" = ? ",
                    new String[]{String.valueOf(id)});
        if(c != null && c.getCount() > 0)
        {
            c.moveToFirst();
            isFav = c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_ISFAV));
        }
        else
        {
            isFav = "false";
        }
        Log.d("","Test   findfavBy isFav "+isFav);

        return isFav;
    }
}
