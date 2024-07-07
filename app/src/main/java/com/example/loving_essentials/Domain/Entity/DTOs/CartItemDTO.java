package com.example.loving_essentials.Domain.Entity.DTOs;

import com.example.loving_essentials.Domain.Entity.ProductDTO;
import com.google.gson.annotations.SerializedName;

public class CartItemDTO
{
    @SerializedName("quantity")
    public int Quantity;
    @SerializedName("product")
    public ProductDTO Product;

    public CartItemDTO() {
    }

    public CartItemDTO(int quantity, ProductDTO product) {
        Quantity = quantity;
        Product = product;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public ProductDTO getProduct() {
        return Product;
    }

    public void setProduct(ProductDTO product) {
        Product = product;
    }
}
