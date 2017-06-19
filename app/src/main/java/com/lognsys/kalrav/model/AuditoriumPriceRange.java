package com.lognsys.kalrav.model;

import java.util.List;

/**
 * Created by admin on 4/17/2017.
 */

public class AuditoriumPriceRange {
    private int istart;
    private int iend;
    private int price;
    private String audiId;
    public AuditoriumPriceRange() {
    }

    public AuditoriumPriceRange(int istart, int iend, int price) {
        this.istart = istart;
        this.iend = iend;
        this.price = price;
    }

    @Override
    public String toString() {
        return "AuditoriumPriceRange{" +
                "istart=" + istart +
                ", iend=" + iend +
                ", price=" + price +
                '}';
    }

    public String getAudiId() {
        return audiId;
    }

    public void setAudiId(String audiId) {
        this.audiId = audiId;
    }

    public int getIstart() {
        return istart;
    }

    public void setIstart(int istart) {
        this.istart = istart;
    }

    public int getIend() {
        return iend;
    }

    public void setIend(int iend) {
        this.iend = iend;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
