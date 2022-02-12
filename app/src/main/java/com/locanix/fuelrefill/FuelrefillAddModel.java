package com.locanix.fuelrefill;

import com.google.gson.annotations.SerializedName;

public class FuelrefillAddModel{

	@SerializedName("Error")
	public String error;

	@SerializedName("Data")
	public Data data;

	@SerializedName("Success")
	public boolean success;
}