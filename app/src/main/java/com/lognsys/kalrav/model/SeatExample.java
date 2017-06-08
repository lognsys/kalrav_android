package com.lognsys.kalrav.model;

import android.graphics.Color;

import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.Seat;

/**
 * Created by Nublo on 05.12.2015.
 * Copyright Nublo
 */
public class SeatExample implements Seat {

    public int id;
    public int color = Color.RED;
    public String marker;
    public String selectedSeatMarker;
    public HallScheme.SeatStatus status;
    public int total;
    public int price;
    public int irow;
    public int jrow;
    public String active ;

    public SeatExample(int irow, int jrow, String active) {
    this.irow=irow;
    this.jrow=jrow;
    this.active=active;

    }

    public SeatExample() {

    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public int getIrow() {
        return irow;
    }

    public void setIrow(int irow) {
        this.irow = irow;
    }

    public int getJrow() {
        return jrow;
    }

    public void setJrow(int jrow) {
        this.jrow = jrow;
    }

    @Override
    public void setTotal(int total) {
        this.total=total;
    }

    @Override
    public int getTotal() {
        return this.total;
    }

    @Override
    public int price() {
        return price;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public int color() {
        return color;
    }

    @Override
    public String marker() {
        return marker;
    }

    @Override
    public String selectedSeat() {
        return selectedSeatMarker;
    }

    @Override
    public HallScheme.SeatStatus status() {
        return status;
    }

    @Override
    public void setStatus(HallScheme.SeatStatus status) {
        this.status = status;
    }

}