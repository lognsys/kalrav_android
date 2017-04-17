package com.lognsys.kalrav.model;

import java.io.Serializable;

/**
 * Created by admin on 4/14/2017.
 */

public class TimeSlot implements Serializable {
    private String dramaId;
    private String dateSlot;
    private String timeSlot;

  /*  public TimeSlot(String dramaId, String dateSlot, String timeSlot) {
        this.dramaId = dramaId;
        this.dateSlot = dateSlot;
        this.timeSlot = timeSlot;
    }
*/

    public String getDramaId() {
        return dramaId;
    }

    public void setDramaId(String dramaId) {
        this.dramaId = dramaId;
    }

    public String getDateSlot() {
        return dateSlot;
    }

    public void setDateSlot(String dateSlot) {
        this.dateSlot = dateSlot;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }


}
