package com.example.loving_essentials.Domain.Entity.DTOs.ShipperDTO;

public class OrderStatusUpdateRequest {
    private int orderId;
    private int shipperId;
    private int newStatus;

    public OrderStatusUpdateRequest() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public int getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(int newStatus) {
        this.newStatus = newStatus;
    }
}
