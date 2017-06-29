package com.lognsys.kalrav.model;

/**
 * Created by admin on 6/29/2017.
 */

public class Ratings {
    private int id;
    private double rating;
    private String rating_date;
    private int dramas_id;
    private int users_id;



    public Ratings(int id2, double rating, String rating_date2, int users_id2, int dramas_id2) {
        this.id = id;
        this.rating = rating;
        this.rating_date = rating_date;
        this.dramas_id = dramas_id;
        this.users_id = users_id;
    }

    public Ratings() {
        // TODO Auto-generated constructor stub
    }

    public String getRating_date() {
        return rating_date;
    }

    public void setRating_date(String rating_date) {
        this.rating_date = rating_date;
    }

    public int getDramas_id() {
        return dramas_id;
    }

    public void setDramas_id(int dramas_id) {
        this.dramas_id = dramas_id;
    }

    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Ratings{" +
                "id=" + id +
                ", rating=" + rating +
                ", rating_date='" + rating_date + '\'' +
                ", dramas_id=" + dramas_id +
                ", users_id=" + users_id +
                '}';
    }
}
