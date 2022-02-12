package com.locanix.fuelrefill.Model.GetfuelEntry;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("volume")
	public double volume;

	@SerializedName("refilledOn")
	public String refilledOn;

	@SerializedName("refilledOnIST")
	public String refilledOnIST;

	@SerializedName("id")
	public int id;

	@SerializedName("userId")
	public String userId;
}