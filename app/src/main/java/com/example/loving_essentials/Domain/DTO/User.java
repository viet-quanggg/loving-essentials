package com.example.loving_essentials.Domain.DTO;

public class User {
    private int id;
    private String name;
    private String email;
    private String phoneNumber; // Changed from 'Phone' to 'phoneNumber'
    private int role;
    private String address;

    public User() {}

    public User(int id, String name, String email, String phoneNumber, int role, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber; // Changed from 'Phone' to 'phoneNumber'
        this.role = role;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}