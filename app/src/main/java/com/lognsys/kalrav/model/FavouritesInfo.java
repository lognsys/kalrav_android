package com.lognsys.kalrav.model;

import java.io.Serializable;

/**
 * Created by pdoshi on 27/12/16.
 */

public class FavouritesInfo implements Serializable {


    //favourite table
    public static final String TABLE_FAVOURITE = "favourite";

    public static final String COLUMN_ID = "id";
    //    public static final String COLUMN_GROUP_NAME = "group_name";
    public static final String COLUMN_FAVOURITE_ID = "_id";
    public static final String COLUMN_DRAMA_ID = "drama_id";
    public static final String COLUMN_ISFAV = "isfav";


    private int id;

    public int getDrama_id() {
        return drama_id;
    }

    public void setDrama_id(int drama_id) {
        this.drama_id = drama_id;
    }

    public String isFav() {
        return isFav;
    }

    public void setFav(String fav) {
        isFav = fav;
    }

    private String isFav;
    private int drama_id;
    private String group_name;
    private String drama_name;
    private String link_photo;
    private String datetime;
    private String briefDescription;
    private String[] cast;
    private String genre;
    private String director;
    private String writer;
    private String music,drama_language,time;

    public String getDrama_language() {
        return drama_language;
    }

    public void setDrama_language(String drama_language) {
        this.drama_language = drama_language;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDrama_length() {
        return drama_length;
    }

    public void setDrama_length(String drama_length) {
        this.drama_length = drama_length;
    }

    private String drama_length;
    private int imageResourceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String[] getCast() {
        return cast;
    }

    public void setCast(String[] cast) {
        this.cast = cast;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }


    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }


    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getDrama_name() {
        return drama_name;
    }

    public void setDrama_name(String drama_name) {
        this.drama_name = drama_name;
    }

    public String getLink_photo() {
        return link_photo;
    }

    public void setLink_photo(String link_photo) {
        this.link_photo = link_photo;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }


}
