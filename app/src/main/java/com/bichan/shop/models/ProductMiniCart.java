package com.bichan.shop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cuong on 5/26/2017.
 */

public class ProductMiniCart {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("description_short")
    @Expose
    private String descriptionShort;
    @SerializedName("description_technical")
    @Expose
    private String descriptionTechnical;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("meta_title")
    @Expose
    private String metaTitle;
    @SerializedName("manufacturer_id")
    @Expose
    private String manufacturerId;
    @SerializedName("sort_order")
    @Expose
    private String sortOrder;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("viewed")
    @Expose
    private String viewed;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("date_modified")
    @Expose
    private String dateModified;
    @SerializedName("product_option_id")
    @Expose
    private String productOptionId;
    @SerializedName("option_name")
    @Expose
    private String optionName;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("product_image_id")
    @Expose
    private String productImageId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("keyword")
    @Expose
    private String keyword;
    @SerializedName("manufacturer")
    @Expose
    private String manufacturer;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("quantity_cart")
    @Expose
    private String quantityCart;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getDescriptionTechnical() {
        return descriptionTechnical;
    }

    public void setDescriptionTechnical(String descriptionTechnical) {
        this.descriptionTechnical = descriptionTechnical;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getViewed() {
        return viewed;
    }

    public void setViewed(String viewed) {
        this.viewed = viewed;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getProductOptionId() {
        return productOptionId;
    }

    public void setProductOptionId(String productOptionId) {
        this.productOptionId = productOptionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getProductImageId() {
        return productImageId;
    }

    public void setProductImageId(String productImageId) {
        this.productImageId = productImageId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getQuantityCart() {
        return quantityCart;
    }

    public void setQuantityCart(String quantityCart) {
        this.quantityCart = quantityCart;
    }

}
