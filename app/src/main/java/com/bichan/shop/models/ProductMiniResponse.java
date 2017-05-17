package com.bichan.shop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cuong on 5/17/2017.
 */

public class ProductMiniResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private ArrayList<ProductMini> productMinis = new ArrayList<>();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<ProductMini> getProductMinis() {
        return productMinis;
    }

    public void setProductMinis(ArrayList<ProductMini> productMinis) {
        this.productMinis = productMinis;
    }
}
