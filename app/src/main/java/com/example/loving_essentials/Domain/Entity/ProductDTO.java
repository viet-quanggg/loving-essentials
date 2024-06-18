package com.example.loving_essentials.Domain.Entity;

public class ProductDTO {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageURL;
    private int quantity;
    private String categoryName;
    private String brandName;

    public ProductDTO() {
    }

    public ProductDTO(int id, String name, String description, double price, String imageURL, int quantity, String categoryName, String brandName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
        this.quantity = quantity;
        this.categoryName = categoryName;
        this.brandName = brandName;
    }

    public ProductDTO(String name, String description, double price, String imageURL, int quantity, String categoryName, String brandName) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
        this.quantity = quantity;
        this.categoryName = categoryName;
        this.brandName = brandName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getimageURL() {
        return imageURL;
    }

    public void setimageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
