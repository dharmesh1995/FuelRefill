package com.locanix.fuelrefill.Model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class EnterFuelRefilleModel {
    @SerializedName("Success")
    public boolean success;

    @SerializedName("Data")
    public JsonObject object;
}
