package com.lognsys.kalrav.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.db.DramaInfoDAOImpl;
import com.lognsys.kalrav.db.FavouritesInfoDAOImpl;
import com.lognsys.kalrav.fragment.DramaFragment;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.FavouritesInfo;
import com.lognsys.kalrav.util.KalravApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by admin on 5/20/2017.
 */

public class KalravDramaByGroupService extends Service {
    private final int UPDATE_INTERVAL = 60 * 1000;
    private Timer timer = new Timer();
    private  String GETALLDRAMA_AND_GROUP_URL="http://192.168.0.19:8080/getalldramaandgroup/";

    ArrayList<DramaInfo> dramaInfos;
    DramaInfoDAOImpl dramaInfoDAO;
    FavouritesInfoDAOImpl favouritesInfoDAOImpl;
    FavouritesInfo favouritesInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        // Code to execute when the service is first created
        Log.d("Service"," Service onCreate ");
//
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }

        Log.d("Service"," Service onDestroy ");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      super.onStartCommand(intent,flags,startId);
        Log.d("Service"," Service onStartCommand KalravApplication.getInstance().getPrefs().getCustomer_id() "+KalravApplication.getInstance().getPrefs().getCustomer_id());
        Log.d("Service"," Service onStartCommand KalravApplication.getInstance().getPrefs().getUser_Group_Name() "+KalravApplication.getInstance().getPrefs().getUser_Group_Name());
        if(KalravApplication.getInstance().getPrefs().getCustomer_id()!=null && KalravApplication.getInstance().getPrefs().getUser_Group_Name()!=null){
            String group_name=KalravApplication.getInstance().getPrefs().getUser_Group_Name();
            Log.d("Service"," Service onStartCommand ");
//            getDramaGroup();
            dramaInfoDAO = new DramaInfoDAOImpl(getApplicationContext());
            favouritesInfoDAOImpl = new FavouritesInfoDAOImpl(getApplicationContext());
            dramaInfos = new ArrayList<DramaInfo>() ;
            favouritesInfo=new FavouritesInfo();

            displayramaByGroup(group_name);
        }
        return Service.START_STICKY;
    }
    private void displayramaByGroup(String group_name) {

        Log.d("Service"," Service displayramaByGroup group_name ===== "+group_name);
//
        GETALLDRAMA_AND_GROUP_URL=GETALLDRAMA_AND_GROUP_URL+group_name;
        JsonArrayRequest req = new JsonArrayRequest(GETALLDRAMA_AND_GROUP_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            Log.d("Service"," Service displayramaByGroup responsetostring ===== "+response.toString());
                            Log.d("Service"," Service displayramaByGroup response length ===== "+response.length());
                            for (int i=0; i<response.length(); i++) {
                                DramaInfo dramaInfo=new DramaInfo();

                                JSONObject jsonObject= null;
                                try {
                                    jsonObject = (response.getJSONObject(i));
                                } catch (JSONException e) {
                                    Log.d("Service"," Service displayramaByGroup JSONException ===== "+e);
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),
                                            "Error: 1 " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                                String groups=jsonObject.getString("groups");
//                                Log.d("","displaydrama groups "+groups);

                                JSONObject jsonGroupsObject=new JSONObject(groups);
                                int groupId=jsonGroupsObject.getInt("id");
                                Log.d(""," Service displayramaByGroup jsonGroupsObject groupId "+groupId);

                                String group_name=jsonGroupsObject.getString("group_name");
                                Log.d(""," Service displayramaByGroup jsonGroupsObject group_name "+group_name);
                                dramaInfo.setGroup_name(group_name);



                                String drama=jsonObject.getString("drama");
                                Log.d(""," Service displayramaByGroup drama "+drama);
                                JSONObject jsonDramaObject=new JSONObject(drama);

                                int dramaId=jsonDramaObject.getInt("id");
                                Log.d(""," Service displayramaByGroup jsonDramaObject dramaId "+dramaId);
                                dramaInfo.setId(dramaId);

                                String title=jsonDramaObject.getString("title");
                                Log.d(""," Service displayramaByGroup jsonDramaObject title "+title);
                                dramaInfo.setTitle(title);

                                String imageurl=jsonDramaObject.getString("imageurl");
                                Log.d(""," Service displayramaByGroup jsonDramaObject imageurl "+imageurl);
                                dramaInfo.setLink_photo(imageurl);


                                if(dramaInfo!= null && dramaInfo.getId()!=0){
                                    String isFav=favouritesInfoDAOImpl.findfavBy(dramaInfo.getId());
                                    dramaInfo.setIsfav(isFav);
                                    dramaInfoDAO.addDrama(dramaInfo);

                                    dramaInfos= (ArrayList<DramaInfo>) dramaInfoDAO.getAllDrama();
                                    Log.d(""," Service displayramaByGroup onPost dramaInfos.size "+dramaInfos.size());
                                    if (dramaInfos.size() > 0 & dramaInfos != null) {

                                    }
                                }

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(""," Service displayramaByGroup JSonException Exception "+e);

                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.no_data_available),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(""," Service displayramaByGroup: volly Exception " + error);
                Toast.makeText(getApplicationContext(),
                        getString(R.string.unknown_error),
                        Toast.LENGTH_LONG).show();
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        KalravApplication.getInstance().addToRequestQueue(req);

    }



    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Service"," Service onBind ");
        //TODO for communication return IBinder implementation
        return null;
    }


}