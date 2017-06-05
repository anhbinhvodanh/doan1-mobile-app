package com.bichan.shop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cuong on 6/4/2017.
 */

public class OrderDetalResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private OrderDetal data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public OrderDetal getData() {
        return data;
    }

    public void setData(OrderDetal data) {
        this.data = data;
    }
}
