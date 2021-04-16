package com.locanix.fuelrefill.Model.UploadImage;

import com.google.gson.annotations.SerializedName;

public class UploadFileResponse {

    @SerializedName("Data")
    private String data;

    @SerializedName("Success")
    private boolean success;

    public String getData() {
        return data;
    }

    public void setData(String data) {
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
                "UploadFileResponse{" +
                        "data = '" + data + '\'' +
                        ",success = '" + success + '\'' +
                        "}";
    }
}