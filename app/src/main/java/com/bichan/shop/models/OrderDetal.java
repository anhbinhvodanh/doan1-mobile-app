package com.bichan.shop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderDetal {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("shipping_firstname")
    @Expose
    private String shippingFirstname;
    @SerializedName("shipping_lastname")
    @Expose
    private String shippingLastname;
    @SerializedName("shipping_address")
    @Expose
    private String shippingAddress;
    @SerializedName("shipping_telephone")
    @Expose
    private String shippingTelephone;
    @SerializedName("shipping_city_id")
    @Expose
    private String shippingCityId;
    @SerializedName("shipping_zone_id")
    @Expose
    private String shippingZoneId;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("shipping_status")
    @Expose
    private String shippingStatus;
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("date_modified")
    @Expose
    private String dateModified;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("zone")
    @Expose
    private String zone;
    @SerializedName("customer")
    @Expose
    private String customer;
    @SerializedName("products")
    @Expose
    private ArrayList<OrderProduct> products = new ArrayList<>();

    public ArrayList<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<OrderProduct> products) {
        this.products = products;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getShippingFirstname() {
        return shippingFirstname;
    }

    public void setShippingFirstname(String shippingFirstname) {
        this.shippingFirstname = shippingFirstname;
    }

    public String getShippingLastname() {
        return shippingLastname;
    }

    public void setShippingLastname(String shippingLastname) {
        this.shippingLastname = shippingLastname;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingTelephone() {
        return shippingTelephone;
    }

    public void setShippingTelephone(String shippingTelephone) {
        this.shippingTelephone = shippingTelephone;
    }

    public String getShippingCityId() {
        return shippingCityId;
    }

    public void setShippingCityId(String shippingCityId) {
        this.shippingCityId = shippingCityId;
    }

    public String getShippingZoneId() {
        return shippingZoneId;
    }

    public void setShippingZoneId(String shippingZoneId) {
        this.shippingZoneId = shippingZoneId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        String status = "";
        switch (this.status){
            case "0":
                status = "Chưa thanh toán";
            case "1":
                status = "Đã thanh toán";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingStatus() {
        String status = "";
        switch (shippingStatus){
            case "0":
                status = "Đang kiểm tra";
            case "1":
                status = "Đang chuyển đi";
            case "2":
                status = "Đã được giao";
        }
        return status;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

}