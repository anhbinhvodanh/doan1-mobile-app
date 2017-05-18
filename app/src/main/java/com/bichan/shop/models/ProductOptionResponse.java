package com.bichan.shop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cuong on 5/18/2017.
 */

public class ProductOptionResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private ArrayList<ProductOption> productOptions = new ArrayList<>();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<ProductOption> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(ArrayList<ProductOption> productOptions) {
        this.productOptions = productOptions;
    }
}
