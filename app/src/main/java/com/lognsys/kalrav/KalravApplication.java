package com.lognsys.kalrav;

import android.app.Application;

import com.lognsys.kalrav.model.UserInfo;

/**
 * Created by pdoshi on 03/01/17.
 */

public class KalravApplication extends Application {

    private UserInfo userInfo;

    public UserInfo getGlobalObject() {
        return this.userInfo;
    }

    public void setGlobalObject(UserInfo userInfo) {

        this.userInfo = userInfo;

    }




}
