package com.bichan.shop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cuong on 5/16/2017.
 */

public class HomeCategory {

    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("products")
    @Expose
    private ArrayList<ProductMini> productMinis = new ArrayList<>();


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ProductMini> getProductMinis() {
        return productMinis;
    }

    public void setProductMinis(ArrayList<ProductMini> productMinis) {
        this.productMinis = productMinis;
    }
}
