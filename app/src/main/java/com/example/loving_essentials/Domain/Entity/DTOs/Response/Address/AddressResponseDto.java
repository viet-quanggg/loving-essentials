package com.example.loving_essentials.Domain.Entity.DTOs.Response.Address;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class AddressResponseDto implements Parcelable {
    public int id;
    public String houseNumber;
    public String street;
    public String district;
    public String ward;
    public String city;

    public UserInfo userInformation ;

    protected AddressResponseDto(Parcel in) {
        id = in.readInt();
        houseNumber = in.readString();
        street = in.readString();
        district = in.readString();
        ward = in.readString();
        city = in.readString();
    }

    public static final Creator<AddressResponseDto> CREATOR = new Creator<AddressResponseDto>() {
        @Override
        public AddressResponseDto createFromParcel(Parcel in) {
            return new AddressResponseDto(in);
        }

        @Override
        public AddressResponseDto[] newArray(int size) {
            return new AddressResponseDto[size];
        }
    };

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

    public UserInfo getUserInfo() {
        return userInformation;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInformation = userInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(houseNumber);
        dest.writeString(street);
        dest.writeString(district);
        dest.writeString(ward);
        dest.writeString(city);
    }

    public static class UserInfo implements Parcelable{
        public String name;
        public String email;
        public String phoneNumber;

        protected UserInfo(Parcel in) {
            name = in.readString();
            email = in.readString();
            phoneNumber = in.readString();
        }

        public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
            @Override
            public UserInfo createFromParcel(Parcel in) {
                return new UserInfo(in);
            }

            @Override
            public UserInfo[] newArray(int size) {
                return new UserInfo[size];
            }
        };

        public String getName() {
            return name;
        }

        public void setName(String name) {
            name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            phoneNumber = phoneNumber;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(email);
            dest.writeString(phoneNumber);
        }
    }
}


