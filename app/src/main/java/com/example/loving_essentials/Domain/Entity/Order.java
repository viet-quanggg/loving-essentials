package com.example.loving_essentials.Domain.Entity;

import java.time.LocalDateTime;

public class Order {
    private int Id;
    private LocalDateTime CreatedDate;
    private Double TotalAmount;
    private LocalDateTime DeliveryDate;
    private int UserId;
    private int TransactionsId;

    public Order() {
    }

    public Order(int id, LocalDateTime createdDate, Double totalAmount, LocalDateTime deliveryDate, int userId, int transactionsId) {
        Id = id;
        CreatedDate = createdDate;
        TotalAmount = totalAmount;
        DeliveryDate = deliveryDate;
        UserId = userId;
        TransactionsId = transactionsId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public LocalDateTime getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        CreatedDate = createdDate;
    }

    public Double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        TotalAmount = totalAmount;
    }

    public LocalDateTime getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getTransactionsId() {
        return TransactionsId;
    }

    public void setTransactionsId(int transactionsId) {
        TransactionsId = transactionsId;
    }
}
