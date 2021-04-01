package com.cubezytech.fuelrefill.Model.EntryFuel;

import com.google.gson.annotations.SerializedName;

public class EntryFuelResponse{

	@SerializedName("Error")
	private String error;

	@SerializedName("Data")
	private Data data;

	@SerializedName("Success")
	private boolean success;

	public void setError(String error){
		this.error = error;
	}

	public String getError(){
		return error;
	}

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
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
			"EntryFuelResponse{" + 
			"error = '" + error + '\'' + 
			",data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			"}";
		}
}