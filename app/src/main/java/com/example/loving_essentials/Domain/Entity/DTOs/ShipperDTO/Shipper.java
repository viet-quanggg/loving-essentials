package com.example.loving_essentials.Domain.Entity.DTOs.ShipperDTO;

public class Shipper {
    private int id;
    private String name;
    private int role;

    public Shipper(int id, String name, int role, String email, String phoneNumber, int status) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public Shipper() {
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String email;
    private String phoneNumber;
    private int status;
}
