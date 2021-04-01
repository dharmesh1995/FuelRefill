package com.cubezytech.fuelrefill.Model.VehicalList;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class VehicleListResponse {

	@SerializedName("Error")
	private String error;

	@SerializedName("Data")
	private List<DataItem> data;

	@SerializedName("Success")
	private boolean success;

	public void setError(String error){
		this.error = error;
	}

	public String getError(){
		return error;
	}

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
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
			"Response{" + 
			"error = '" + error + '\'' + 
			",data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			"}";
		}
}