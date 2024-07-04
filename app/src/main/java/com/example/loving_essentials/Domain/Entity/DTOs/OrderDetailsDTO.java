package com.example.loving_essentials.Domain.Entity.DTOs;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class OrderDetailsDTO
{
    int id;
    int orderId;
    /*@SerializedName("created")
    public Date Created;
    @SerializedName("updated")
    public Date Updated;*/
    @SerializedName("Url")
    String Url;
    @SerializedName("ProductName")
    public String productName;
    @SerializedName("Price")
    public double price;
    @SerializedName("Quantity")
    public double quantity;

    public OrderDetailsDTO(int id, int orderId, String url, String productName, double price, double quantity) {
        this.id = id;
        this.orderId = orderId;
        Url = url;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
