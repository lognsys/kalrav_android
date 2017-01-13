package com.lognsys.kalrav.model;

/**
 * Created by pdoshi on 09/01/17.
 */

public class Notification {


    private String id = "";
    private String title = "";

    public Notification(String title, String timeStamp, String genre, String message, String description) {
        this.id = id;
        this.title = title;
        this.timeStamp = timeStamp;
        this.genre = genre;
        this.message = message;
        this.description = description;
        this.imageUrl = imageUrl;
        this.imageResourceId = imageResourceId;
    }

    private String timeStamp = "";
    private String genre = ""; //eg Drama, Charity, General Announcement, Holiday Trip
    private String message = "";
    private String description = "";
    private String imageUrl = "";
    private int imageResourceId;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
