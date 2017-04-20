package com.lognsys.kalrav.db;

import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.FavouritesInfo;
import com.lognsys.kalrav.model.TicketsInfo;
import com.lognsys.kalrav.model.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pdoshi on 27/12/16.
 */

public interface TicketInfoDAO {



    void addTicket(TicketsInfo ticketsInfo);

    int updateTicket(TicketsInfo ticketsInfo);

    boolean deleteTicket();

    ArrayList<TicketsInfo> getAllTicket();

    List<TicketsInfo>  getTicketListByFavId(int id);

    boolean isTicketExist(TicketsInfo ticketsInfo);

}
