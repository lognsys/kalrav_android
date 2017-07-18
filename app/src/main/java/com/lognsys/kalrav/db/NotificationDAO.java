package com.lognsys.kalrav.db;

import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.FavouritesInfo;
import com.lognsys.kalrav.model.NotificationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pdoshi on 27/12/16.
 */

public interface NotificationDAO {



//    add notificationInfo
    void addNotificationInfo(NotificationInfo notificationInfo);

    ArrayList<NotificationInfo> getAllNotificationInfo();


    int deleteNotificationInfo(int num_of_days);
}
