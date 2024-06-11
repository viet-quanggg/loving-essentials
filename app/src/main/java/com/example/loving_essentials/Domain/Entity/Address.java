package com.example.loving_essentials.Domain.Entity;

public class Address {
    private int Id;
    private String HouseNumber;
    private String Street;
    private String Ward;
    private String District;
    private String City;
    private int UserId;

    public Address() {
    }

    public Address(int id, String houseNumber, String street, String ward, String district, String city, int userId) {
        Id = id;
        HouseNumber = houseNumber;
        Street = street;
        Ward = ward;
        District = district;
        City = city;
        UserId = userId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getHouseNumber() {
        return HouseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        HouseNumber = houseNumber;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getWard() {
        return Ward;
    }

    public void setWard(String ward) {
        Ward = ward;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }
}
