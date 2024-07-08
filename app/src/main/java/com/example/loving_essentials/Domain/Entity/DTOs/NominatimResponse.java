package com.example.loving_essentials.Domain.Entity.DTOs;

public class NominatimResponse {
    public double lat;
    public double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
