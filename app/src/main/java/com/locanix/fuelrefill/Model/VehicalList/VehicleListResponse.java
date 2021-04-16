package com.locanix.fuelrefill.Model.VehicalList;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleListResponse {

    @SerializedName("Error")
    private String error;

    @SerializedName("Data")
    private List<DataItem> data;

    @SerializedName("Success")
    private boolean success;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return
                "Response{" +
                        "error = '" + error + '\'' +
                        ",data = '" + data + '\'' +
                        ",success = '" + success + '\'' +
                        "}";
    }
}