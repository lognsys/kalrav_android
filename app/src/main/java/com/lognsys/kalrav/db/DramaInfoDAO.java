package com.lognsys.kalrav.db;

import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by pdoshi on 27/12/16.
 */

public interface DramaInfoDAO {



    boolean addDrama(DramaInfo dramaInfo);

    boolean updateDrama();

    boolean deleteDrama();

    List<DramaInfo> findAllDrama();

    List<DramaInfo> findDramaBy(String date);



}
