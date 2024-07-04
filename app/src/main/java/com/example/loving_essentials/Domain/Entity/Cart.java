package com.example.loving_essentials.Domain.Entity;

import com.example.loving_essentials.Domain.Entity.DTOs.CartItemDTO;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public class Cart {
    @SerializedName("id")
    public int Id;

    public LocalDateTime CreateAt;

    public LocalDateTime UpdateAt;
    @SerializedName("buyerId")
    public int BuyerId ;
    @SerializedName("productId")
    public int ProductId ;
    @SerializedName("price")
    public double Price;
    @SerializedName("quantity")
    public int Quantity ;
    @SerializedName("products")
    public List<CartItemDTO> Products;

    public Cart() {
    }

    public Cart(int id, LocalDateTime createAt, LocalDateTime updateAt, int buyerId, int productId, double price, int quantity, List<CartItemDTO> products) {
        Id = id;
        CreateAt = createAt;
        UpdateAt = updateAt;
        BuyerId = buyerId;
        ProductId = productId;
        Price = price;
        Quantity = quantity;
        Products = products;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public LocalDateTime getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        CreateAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        UpdateAt = updateAt;
    }

    public int getBuyerId() {
        return BuyerId;
    }

    public void setBuyerId(int buyerId) {
        BuyerId = buyerId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public List<CartItemDTO> getProducts() {
        return Products;
    }

    public void setProducts(List<CartItemDTO> products) {
        Products = products;
    }
}
