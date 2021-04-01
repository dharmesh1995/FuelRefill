package com.cubezytech.fuelrefill.Model.VehicalList;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("lastseen")
	private String lastseen;

	@SerializedName("vehiclenumber")
	private String vehiclenumber;

	@SerializedName("satelliteCount")
	private int satelliteCount;

	@SerializedName("odometer")
	private int odometer;

	@SerializedName("externalPowerSupply")
	private Object externalPowerSupply;

	@SerializedName("heading")
	private int heading;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("description")
	private String description = "";

	@SerializedName("vehicleid")
	private int vehicleid;

	@SerializedName("ignition")
	private boolean ignition;

	@SerializedName("speed")
	private double speed;

	@SerializedName("longitude")
	private String longitude;

	public void setLastseen(String lastseen){
		this.lastseen = lastseen;
	}

	public String getLastseen(){
		return lastseen;
	}

	public void setVehiclenumber(String vehiclenumber){
		this.vehiclenumber = vehiclenumber;
	}

	public String getVehiclenumber(){
		return vehiclenumber;
	}

	public void setSatelliteCount(int satelliteCount){
		this.satelliteCount = satelliteCount;
	}

	public int getSatelliteCount(){
		return satelliteCount;
	}

	public void setOdometer(int odometer){
		this.odometer = odometer;
	}

	public int getOdometer(){
		return odometer;
	}

	public void setExternalPowerSupply(Object externalPowerSupply){
		this.externalPowerSupply = externalPowerSupply;
	}

	public Object getExternalPowerSupply(){
		return externalPowerSupply;
	}

	public void setHeading(int heading){
		this.heading = heading;
	}

	public int getHeading(){
		return heading;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setVehicleid(int vehicleid){
		this.vehicleid = vehicleid;
	}

	public int getVehicleid(){
		return vehicleid;
	}

	public void setIgnition(boolean ignition){
		this.ignition = ignition;
	}

	public boolean isIgnition(){
		return ignition;
	}

	public void setSpeed(double speed){
		this.speed = speed;
	}

	public double getSpeed(){
		return speed;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"lastseen = '" + lastseen + '\'' + 
			",vehiclenumber = '" + vehiclenumber + '\'' + 
			",satelliteCount = '" + satelliteCount + '\'' + 
			",odometer = '" + odometer + '\'' + 
			",externalPowerSupply = '" + externalPowerSupply + '\'' + 
			",heading = '" + heading + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",description = '" + description + '\'' + 
			",vehicleid = '" + vehicleid + '\'' + 
			",ignition = '" + ignition + '\'' + 
			",speed = '" + speed + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}
}