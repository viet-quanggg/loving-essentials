package com.example.loving_essentials.Domain.Entity.DTOs;

import com.example.loving_essentials.Domain.Entity.OrderDetail;

import java.util.Date;
import java.util.List;

public class OrderDTO {
    int id;
    public Date Created;
    public Date Updated;
    double totalPrice;
    public int buyerId;
    public int ShipperId;
    public List<OrderDetail> OrderDetails;

    public OrderDTO(int id, Date created, Date updated, double totalPrice, int buyerId, int shipperId, List<OrderDetail> orderDetails) {
        this.id = id;
        Created = created;
        Updated = updated;
        this.totalPrice = totalPrice;
        this.buyerId = buyerId;
        ShipperId = shipperId;
        OrderDetails = orderDetails;
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

    public int getShipperId() {
        return ShipperId;
    }

    public void setShipperId(int shipperId) {
        ShipperId = shipperId;
    }

    public List<OrderDetail> getOrderDetails() {
        return OrderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        OrderDetails = orderDetails;
    }
}
