package com.example.loving_essentials.Domain.Entity.DTOs.Admin.ProductManagement;

import java.time.LocalDateTime;

public class ProductDetailAdmin {
    private int id;
    private String name;
    private double price;
    private String description;
    private String imageURL;
    private int quantity;
    private int categoryId;
    private int brandId;
    private byte status;

    public ProductDetailAdmin() {
    }

    public ProductDetailAdmin(int id, String name, double price, String description, String imageURL, int quantity, int categoryId, int brandId, byte status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageURL = imageURL;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }
}

