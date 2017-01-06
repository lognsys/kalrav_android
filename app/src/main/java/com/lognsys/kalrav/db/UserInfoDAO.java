package com.lognsys.kalrav.db;

import com.lognsys.kalrav.model.UserInfo;

/**
 * Created by pdoshi on 02/01/17.
 */

public interface UserInfoDAO {

    void addUser(UserInfo userInfo);

    UserInfo findUserBy(UserInfo userEmail);

    int updateUserInfo(UserInfo userInfo);

    boolean deleteUser(UserInfo userInfo);

    boolean isUserExist(UserInfo user);

    boolean isLoggedIn(UserInfo user);

    public int logOut(UserInfo user);

    public UserInfo lastUserLoggedIn();

}
