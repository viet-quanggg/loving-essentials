package com.example.loving_essentials.Domain.Entity.DTOs.Response.Address.DTOs.PaymentResponse;

public class DataResponse{
    public String checkoutUrl;
    public int orderCode;


    public DataResponse() {
    }

    public DataResponse(String checkoutUrl, int orderCode) {
        this.checkoutUrl = checkoutUrl;
        this.orderCode = orderCode;
    }

    public int getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(int orderCode) {
        this.orderCode = orderCode;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }
}
