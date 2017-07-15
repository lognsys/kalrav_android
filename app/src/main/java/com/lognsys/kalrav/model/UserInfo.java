package com.lognsys.kalrav.model;

import java.io.Serializable;

/**
 * Created by pdoshi on 27/12/16.
 */

public class UserInfo implements Serializable {


    //User fields
    // TODO : Pending phoneNo & zipcode validation

    //user table
    public static final String TABLE_USER = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_FBID = "fb_id";
    public static final String COLUMN_USER_GOOGLEID = "google_id";
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_USER_REALNAME = "realname";
    public static final String COLUMN_USER_PHONENO = "phone";
    public static final String COLUMN_USER_PROVENANCE = "provenance";
    public static final String COLUMN_USER_BIRTHDAY = "birthday";
    public static final String COLUMN_USER_ENABLED = "enabled";
    public static final String COLUMN_USER_IS_NOTIFICATION = "notification";
    public static final String COLUMN_USER_DEVICE_TOKEN = "device";
    public static final String COLUMN_USER_ADDRESS = "address";
    public static final String COLUMN_USER_CITY = "city";
    public static final String COLUMN_USER_STATE = "state";
    public static final String COLUMN_USER_ZIPCODE = "zipcode";
    public static final String COLUMN_USER_ROLE = "role";
    public static final String COLUMN_USER_GROUP_NAME = "groupname";
    public static final String COLUMN_USER_PROFILELINK = "profile_link";
    public static final String COLUMN_USER_LOGGEDIN = "logged";
    public static final String COLUMN_USER_LOCATION = "location";

    private boolean enabled;
    private boolean notification;
    private String device;
    private String provenance;
    private int id;
    private String phoneNo;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private String google_id;
    private String location;
    private String name;
    private String email;
    private String birthday;
    private int loggedIn;
    private String groupname;
    private String picture;
    private String fb_id;
    private String role;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }



    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }



    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }


    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(int loggedIn) {
        this.loggedIn = loggedIn;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public UserInfo(){
        //no-arg constructor
    }

    public UserInfo(int id,String phoneNo, String address, String city, String state,  String zipcode, String fb_id, String location, String name, String email, String birthday, int loggedIn) {
        this.id = id;
        this.phoneNo = phoneNo;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.fb_id = fb_id;
        this.location = location;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.loggedIn = loggedIn;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "enabled=" + enabled +
                ", notification=" + notification +
                ", device='" + device + '\'' +
                ", provenance='" + provenance + '\'' +
                ", id=" + id +
                ", phoneNo='" + phoneNo + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", google_id='" + google_id + '\'' +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthday='" + birthday + '\'' +
                ", loggedIn=" + loggedIn +
                ", groupname='" + groupname + '\'' +
                ", picture='" + picture + '\'' +
                ", fb_id='" + fb_id + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
