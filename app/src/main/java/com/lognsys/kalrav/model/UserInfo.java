package com.lognsys.kalrav.model;

/**
 * Created by pdoshi on 27/12/16.
 */

public class UserInfo {


    //User fields
    // TODO : Pending phoneNo & zipcode validation
    private String phoneNo;
    private String address;
    private String city;
    private String state;
    private String zipcode;


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


    //facebook fields
    private String fb_id;
    private String location;
    private String name;
    private String email;
    private String birthday;
    private int loggedIn;

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

    public UserInfo(String phoneNo, String address, String city, String state, String zipcode, String fb_id, String location, String name, String email, String birthday, int loggedIn) {
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
                "phoneNo='" + phoneNo + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", fb_id='" + fb_id + '\'' +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthday='" + birthday + '\'' +
                ", loggedIn=" + loggedIn +
                '}';
    }
}
