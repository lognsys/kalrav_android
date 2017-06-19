package com.lognsys.kalrav.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 4/17/2017.
 */

public class Auditorium implements Serializable {
    private int dramaId;
    private String audiId;
    private String audiName;
    private String audiRowName;
    private List<String> seats;
    private ArrayList<AuditoriumPriceRange> auditoriumPriceRanges;
    private String price;
    private String datetime;

    public ArrayList<AuditoriumPriceRange> getAuditoriumPriceRanges() {
        return auditoriumPriceRanges;
    }

    public void setAuditoriumPriceRanges(ArrayList<AuditoriumPriceRange> auditoriumPriceRanges) {
        this.auditoriumPriceRanges = auditoriumPriceRanges;
    }

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
