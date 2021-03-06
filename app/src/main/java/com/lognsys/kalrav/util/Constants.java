package com.lognsys.kalrav.util;

import com.lognsys.kalrav.R;

/**
 * Created by pdoshi on 05/01/17.
 */

public class Constants {

    //set facebook login/logout flag - 1/0
    public static final int LOG_IN = 1;
    public static final int LOG_OUT = 0;

    //NotificationInfo fragment Genre
    public static final String GENRE_TRIP = "Trip";
    public static final String GENRE_CHARITY = "Charity";
    public static final String GENRE_GENERAL = "Message";
    public static final String GENRE_DRAMA = "Drama";

    //default images to be used
    public static int notificationImages[] = {R.drawable.attention, R.mipmap.ic_launcher_lounger, R.mipmap.ic_launcher_charity};

    //Placeholder for Drama images & drama names
//    public static int dramaImages[] = {R.drawable.gujjubhai_great, R.drawable.gujjubhai_ghode_chadhiya, R.drawable.jalsa_karo_jayantilal};
//    public static String dramaNames[] = {"Gujjubhai the Great", "Gujjubhai Ghode Chadhiya", "Jalsa Karo Jayantilal"};
//    public static String dramaGroupNames[] = {"Youth", "Medium", "Comedy"};

    public class Shared {
    }

    public enum API_URL_USER {
        post_create_user_url,
        post_userdetails_already_exist_url,
        sendDeviceToken,
        put_update_user_url
    }
    public enum API_URL_DRAMA {
        get_alldrama_with_group_url,
        getdramadetailbyid,
        ratedrama
    }
    public enum API_URL_AUDITORIUM_LIST {
        getauditoriumlist
    }
    public enum API_URL_BOOKING {
        bookingconfirm,bookedseats
    }
}
