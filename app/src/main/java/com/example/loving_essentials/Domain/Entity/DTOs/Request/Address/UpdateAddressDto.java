package com.example.loving_essentials.Domain.Entity.DTOs.Request.Address;

public class UpdateAddressDto {
    public int id;
    public String houseNumber;
    public String street;
    public String district;
    public String ward;
    public String city;

    public UpdateAddressDto() {
    }

    public UpdateAddressDto(int id, String houseNumber, String street, String district, String ward, String city) {
        this.id = id;
        this.houseNumber = houseNumber;
        this.street = street;
        this.district = district;
        this.ward = ward;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
