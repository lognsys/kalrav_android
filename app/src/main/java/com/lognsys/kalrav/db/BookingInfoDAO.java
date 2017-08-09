package com.lognsys.kalrav.db;

import com.lognsys.kalrav.model.BookingInfo;

import java.util.ArrayList;

/**
 * Created by pdoshi on 27/12/16.
 */

public interface BookingInfoDAO {



    void addTicket(BookingInfo bookingInfo);

    int updateTicket(BookingInfo bookingInfo);

    boolean deleteTicket();

    ArrayList<BookingInfo> getAllTicket();

   // List<BookingInfo>  getTicketListById(int id);

    boolean isTicketExist(BookingInfo bookingInfo);

}
