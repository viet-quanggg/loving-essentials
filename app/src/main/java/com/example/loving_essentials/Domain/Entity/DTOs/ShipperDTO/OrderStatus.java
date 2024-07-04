package com.example.loving_essentials.Domain.Entity.DTOs.ShipperDTO;

public enum OrderStatus {
    Pending(1),
    Processing(2),
    Shipped(3),
    Delivered(4),
    Cancelled(5);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OrderStatus fromValue(int value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid order status value: " + value);
    }
}