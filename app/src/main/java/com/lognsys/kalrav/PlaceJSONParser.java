package com.lognsys.kalrav;

/**
 * Created by admin on 4/11/2017.
 */

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PlaceJSONParser {

    /** Receives a JSONObject and returns a list */
    public List<String> parse(JSONObject jObject){

        JSONArray jPlaces = null;
        try {
            /** Retrieves all the elements in the 'places' array */
            jPlaces = jObject.getJSONArray("predictions");
            Log.d("","Test description jPlaces "+jPlaces.length());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getPlaces with the array of json object
         * where each json object represent a place
         */
        return getPlaces(jPlaces);
    }


    private List<String> getPlaces(JSONArray jPlaces){
        int placesCount = jPlaces.length();
        List<String> placesList = new ArrayList<String>();
        List<String> place = null;

        /** Taking each place, parses and adds to list object */
        for(int i=0; i<placesCount;i++){
            try {
                /** Call getPlace with place JSON object to parse the place */
                Log.d("","Test getPlace((JSONObject)jPlaces.get(i)) "+ getPlace((JSONObject)jPlaces.get(i)));
                place = getPlace((JSONObject)jPlaces.get(i));
                Log.d("","Test place "+ place);
                for(String placeCity: place){
                    placesList.add(placeCity);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placesList;
    }

    /** Parsing the Place JSON object */
    private List<String> getPlace(JSONObject jPlace){

        List<String> place = new ArrayList<String>();

        String id="";
        String reference="";
        String description="";

        try {

            description = jPlace.getString("description");
//            id = jPlace.getString("id");
//            reference = jPlace.getString("reference");

            place.add(description);
//            place.put("_id",id);
//            place.put("reference",reference);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}
