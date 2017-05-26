package com.bichan.shop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cuong on 5/26/2017.
 */

public class ProductMiniCartResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private ArrayList<ProductMiniCart> productMinis = new ArrayList<>();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<ProductMiniCart> getProductMinis() {
        return productMinis;
    }

    public void setProductMinis(ArrayList<ProductMiniCart> productMinis) {
        this.productMinis = productMinis;
    }
}
