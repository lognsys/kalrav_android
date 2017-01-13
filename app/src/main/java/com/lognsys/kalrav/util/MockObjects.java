package com.lognsys.kalrav.util;

import com.lognsys.kalrav.model.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pdoshi on 11/01/17.
 */

public class MockObjects {

   private static final List<Notification> notificationList = new ArrayList<>();

    public MockObjects() {
        //no-arg constructor
    }

    public static List<Notification> getNotificationList() {

        if(notificationList.size() == 0) {

            notificationList.add(new Notification("Dubai Trip", "Jan 10 2016", Constants.GENRE_TRIP, "Trip to Dubai for 5 nights / 6 days", "Dubai trip includes tickets to zoo, ferari world, Buj Al arab and latest description"));
            notificationList.add(new Notification("Hu Foundation", "Jan 09 2016", Constants.GENRE_CHARITY, "Moto - Every child should be educated..", "This foundation aims at educating every single child and abolish child slavery"));
            notificationList.add(new Notification("Drama announcement", "Jan 08 2016", Constants.GENRE_GENERAL, "Drama Tickets are available.", "Drama will be help in Aspee. Tickets should be collected at counter.. 20 tickets available for guest"));
        }

        return notificationList;
    }


}
