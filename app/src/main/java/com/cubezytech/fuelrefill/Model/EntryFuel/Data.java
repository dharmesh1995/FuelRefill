package com.cubezytech.fuelrefill.Model.EntryFuel;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("AnnullyValumePercentage")
	private int annullyValumePercentage;

	@SerializedName("MonthlyValumePercentage")
	private int monthlyValumePercentage;

	@SerializedName("MonthlyQuotaReached")
	private boolean monthlyQuotaReached;

	@SerializedName("AnnuallyQuotaReached")
	private boolean annuallyQuotaReached;

	public void setAnnullyValumePercentage(int annullyValumePercentage){
		this.annullyValumePercentage = annullyValumePercentage;
	}

	public int getAnnullyValumePercentage(){
		return annullyValumePercentage;
	}

	public void setMonthlyValumePercentage(int monthlyValumePercentage){
		this.monthlyValumePercentage = monthlyValumePercentage;
	}

	public int getMonthlyValumePercentage(){
		return monthlyValumePercentage;
	}

	public void setMonthlyQuotaReached(boolean monthlyQuotaReached){
		this.monthlyQuotaReached = monthlyQuotaReached;
	}

	public boolean isMonthlyQuotaReached(){
		return monthlyQuotaReached;
	}

	public void setAnnuallyQuotaReached(boolean annuallyQuotaReached){
		this.annuallyQuotaReached = annuallyQuotaReached;
	}

	public boolean isAnnuallyQuotaReached(){
		return annuallyQuotaReached;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"annullyValumePercentage = '" + annullyValumePercentage + '\'' + 
			",monthlyValumePercentage = '" + monthlyValumePercentage + '\'' + 
			",monthlyQuotaReached = '" + monthlyQuotaReached + '\'' + 
			",annuallyQuotaReached = '" + annuallyQuotaReached + '\'' + 
			"}";
		}
}