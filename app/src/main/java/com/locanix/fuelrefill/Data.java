package com.locanix.fuelrefill;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("AnnullyValumePercentage")
	public double annullyValumePercentage;

	@SerializedName("MonthlyValumePercentage")
	public double monthlyValumePercentage;

	@SerializedName("FuelEntryId")
	public int fuelEntryId;

	@SerializedName("MonthlyQuotaReached")
	public boolean monthlyQuotaReached;

	@SerializedName("AnnuallyQuotaReached")
	public boolean annuallyQuotaReached;
}