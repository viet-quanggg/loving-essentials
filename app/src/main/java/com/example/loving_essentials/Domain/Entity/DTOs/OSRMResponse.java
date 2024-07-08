package com.example.loving_essentials.Domain.Entity.DTOs;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OSRMResponse {
    @SerializedName("routes")
    public List<Route> routes;

    public static class Route {
        @SerializedName("geometry")
        public Geometry geometry;

        @SerializedName("legs")
        public List<Leg> legs;
    }

    public static class Geometry {
        @SerializedName("coordinates")
        public List<List<Double>> coordinates;
    }

    public static class Leg {
        @SerializedName("distance")
        public double distance;
    }
}
