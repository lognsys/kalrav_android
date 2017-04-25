package com.lognsys.kalrav.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by admin on 4/19/2017.
 */

public class TicketsInfo implements Serializable {
    int _id;
    int drama_id;
    int user_id;
    String drama_name;
    String drama_group_name;
    String drama_time;
    String drama_date;
    String drama_photo;

    String booked_time;
    String booked_date;

    String confirmation_code;

    String seats_total_price;
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

    public String getDrama_time() {
        return drama_time;
    }

    public void setDrama_time(String drama_time) {
        this.drama_time = drama_time;
    }

    public String getDrama_date() {
        return drama_date;
    }

    public void setDrama_date(String drama_date) {
        this.drama_date = drama_date;
    }

    public String getDrama_photo() {
        return drama_photo;
    }

    public void setDrama_photo(String drama_photo) {
        this.drama_photo = drama_photo;
    }

    public String getBooked_time() {
        return booked_time;
    }

    public void setBooked_time(String booked_time) {
        this.booked_time = booked_time;
    }

    public String getBooked_date() {
        return booked_date;
    }

    public void setBooked_date(String booked_date) {
        this.booked_date = booked_date;
    }

    public String getConfirmation_code() {
        return confirmation_code;
    }

    public void setConfirmation_code(String confirmation_code) {
        this.confirmation_code = confirmation_code;
    }

    public String getSeats_total_price() {
        return seats_total_price;
    }

    public void setSeats_total_price(String seats_total_price) {
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
}
