package com.example.loving_essentials.Domain.Entity.DTOs.AuthDTO;


import com.example.loving_essentials.Domain.Entity.User;

public class LoginResponse {
    private User user;
    private String token;

    // getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}