package com.lognsys.kalrav.util;

import com.lognsys.kalrav.model.NotificationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pdoshi on 11/01/17.
 */

public class MockObjects {

   private static final List<NotificationInfo> NOTIFICATION_INFO_LIST = new ArrayList<>();

    public MockObjects() {
        //no-arg constructor
    }

    public static List<NotificationInfo> getNotificationInfoList() {

//        if(NOTIFICATION_INFO_LIST.size() == 0) {
//
//            NOTIFICATION_INFO_LIST.add(new NotificationInfo("Dubai Trip", "Jan 10 2016", Constants.GENRE_TRIP, "Trip to Dubai for 5 nights / 6 days", "Dubai trip includes tickets to zoo, ferari world, Buj Al arab and latest description"));
//            NOTIFICATION_INFO_LIST.add(new NotificationInfo("Hu Foundation", "Jan 09 2016", Constants.GENRE_CHARITY, "Moto - Every child should be educated..", "This foundation aims at educating every single child and abolish child slavery"));
//            NOTIFICATION_INFO_LIST.add(new NotificationInfo("Drama announcement", "Jan 08 2016", Constants.GENRE_GENERAL, "Drama Tickets are available.", "Drama will be help in Aspee. Tickets should be collected at counter.. 20 tickets available for guest"));
//        }

        return NOTIFICATION_INFO_LIST;
    }


}
