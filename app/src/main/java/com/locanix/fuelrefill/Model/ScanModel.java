package com.locanix.fuelrefill.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ScanModel{

	@SerializedName("Error")
	public String error;

	@SerializedName("Data")
	public List<DataItem> data;

	@SerializedName("Success")
	public boolean success;


	public class DataItem{

		@SerializedName("lastseen")
		public String lastseen;

		@SerializedName("vehiclenumber")
		public String vehiclenumber;

		@SerializedName("heading")
		public double heading;

		@SerializedName("latitude")
		public String latitude;

		@SerializedName("description")
		public String description;

		@SerializedName("vehicleid")
		public int vehicleid;

		@SerializedName("speed")
		public double speed;

		@SerializedName("longitude")
		public String longitude;
	}


	@Override
	public String toString() {
		return "ScanModel{" +
				"error='" + error + '\'' +
				", data=" + data +
				", success=" + success +
				'}';
	}
}