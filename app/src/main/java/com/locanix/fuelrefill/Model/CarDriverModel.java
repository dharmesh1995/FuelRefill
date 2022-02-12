package com.locanix.fuelrefill.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarDriverModel {
    @SerializedName("Success")
    public boolean success;

    @SerializedName("Data")
    public String data;
}
