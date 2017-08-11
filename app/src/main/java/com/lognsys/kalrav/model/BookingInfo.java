package com.lognsys.kalrav.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by admin on 4/19/2017.
 */

public class BookingInfo implements Serializable {


    //BookingInfo Table
    public static final String TABLE_TICKET = "ticket";
    //    public static final String COLUMN_DRAMA_ID = "drama_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_BOOKED_DATETIME = "booked_datetime";
    public static final String COLUMN_CONFIRMATION_CODE = "confirmation_code";
    public static final String COLUMN_SEAT_TOTAL_PRICE = "seats_total_price";
    public static final String COLUMN_NO_OF_SEATS_BOOKED = "seats_no_of_seats_booked";
    public static final String COLUMN_SEAT_NO = "seat_seat_no";
    public static final String COLUMN_AUDITORIUM_NAME = "auditorium_name";
    public static final String COLUMN_DRAMA_NAME = "drama_name";
    public static final String COLUMN_GROUP_NAME = "group_name";
    public static final String COLUMN_DRAMA_ID= "drama_id";
    public static final String COLUMN_LINK_PHOTO = "photo_link";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_EMAIL_ID = "user_emailid";
    public static final String COLUMN_DRAMA_DATETIME = "dramadatetime";
    public static final String COLUMN_ID = "id";

    public static final String COLUMN_QRCODE_BITMAP ="bitmapQRCode";













    int _id;
    int drama_id;
    int user_id;
    String drama_name;
    String drama_group_name;
    String drama_datetime;
    String drama_photo;

    String booked_datetime;

    String confirmation_code;

    double seats_total_price;
    String seats_no_of_seats_booked;
    String seart_seat_no;

    String auditorium_name;

    String user_name;
    String user_emailid;

    public Bitmap getBitmapQRCode() {
        return bitmapQRCode;
    }

    public void setBitmapQRCode(Bitmap bitmapQRCode) {
        this.bitmapQRCode = bitmapQRCode;
    }

    Bitmap bitmapQRCode;


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getDrama_id() {
        return drama_id;
    }

    public void setDrama_id(int drama_id) {
        this.drama_id = drama_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDrama_name() {
        return drama_name;
    }

    public void setDrama_name(String drama_name) {
        this.drama_name = drama_name;
    }

    public String getDrama_group_name() {
        return drama_group_name;
    }

    public void setDrama_group_name(String drama_group_name) {
        this.drama_group_name = drama_group_name;
    }


    public String getDrama_photo() {
        return drama_photo;
    }

    public void setDrama_photo(String drama_photo) {
        this.drama_photo = drama_photo;
    }

    public String getConfirmation_code() {
        return confirmation_code;
    }

    public void setConfirmation_code(String confirmation_code) {
        this.confirmation_code = confirmation_code;
    }

    public double getSeats_total_price() {
        return seats_total_price;
    }

    public void setSeats_total_price(double seats_total_price) {
        this.seats_total_price = seats_total_price;
    }

    public String getSeats_no_of_seats_booked() {
        return seats_no_of_seats_booked;
    }

    public void setSeats_no_of_seats_booked(String seats_no_of_seats_booked) {
        this.seats_no_of_seats_booked = seats_no_of_seats_booked;
    }

    public String getSeart_seat_no() {
        return seart_seat_no;
    }

    public void setSeart_seat_no(String seart_seat_no) {
        this.seart_seat_no = seart_seat_no;
    }

    public String getAuditorium_name() {
        return auditorium_name;
    }

    public void setAuditorium_name(String auditorium_name) {
        this.auditorium_name = auditorium_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_emailid() {
        return user_emailid;
    }

    public void setUser_emailid(String user_emailid) {
        this.user_emailid = user_emailid;
    }

    public String getDrama_datetime() {
        return drama_datetime;
    }

    public void setDrama_datetime(String drama_datetime) {
        this.drama_datetime = drama_datetime;
    }

    public String getBooked_datetime() {
        return booked_datetime;
    }

    public void setBooked_datetime(String booked_datetime) {
        this.booked_datetime = booked_datetime;
    }

    @Override
    public String toString() {
        return "BookingInfo{" +
                "_id=" + _id +
                ", drama_id=" + drama_id +
                ", user_id=" + user_id +
                ", drama_name='" + drama_name + '\'' +
                ", drama_group_name='" + drama_group_name + '\'' +
                ", drama_datetime='" + drama_datetime + '\'' +
                ", drama_photo='" + drama_photo + '\'' +
                ", booked_datetime='" + booked_datetime + '\'' +
                ", confirmation_code='" + confirmation_code + '\'' +
                ", seats_total_price=" + seats_total_price +
                ", seats_no_of_seats_booked='" + seats_no_of_seats_booked + '\'' +
                ", seart_seat_no='" + seart_seat_no + '\'' +
                ", auditorium_name='" + auditorium_name + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_emailid='" + user_emailid + '\'' +
                ", bitmapQRCode=" + bitmapQRCode +
                '}';
    }
}
