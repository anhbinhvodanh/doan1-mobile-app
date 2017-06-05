package com.bichan.shop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cuong on 6/4/2017.
 */

public class OrderResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private ArrayList<Order> data = new ArrayList<>();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<Order> getData() {
        return data;
    }

    public void setData(ArrayList<Order> data) {
        this.data = data;
    }
}
