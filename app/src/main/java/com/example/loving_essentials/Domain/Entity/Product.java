package com.example.loving_essentials.Domain.Entity;

import android.net.Uri;

import java.time.LocalDateTime;

public class Product {
    private int Id;
    private String Name;
    private double Price;
    private LocalDateTime CreatedDate;
    private LocalDateTime UpdatedDate;
    private String Description;
    private Uri Image;
    private int CategoryId;
    private int BrandId;

    public Product() {
    }

    public Product(int id, String name, double price, LocalDateTime createdDate, LocalDateTime updatedDate, String description, Uri image, int categoryId, int brandId) {
        Id = id;
        Name = name;
        Price = price;
        CreatedDate = createdDate;
        UpdatedDate = updatedDate;
        Description = description;
        Image = image;
        CategoryId = categoryId;
        BrandId = brandId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public LocalDateTime getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        CreatedDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Uri getImage() {
        return Image;
    }

    public void setImage(Uri image) {
        Image = image;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public int getBrandId() {
        return BrandId;
    }

    public void setBrandId(int brandId) {
        BrandId = brandId;
    }
}
