package com.example.loving_essentials.Domain.Entity.DTOs.Request.Address;

public class AddAddressRequestDto {
    private String houseNumber;
    private String street;
    private String ward;
    private String district;
    private String city;
    private UserAddress userAddress;

    public AddAddressRequestDto() {
    }

    public AddAddressRequestDto(String houseNumber, String street, String ward, String district, String city, UserAddress userAddress) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.userAddress = userAddress;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
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

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static class UserAddress{
        private int id;
        private String name;
        private String phoneNumber;

        public UserAddress(int id, String name, String phoneNumber) {
            this.id = id;
            this.name = name;
            this.phoneNumber = phoneNumber;
        }
    }
}
