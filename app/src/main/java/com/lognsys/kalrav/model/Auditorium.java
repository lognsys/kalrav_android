package com.lognsys.kalrav.model;

import java.util.List;

/**
 * Created by admin on 4/17/2017.
 */

public class Auditorium {
    private String audiId;
    private String audiName;
    private String audiRowName;
    private List<String> seats;
    private String price;

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
