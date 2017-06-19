package com.lognsys.kalrav.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.FavouritesInfo;
import com.lognsys.kalrav.model.TicketsInfo;
import com.lognsys.kalrav.model.UserInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pdoshi on 27/12/16.
 */

public class TicketInfoDAOImpl implements TicketInfoDAO {

    private final String TAG = getClass().getSimpleName();
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    public TicketInfoDAOImpl(Context context) {

        sqLiteHelper = new SQLiteHelper(context);
        db = sqLiteHelper.getWritableDatabase();
    }



    @Override
    public void addTicket(TicketsInfo ticketsInfo) {
        db = sqLiteHelper.getWritableDatabase();

        // If user exists update user
        if (ticketsInfo.get_id()!=0) {

            updateTicket(ticketsInfo);

        } else {  //Create user if new

            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_DRAMA_ID, ticketsInfo.getDrama_id());
            values.put(SQLiteHelper.COLUMN_USER_ID, ticketsInfo.getUser_id());
            values.put(SQLiteHelper.COLUMN_DRAMA_NAME, ticketsInfo.getDrama_name());
            values.put(SQLiteHelper.COLUMN_GROUP_NAME, ticketsInfo.getDrama_group_name());
            values.put(SQLiteHelper.COLUMN_LINK_PHOTO, ticketsInfo.getDrama_photo());
            values.put(SQLiteHelper.COLUMN_DATETIME, ticketsInfo.getDrama_date());
            values.put(SQLiteHelper.COLUMN_DRAMA_TIME, ticketsInfo.getDrama_time());
            values.put(SQLiteHelper.COLUMN_BOOKED_TIME, ticketsInfo.getBooked_time());
            values.put(SQLiteHelper.COLUMN_BOOKED_DATE, ticketsInfo.getBooked_date());
            values.put(SQLiteHelper.COLUMN_CONFIRMATION_CODE, ticketsInfo.getConfirmation_code());
            values.put(SQLiteHelper.COLUMN_SEAT_TOTAL_PRICE, ticketsInfo.getSeats_total_price());
            values.put(SQLiteHelper.COLUMN_NO_OF_SEATS_BOOKED, ticketsInfo.getSeats_no_of_seats_booked());
            values.put(SQLiteHelper.COLUMN_SEAT_NO, ticketsInfo.getSeart_seat_no());
            values.put(SQLiteHelper.COLUMN_AUDITORIUM_NAME, ticketsInfo.getAuditorium_name());
            values.put(SQLiteHelper.COLUMN_USER_NAME, ticketsInfo.getUser_name());
            values.put(SQLiteHelper.COLUMN_USER_EMAIL_ID, ticketsInfo.getUser_emailid());
            Log.d("","Test insert ticketsInfo.getBitmapQRCode() "+ticketsInfo.getBitmapQRCode());
            Log.d("","Test insert BitMapToString(ticketsInfo.getBitmapQRCode()) "+ BitMapToString(ticketsInfo.getBitmapQRCode()));

            values.put(SQLiteHelper.COLUMN_QRCODE_BITMAP, BitMapToString(ticketsInfo.getBitmapQRCode()));

            // Inserting Row
            db.insert(SQLiteHelper.TABLE_TICKET, null, values);
            db.close(); // Closing database connection

        }
    }

    @Override
    public boolean isTicketExist(TicketsInfo ticketsInfo) {
            db = sqLiteHelper.getReadableDatabase();
        Log.d(TAG, "isTicketExist ticketsInfo.get_id() "+ticketsInfo.get_id());

        Cursor cur = db.rawQuery("SELECT * FROM ticket where "+SQLiteHelper.COLUMN_ID+" = ? ",
                    new String[]{String.valueOf(ticketsInfo.get_id())});
            if (cur != null && cur.getCount()>0) {
                Log.d(TAG, "isTicketExist cur "+cur.getCount());
                cur.moveToFirst();                       // Always one row returned.
                if (cur.getInt(0) == 0) {               // Zero count means empty table.
                    Log.d(TAG, "DB_isUserExists - NOT FOUND...");
                    return false;
                }
            }
            return false;

    }
    @Override
    public int updateTicket(TicketsInfo ticketsInfo) {

        Log.d(TAG, "updateTicket - " );

        db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_DRAMA_ID, ticketsInfo.getDrama_id());
        values.put(SQLiteHelper.COLUMN_USER_ID, ticketsInfo.getUser_id());
        values.put(SQLiteHelper.COLUMN_DRAMA_NAME, ticketsInfo.getDrama_name());
        values.put(SQLiteHelper.COLUMN_GROUP_NAME, ticketsInfo.getDrama_group_name());
        values.put(SQLiteHelper.COLUMN_LINK_PHOTO, ticketsInfo.getDrama_photo());
        values.put(SQLiteHelper.COLUMN_DATETIME, ticketsInfo.getDrama_date());
        values.put(SQLiteHelper.COLUMN_DRAMA_TIME, ticketsInfo.getDrama_time());
        values.put(SQLiteHelper.COLUMN_BOOKED_TIME, ticketsInfo.getBooked_time());
        values.put(SQLiteHelper.COLUMN_BOOKED_DATE, ticketsInfo.getBooked_date());
        values.put(SQLiteHelper.COLUMN_CONFIRMATION_CODE, ticketsInfo.getConfirmation_code());
        values.put(SQLiteHelper.COLUMN_SEAT_TOTAL_PRICE, ticketsInfo.getSeats_total_price());
        values.put(SQLiteHelper.COLUMN_NO_OF_SEATS_BOOKED, ticketsInfo.getSeats_no_of_seats_booked());
        values.put(SQLiteHelper.COLUMN_SEAT_NO, ticketsInfo.getSeart_seat_no());
        values.put(SQLiteHelper.COLUMN_AUDITORIUM_NAME, ticketsInfo.getAuditorium_name());
        values.put(SQLiteHelper.COLUMN_USER_NAME, ticketsInfo.getUser_name());
        values.put(SQLiteHelper.COLUMN_USER_EMAIL_ID, ticketsInfo.getUser_emailid());
        Log.d("","Test updateTicket ticketsInfo.getBitmapQRCode() "+ticketsInfo.getBitmapQRCode());
        Log.d("","Test updateTicket BitMapToString(ticketsInfo.getBitmapQRCode()) "+ BitMapToString(ticketsInfo.getBitmapQRCode()));

        values.put(SQLiteHelper.COLUMN_QRCODE_BITMAP, BitMapToString(ticketsInfo.getBitmapQRCode()));

        // updating row
        return db.update(SQLiteHelper.TABLE_TICKET, values, SQLiteHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(ticketsInfo.get_id())});

    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    @Override
    public boolean deleteTicket() {
        return false;
    }

    @Override
    public ArrayList<TicketsInfo> getAllTicket() {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        TicketsInfo ticketsInfo = null;
        Log.d("","Test getAllTicket  ");


        ArrayList<TicketsInfo> ticketsInfos=new ArrayList<TicketsInfo>();
        Cursor c = db.rawQuery("SELECT * FROM ticket ", null);
        if(c!=null)
        Log.d("","Test getAllTicket  c.getCount() "+ c.getCount());

        if (c != null && c.getCount()>0) {

            while (c.moveToNext()){
                ticketsInfo = new TicketsInfo();

                ticketsInfo.set_id(Integer.parseInt(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_ID)).toString()));
                ticketsInfo.setDrama_id(Integer.parseInt(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DRAMA_ID)).toString()));
                ticketsInfo.setUser_id(Integer.parseInt(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_USER_ID)).toString()));
                ticketsInfo.setDrama_name(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DRAMA_NAME)).toString());
               if(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_GROUP_NAME))!=null){
                   ticketsInfo.setDrama_group_name(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_GROUP_NAME)).toString());

               }
               else{
                   ticketsInfo.setDrama_group_name("None");

               }
                ticketsInfo.setDrama_photo(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_LINK_PHOTO)).toString());
//                if(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DATETIME))!=null)
//                ticketsInfo.setDrama_date(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DATETIME)));
//
//                ticketsInfo.setDrama_time(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DRAMA_TIME)).toString());
                ticketsInfo.setBooked_time(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_BOOKED_TIME)).toString());
                ticketsInfo.setBooked_date(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_BOOKED_DATE)).toString());
                ticketsInfo.setConfirmation_code(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_CONFIRMATION_CODE)).toString());
                ticketsInfo.setSeats_total_price(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_SEAT_TOTAL_PRICE)).toString());
                ticketsInfo.setSeats_no_of_seats_booked(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_NO_OF_SEATS_BOOKED)).toString());
                ticketsInfo.setSeart_seat_no(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_SEAT_NO)).toString());
                ticketsInfo.setAuditorium_name(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_AUDITORIUM_NAME)).toString());
                ticketsInfo.setUser_name(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_USER_NAME)).toString());
                ticketsInfo.setUser_emailid(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_USER_EMAIL_ID)).toString());
                Log.d("","Test getAllTicket SQLiteHelper.COLUMN_QRCODE_BITMAP "+c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_QRCODE_BITMAP)).toString());
                Log.d("","Test getAllTicket StringToBitMap(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_QRCODE_BITMAP)).toString()) "+StringToBitMap(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_QRCODE_BITMAP)).toString()));

                ticketsInfo.setBitmapQRCode(StringToBitMap(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_QRCODE_BITMAP)).toString()));
                Log.d("","Test getAllTicket ticketsInfo.getBitmapQRCode() "+ticketsInfo.getBitmapQRCode());

                ticketsInfos.add(ticketsInfo);
            }
            Log.d("","Test getAllTicket ticketsInfos.size "+ticketsInfos.size());

            c.close();
            return ticketsInfos;
        }
        else{
            return  null;
        }

    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }


 /*   public ArrayList<TicketsInfo> getTicketListById(int favouritesInfo) {

        Log.d("","Test getTicketListById favouritesInfo "+favouritesInfo);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        TicketsInfo ticketsInfo = null;


        ArrayList<TicketsInfo> ticketsInfos=new ArrayList<TicketsInfo>();
        Cursor c = db.rawQuery("SELECT * FROM ticket where "+ SQLiteHelper.COLUMN_ID +" = ? ",
                new String[]{String.valueOf(favouritesInfo)});

        if(c!=null)
            Log.d("","Test getTicketListById  c.getCount() "+ c.getCount());

        if (c != null && c.getCount()>0) {

            while (c.moveToNext()){
                ticketsInfo = new TicketsInfo();

                ticketsInfo.set_id(Integer.parseInt(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_ID)).toString()));
                Log.d("","Test getTicketListById ticketsInfo.getID "+ ticketsInfo.get_id());
                ticketsInfo.setDrama_id(Integer.parseInt(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DRAMA_ID)).toString()));
                ticketsInfo.setUser_id(Integer.parseInt(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_USER_ID)).toString()));
                ticketsInfo.setDrama_name(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DRAMA_NAME)).toString());
                ticketsInfo.setDrama_group_name(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_GROUP_NAME)).toString());
                ticketsInfo.setDrama_photo(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_LINK_PHOTO)).toString());
                ticketsInfo.setDrama_date(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DATETIME)).toString());
                ticketsInfo.setDrama_time(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DRAMA_TIME)).toString());
                ticketsInfo.setBooked_time(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_BOOKED_TIME)).toString());
                ticketsInfo.setBooked_date(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_BOOKED_DATE)).toString());
                ticketsInfo.setConfirmation_code(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_CONFIRMATION_CODE)).toString());
                ticketsInfo.setSeats_total_price(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_SEAT_TOTAL_PRICE)).toString());
                ticketsInfo.setSeats_no_of_seats_booked(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_NO_OF_SEATS_BOOKED)).toString());
                ticketsInfo.setSeart_seat_no(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_SEAT_NO)).toString());
                ticketsInfo.setAuditorium_name(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_AUDITORIUM_NAME)).toString());
                ticketsInfo.setUser_name(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_USER_NAME)).toString());
                ticketsInfo.setUser_emailid(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_USER_EMAIL_ID)).toString());

                ticketsInfos.add(ticketsInfo);
            }
            Log.d("","Test getTicketListByFavId dramaInfos.size "+ticketsInfos.size());

            c.close();
        }
        return ticketsInfos;
    }*/

}
