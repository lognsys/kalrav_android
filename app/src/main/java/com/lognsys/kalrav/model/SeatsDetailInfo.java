package com.lognsys.kalrav.model;

import java.util.List;

/**
 * Created by admin on 8/18/2017.
 */

public class SeatsDetailInfo {
    List<SeatExample> itemsList;

    int dramaInfoId;
    String time,strDate;

    Auditorium auditorium;

    public List<SeatExample> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<SeatExample> itemsList) {
        this.itemsList = itemsList;
    }

    public int getDramaInfoId() {
        return dramaInfoId;
    }

    public void setDramaInfoId(int dramaInfoId) {
        this.dramaInfoId = dramaInfoId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public Auditorium getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(Auditorium auditorium) {
        this.auditorium = auditorium;
    }
}
