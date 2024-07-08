package com.example.loving_essentials.Domain.Entity.DTOs.Request.Order;

public class CreateOrderRequest {
    public int cartId;
    public int addressId;
    public int method;
    public int payment;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(int cartId, int addressId, int method, int payment) {
        this.cartId = cartId;
        this.addressId = addressId;
        this.method = method;
        this.payment = payment;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }
}
