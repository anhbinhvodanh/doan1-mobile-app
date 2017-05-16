package com.bichan.shop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cuong on 5/16/2017.
 */

public class HomeSliderResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private ArrayList<HomeSlider> homeSliders = new ArrayList<>();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<HomeSlider> getHomeSliders() {
        return homeSliders;
    }

    public void setHomeSliders(ArrayList<HomeSlider> homeSliders) {
        this.homeSliders = homeSliders;
    }
}
