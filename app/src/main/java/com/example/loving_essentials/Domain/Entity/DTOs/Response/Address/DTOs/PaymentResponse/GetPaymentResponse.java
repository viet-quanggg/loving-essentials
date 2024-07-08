package com.example.loving_essentials.Domain.Entity.DTOs.Response.Address.DTOs.PaymentResponse;

public class GetPaymentResponse {
    public int error;
    public String message;
    public DataResponse data;

    public GetPaymentResponse() {
    }

    public GetPaymentResponse(int error, String message, DataResponse data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataResponse getData() {
        return data;
    }

    public void setData(DataResponse data) {
        this.data = data;
    }
}

