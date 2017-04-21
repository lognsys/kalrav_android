package com.lognsys.kalrav.db;

import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.FavouritesInfo;
import com.lognsys.kalrav.model.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by pdoshi on 27/12/16.
 */

public interface FavouritesInfoDAO {



//    boolean addDrama(DramaInfo dramaInfo);
    void addFav(FavouritesInfo favouritesInfo);

//    boolean updateDrama();
    int updateFav(FavouritesInfo favouritesInfo);

//    boolean deleteDrama();
    int deleteFav(FavouritesInfo favouritesInfo);

    List<FavouritesInfo> getAllFav();

//    List<DramaInfo> findDramaBy(String date);
FavouritesInfo findfavBy(FavouritesInfo favouritesInfo);
    boolean isFavExits(FavouritesInfo favouritesInfo);
}
