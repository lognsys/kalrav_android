package com.lognsys.kalrav.model;

import java.util.List;

/**
 * Created by admin on 4/17/2017.
 */

public class Auditorium {
    private int dramaId;
    private String audiId;
    private String audiName;
    private String audiRowName;
    private List<String> seats;
    private String price;
    private String datetime;

    public int getDramaId() {
        return dramaId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setDramaId(int dramaId) {
        this.dramaId = dramaId;
    }

    public String getAudiId() {
        return audiId;
    }

    public void setAudiId(String audiId) {
        this.audiId = audiId;
    }

    public String getAudiName() {
        return audiName;
    }

    public void setAudiName(String audiName) {
        this.audiName = audiName;
    }

    public String getAudiRowName() {
        return audiRowName;
    }

    public void setAudiRowName(String audiRowName) {
        this.audiRowName = audiRowName;
    }

    public List<String> getSeats() {
        return seats;
    }

    public void setSeats(List<String> seats) {
        this.seats = seats;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
