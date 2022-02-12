package com.locanix.fuelrefill.Model.GetfuelEntry;

import com.google.gson.annotations.SerializedName;

public class GetFuelEntryModel{

	@SerializedName("Error")
	public String error;

	@SerializedName("Data")
	public Data data;

	@SerializedName("Success")
	public boolean success;
}