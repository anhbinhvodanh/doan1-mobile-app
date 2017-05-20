package com.bichan.shop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cuong on 5/20/2017.
 */

public class ReviewResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private ArrayList<Review> reviews = new ArrayList<>();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
}
