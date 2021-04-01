package com.cubezytech.fuelrefill.Model.FuelHistory;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("volume")
	private double volume;

	@SerializedName("refilledOn")
	private String refilledOn;

	@SerializedName("refilledOnIST")
	private String refilledOnIST;

	@SerializedName("id")
	private int id;

	@SerializedName("userId")
	private String userId;

	public void setVolume(double volume){
		this.volume = volume;
	}

	public double getVolume(){
		return volume;
	}

	public void setRefilledOn(String refilledOn){
		this.refilledOn = refilledOn;
	}

	public String getRefilledOn(){
		return refilledOn;
	}

	public void setRefilledOnIST(String refilledOnIST){
		this.refilledOnIST = refilledOnIST;
	}

	public String getRefilledOnIST(){
		return refilledOnIST;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"volume = '" + volume + '\'' + 
			",refilledOn = '" + refilledOn + '\'' + 
			",refilledOnIST = '" + refilledOnIST + '\'' + 
			",id = '" + id + '\'' + 
			",userId = '" + userId + '\'' + 
			"}";
		}
}