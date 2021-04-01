package com.cubezytech.fuelrefill.Model.UploadImage;

import com.google.gson.annotations.SerializedName;

public class UploadFileResponse{

	@SerializedName("Data")
	private String data;

	@SerializedName("Success")
	private boolean success;

	public void setData(String data){
		this.data = data;
	}

	public String getData(){
		return data;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	@Override
 	public String toString(){
		return 
			"UploadFileResponse{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			"}";
		}
}