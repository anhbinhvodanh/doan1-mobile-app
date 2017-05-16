package com.bichan.shop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cuong on 5/16/2017.
 */

public class HomeCategoryResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private ArrayList<HomeCategory> homeCategories = new ArrayList<>();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<HomeCategory> getHomeCategories() {
        return homeCategories;
    }

    public void setHomeCategories(ArrayList<HomeCategory> homeCategories) {
        this.homeCategories = homeCategories;
    }
}
