package com.lognsys.kalrav.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.UserInfo;

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
    public boolean addDrama(DramaInfo dramaInfo) {
        return false;
    }

    @Override
    public boolean updateDrama() {
        return false;
    }

    @Override
    public boolean deleteDrama() {
        return false;
    }

    @Override
    public List<DramaInfo> findAllDrama() {
        return null;
    }

    @Override
    public List<DramaInfo> findDramaBy(String date) {
        return null;
    }
}
