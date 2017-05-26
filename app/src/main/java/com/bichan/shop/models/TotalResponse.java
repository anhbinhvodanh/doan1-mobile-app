package com.bichan.shop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cuong on 5/26/2017.
 */

public class TotalResponse {
    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("data")
    @Expose
    private int data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
