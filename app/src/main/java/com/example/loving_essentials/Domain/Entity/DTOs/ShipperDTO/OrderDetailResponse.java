package com.example.loving_essentials.Domain.Entity.DTOs.ShipperDTO;

import com.example.loving_essentials.Domain.Entity.ProductDTO;

public class OrderDetailResponse {
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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductDTO getProducts() {
        return products;
    }

    public void setProducts(ProductDTO products) {
        this.products = products;
    }

    int id;
    int orderId;
    String createAt;
    String updateAt;
    int productId;
    double price;
    int quantity;
    ProductDTO products;
}
