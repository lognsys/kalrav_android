package com.lognsys.kalrav.model;

import java.io.Serializable;

/**
 * Created by pdoshi on 27/12/16.
 */

public class UserInfo implements Serializable {


    //User fields
    // TODO : Pending phoneNo & zipcode validation

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
                "id=" + id +
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
                '}';
    }
}
