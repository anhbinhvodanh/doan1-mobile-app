package com.bichan.shop.models;

/**
 * Created by cuong on 5/17/2017.
 */

public class ProductsFilter {
    String categoryId;
    int start;
    int limit;
    SortOrder sortOrder;
    SortType sortType;
    String name;

    public ProductsFilter(int limit){
        this.start = 0;
        this.limit = limit;
    }

    public ProductsFilter(int start, int limit){
        this.start = start;
        this.limit = limit;
    }

    public void next(){
        this.start = this.start + limit;
    }

    public void reload(){
        this.start = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSortOrder() {
        if(sortOrder == null)
            return "";
        switch (sortOrder){
            case DESC:
                return "DESC";
            case ASC:
                return "ASC";
        }
        return "";
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortType() {
        if(sortType == null)
            return "";
        switch (sortType){
            case DATE_ADD:
                return "p.date_added";
            case MODEL:
                return "po.model";
            case NAME:
                return "p.name";
            case PRICE:
                return "po.discount";
            case QUANTITY:
                return "po.quantity";
        }
        return "";
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }
}
