package com.lognsys.kalrav.db;

import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.FavouritesInfo;
import com.lognsys.kalrav.model.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pdoshi on 27/12/16.
 */

public interface DramaInfoDAO {



//    boolean addDrama(DramaInfo dramaInfo);
    void addDrama(DramaInfo dramaInfo);

//    boolean updateDrama();
    int updateDrama(DramaInfo dramaInfo);

    boolean deleteDrama();

    List<DramaInfo> getAllDrama();

//    List<DramaInfo> findDramaBy(String date);
List<DramaInfo>  getDramaListByFavId(ArrayList<FavouritesInfo> favouritesInfos);

    boolean isDramaExist(DramaInfo dramaInfo);

    List<DramaInfo> getAllDramaByUserGroup(String group_name);


    DramaInfo getDramaByDramaId(int id);

    int getDramaCount();
}
