package com.lognsys.kalrav.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.lognsys.kalrav.model.BookingInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by pdoshi on 27/12/16.
 */

public class BookingInfoDAOImpl implements BookingInfoDAO {

    private final String TAG = getClass().getSimpleName();
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    public BookingInfoDAOImpl(Context context) {

        sqLiteHelper = new SQLiteHelper(context);
        db = sqLiteHelper.getWritableDatabase();
    }



    @Override
    public void addTicket(BookingInfo bookingInfo) {
        db = sqLiteHelper.getWritableDatabase();

        // If user exists update user
        if (bookingInfo.get_id()!=0) {

            updateTicket(bookingInfo);

        } else {  //Create user if new

            ContentValues values = new ContentValues();
            values.put(BookingInfo.COLUMN_DRAMA_ID, bookingInfo.getDrama_id());
            values.put(BookingInfo.COLUMN_USER_ID, bookingInfo.getUser_id());
            values.put(BookingInfo.COLUMN_DRAMA_NAME, bookingInfo.getDrama_name());
            values.put(BookingInfo.COLUMN_GROUP_NAME, bookingInfo.getDrama_group_name());
            values.put(BookingInfo.COLUMN_LINK_PHOTO, bookingInfo.getDrama_photo());
            values.put(BookingInfo.COLUMN_DATETIME, bookingInfo.getDrama_date());
            values.put(BookingInfo.COLUMN_DRAMA_TIME, bookingInfo.getDrama_time());
            values.put(BookingInfo.COLUMN_BOOKED_TIME, bookingInfo.getBooked_time());
            values.put(BookingInfo.COLUMN_BOOKED_DATE, bookingInfo.getBooked_date());
            values.put(BookingInfo.COLUMN_CONFIRMATION_CODE, bookingInfo.getConfirmation_code());
            values.put(BookingInfo.COLUMN_SEAT_TOTAL_PRICE, bookingInfo.getSeats_total_price());
            values.put(BookingInfo.COLUMN_NO_OF_SEATS_BOOKED, bookingInfo.getSeats_no_of_seats_booked());
            values.put(BookingInfo.COLUMN_SEAT_NO, bookingInfo.getSeart_seat_no());
            values.put(BookingInfo.COLUMN_AUDITORIUM_NAME, bookingInfo.getAuditorium_name());
            values.put(BookingInfo.COLUMN_USER_NAME, bookingInfo.getUser_name());
            values.put(BookingInfo.COLUMN_USER_EMAIL_ID, bookingInfo.getUser_emailid());
            Log.d("","Test insert bookingInfo.getBitmapQRCode() "+ bookingInfo.getBitmapQRCode());
            Log.d("","Test insert BitMapToString(bookingInfo.getBitmapQRCode()) "+ BitMapToString(bookingInfo.getBitmapQRCode()));

            values.put(BookingInfo.COLUMN_QRCODE_BITMAP, BitMapToString(bookingInfo.getBitmapQRCode()));

            // Inserting Row
            db.insert(BookingInfo.TABLE_TICKET, null, values);
            db.close(); // Closing database connection

        }
    }

    @Override
    public boolean isTicketExist(BookingInfo bookingInfo) {
            db = sqLiteHelper.getReadableDatabase();
        Log.d(TAG, "isTicketExist bookingInfo.get_id() "+ bookingInfo.get_id());

        Cursor cur = db.rawQuery("SELECT * FROM ticket where "+ BookingInfo.COLUMN_ID+" = ? ",
                    new String[]{String.valueOf(bookingInfo.get_id())});
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
    public int updateTicket(BookingInfo bookingInfo) {

        Log.d(TAG, "updateTicket - " );

        db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BookingInfo.COLUMN_DRAMA_ID, bookingInfo.getDrama_id());
        values.put(BookingInfo.COLUMN_USER_ID, bookingInfo.getUser_id());
        values.put(BookingInfo.COLUMN_DRAMA_NAME, bookingInfo.getDrama_name());
        values.put(BookingInfo.COLUMN_GROUP_NAME, bookingInfo.getDrama_group_name());
        values.put(BookingInfo.COLUMN_LINK_PHOTO, bookingInfo.getDrama_photo());
        values.put(BookingInfo.COLUMN_DATETIME, bookingInfo.getDrama_date());
        values.put(BookingInfo.COLUMN_DRAMA_TIME, bookingInfo.getDrama_time());
        values.put(BookingInfo.COLUMN_BOOKED_TIME, bookingInfo.getBooked_time());
        values.put(BookingInfo.COLUMN_BOOKED_DATE, bookingInfo.getBooked_date());
        values.put(BookingInfo.COLUMN_CONFIRMATION_CODE, bookingInfo.getConfirmation_code());
        values.put(BookingInfo.COLUMN_SEAT_TOTAL_PRICE, bookingInfo.getSeats_total_price());
        values.put(BookingInfo.COLUMN_NO_OF_SEATS_BOOKED, bookingInfo.getSeats_no_of_seats_booked());
        values.put(BookingInfo.COLUMN_SEAT_NO, bookingInfo.getSeart_seat_no());
        values.put(BookingInfo.COLUMN_AUDITORIUM_NAME, bookingInfo.getAuditorium_name());
        values.put(BookingInfo.COLUMN_USER_NAME, bookingInfo.getUser_name());
        values.put(BookingInfo.COLUMN_USER_EMAIL_ID, bookingInfo.getUser_emailid());
        Log.d("","Test updateTicket bookingInfo.getBitmapQRCode() "+ bookingInfo.getBitmapQRCode());
        Log.d("","Test updateTicket BitMapToString(bookingInfo.getBitmapQRCode()) "+ BitMapToString(bookingInfo.getBitmapQRCode()));

        values.put(BookingInfo.COLUMN_QRCODE_BITMAP, BitMapToString(bookingInfo.getBitmapQRCode()));

        // updating row
        return db.update(BookingInfo.TABLE_TICKET, values, BookingInfo.COLUMN_ID + " = ?",
                new String[]{String.valueOf(bookingInfo.get_id())});

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
    public ArrayList<BookingInfo> getAllTicket() {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        BookingInfo bookingInfo = null;
        Log.d("","Test getAllTicket  ");


        ArrayList<BookingInfo> bookingInfos =new ArrayList<BookingInfo>();
        Cursor c = db.rawQuery("SELECT * FROM ticket ", null);
        if(c!=null)
        Log.d("","Test getAllTicket  c.getCount() "+ c.getCount());

        if (c != null && c.getCount()>0) {

            while (c.moveToNext()){
                bookingInfo = new BookingInfo();

                bookingInfo.set_id(Integer.parseInt(c.getString(c.getColumnIndex(BookingInfo.COLUMN_ID)).toString()));
                bookingInfo.setDrama_id(Integer.parseInt(c.getString(c.getColumnIndex(BookingInfo.COLUMN_DRAMA_ID)).toString()));
                bookingInfo.setUser_id(Integer.parseInt(c.getString(c.getColumnIndex(BookingInfo.COLUMN_USER_ID)).toString()));
                bookingInfo.setDrama_name(c.getString(c.getColumnIndex(BookingInfo.COLUMN_DRAMA_NAME)).toString());
               if(c.getString(c.getColumnIndex(BookingInfo.COLUMN_GROUP_NAME))!=null){
                   bookingInfo.setDrama_group_name(c.getString(c.getColumnIndex(BookingInfo.COLUMN_GROUP_NAME)).toString());

               }
               else{
                   bookingInfo.setDrama_group_name("None");

               }
                bookingInfo.setDrama_photo(c.getString(c.getColumnIndex(BookingInfo.COLUMN_LINK_PHOTO)).toString());
//                if(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DATETIME))!=null)
//                bookingInfo.setDrama_date(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DATETIME)));
//
//                bookingInfo.setDrama_time(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DRAMA_TIME)).toString());
                bookingInfo.setBooked_time(c.getString(c.getColumnIndex(BookingInfo.COLUMN_BOOKED_TIME)).toString());
                bookingInfo.setBooked_date(c.getString(c.getColumnIndex(BookingInfo.COLUMN_BOOKED_DATE)).toString());
                bookingInfo.setConfirmation_code(c.getString(c.getColumnIndex(BookingInfo.COLUMN_CONFIRMATION_CODE)).toString());
                bookingInfo.setSeats_total_price(c.getString(c.getColumnIndex(BookingInfo.COLUMN_SEAT_TOTAL_PRICE)).toString());
                bookingInfo.setSeats_no_of_seats_booked(c.getString(c.getColumnIndex(BookingInfo.COLUMN_NO_OF_SEATS_BOOKED)).toString());
                bookingInfo.setSeart_seat_no(c.getString(c.getColumnIndex(BookingInfo.COLUMN_SEAT_NO)).toString());
                bookingInfo.setAuditorium_name(c.getString(c.getColumnIndex(BookingInfo.COLUMN_AUDITORIUM_NAME)).toString());
                bookingInfo.setUser_name(c.getString(c.getColumnIndex(BookingInfo.COLUMN_USER_NAME)).toString());
                bookingInfo.setUser_emailid(c.getString(c.getColumnIndex(BookingInfo.COLUMN_USER_EMAIL_ID)).toString());
                Log.d("","Test getAllTicket SQLiteHelper.COLUMN_QRCODE_BITMAP "+c.getString(c.getColumnIndex(BookingInfo.COLUMN_QRCODE_BITMAP)).toString());
                Log.d("","Test getAllTicket StringToBitMap(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_QRCODE_BITMAP)).toString()) "+StringToBitMap(c.getString(c.getColumnIndex(BookingInfo.COLUMN_QRCODE_BITMAP)).toString()));

                bookingInfo.setBitmapQRCode(StringToBitMap(c.getString(c.getColumnIndex(BookingInfo.COLUMN_QRCODE_BITMAP)).toString()));
                Log.d("","Test getAllTicket bookingInfo.getBitmapQRCode() "+ bookingInfo.getBitmapQRCode());

                bookingInfos.add(bookingInfo);
            }
            Log.d("","Test getAllTicket bookingInfos.size "+ bookingInfos.size());

            c.close();
            return bookingInfos;
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


}
