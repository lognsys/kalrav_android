package com.lognsys.kalrav.model;

/**
 * Created by pdoshi on 09/01/17.
 */

public class NotificationInfo {

    //notification table
    public static final String TABLE_NOTIFICATION = "notification";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTIFICATION_DRAMA_ID = "dramaId";
    public static final String COLUMN_NOTIFICATION_USER_ID = "userId";
    public static final String COLUMN_NOTIFICATION_USER_REALNAME = "realname";
    public static final String COLUMN_NOTIFICATION_DRAMA_TITLE = "dramaTitle";
    public static final String COLUMN_NOTIFICATION_MESSAGE = "message";

    public static final String COLUMN_NOTIFICATION_CONFIRMATION_CODE = "confirmationCode";
    public static final String COLUMN_NOTIFICATION_BOOKING_ID = "bookingId";
    public static final String COLUMN_TIMESTAMP = "last_edit";
    private int _id ;
    private int dramaId ;
    private int userId;
    private String realname;
    private String dramaTitle;
    private String message;
    private int bookingId;
    private String confirmationCode;
    private String last_edit;


    public NotificationInfo(int _id, int dramaId, int userId, String realname, String dramaTitle, String message, String confirmationCode,int bookingId) {
        this._id = _id;
        this.dramaId = dramaId;
        this.userId = userId;
        this.realname = realname;
        this.dramaTitle = dramaTitle;
        this.message = message;
        this.confirmationCode = confirmationCode;
        this.bookingId = bookingId;
    }

    public NotificationInfo() {

    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getLast_edit() {
        return last_edit;
    }

    public void setLast_edit(String last_edit) {
        this.last_edit = last_edit;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getDramaId() {
        return dramaId;
    }

    public void setDramaId(int dramaId) {
        this.dramaId = dramaId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getDramaTitle() {
        return dramaTitle;
    }

    public void setDramaTitle(String dramaTitle) {
        this.dramaTitle = dramaTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NotificationInfo{" +
                "_id=" + _id +
                ", dramaId=" + dramaId +
                ", userId=" + userId +
                ", realname='" + realname + '\'' +
                ", dramaTitle='" + dramaTitle + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
