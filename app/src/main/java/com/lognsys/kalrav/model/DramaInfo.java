package com.lognsys.kalrav.model;

import java.io.Serializable;

/**
 * Created by pdoshi on 27/12/16.
 */

public class DramaInfo implements Serializable {

    //Drama TABLE
    public static final String TABLE_DRAMA = "drama";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_GROUP_NAME = "group_name";
    public static final String COLUMN_DRAMA_NAME = "drama_name";
    public static final String COLUMN_LINK_PHOTO = "photo_link";
    public static final String COLUMN_DATETIME = "date";
    public static final String COLUMN_DRAMA_LENGTH = "drama_length";
    public static final String COLUMN_DRAMA_LANGUAGE = "drama_language";
    public static final String COLUMN_DRAMA_GENRE = "drama_genre";
    public static final String COLUMN_DRAMA_TIME = "time";
    public static final String COLUMN_DRAMA_DESCRIPTION = "description";
    public static final String COLUMN_DRAMA_CAST = "star_cast";
    public static final String COLUMN_DRAMA_WRITER = "writer";
    public static final String COLUMN_DRAMA_DIRECTOR = "director";
    public static final String COLUMN_DRAMA_AVG_RATING = "avg_rating";
    public static final String COLUMN_DRAMA_MUSIC = "music";
    public static final String COLUMN_DRAMA_ISFAV = "isfav";

    private String group_name;
    private String title;
    private String link_photo;
    private String datetime;
    private String description;
    private String[] cast;
    private String genre;
    private String director;
    private String writer;
    private String music,star_cast,drama_language,time;
    String avg_rating;

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    public String getStar_cast() {
        return star_cast;
    }

    public void setStar_cast(String star_cast) {
        this.star_cast = star_cast;
    }

    public String getIsfav() {
        return isfav;
    }

    public void setIsfav(String isfav) {
        this.isfav = isfav;
    }

    private String isfav;
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

    private int id;

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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
