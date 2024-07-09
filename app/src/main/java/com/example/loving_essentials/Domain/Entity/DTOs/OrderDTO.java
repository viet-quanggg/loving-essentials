package com.example.loving_essentials.Domain.Entity.DTOs;

import com.example.loving_essentials.Domain.Entity.OrderDetail;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class OrderDTO {
    int id;
    @SerializedName("created")
    public Date Created;
    @SerializedName("updated")
    public Date Updated;
    @SerializedName("totalPrice")
    double totalPrice;
    @SerializedName("buyerId")
    public int buyerId;
    @SerializedName("shipperName")
    public String ShipperName;
    @SerializedName("status")
    public int Status;
    public List<OrderDetail> OrderDetails;

    public OrderDTO(int id, Date created, double totalPrice, int buyerId, String shipperName, int status) {
        this.id = id;
        Created = created;
        this.totalPrice = totalPrice;
        this.buyerId = buyerId;
        ShipperName = shipperName;
        status = Status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return Created;
    }

    public void setCreated(Date created) {
        Created = created;
    }

    public Date getUpdated() {
        return Updated;
    }

    public void setUpdated(Date updated) {
        Updated = updated;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public String getShipperName() {
        return ShipperName;
    }

    public void setShipperName(String shipperName) {
        ShipperName = shipperName;
    }

    public List<OrderDetail> getOrderDetails() {
        return OrderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        OrderDetails = orderDetails;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
