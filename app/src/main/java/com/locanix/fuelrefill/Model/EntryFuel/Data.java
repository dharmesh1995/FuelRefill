package com.locanix.fuelrefill.Model.EntryFuel;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("AnnullyValumePercentage")
    private double annullyValumePercentage;

    @SerializedName("MonthlyValumePercentage")
    private double monthlyValumePercentage;

    @SerializedName("MonthlyQuotaReached")
    private boolean monthlyQuotaReached;

    @SerializedName("AnnuallyQuotaReached")
    private boolean annuallyQuotaReached;

    public double getAnnullyValumePercentage() {
        return annullyValumePercentage;
    }

    public void setAnnullyValumePercentage(double annullyValumePercentage) {
        this.annullyValumePercentage = annullyValumePercentage;
    }

    public double getMonthlyValumePercentage() {
        return monthlyValumePercentage;
    }

    public void setMonthlyValumePercentage(double monthlyValumePercentage) {
        this.monthlyValumePercentage = monthlyValumePercentage;
    }

    public boolean isMonthlyQuotaReached() {
        return monthlyQuotaReached;
    }

    public void setMonthlyQuotaReached(boolean monthlyQuotaReached) {
        this.monthlyQuotaReached = monthlyQuotaReached;
    }

    public boolean isAnnuallyQuotaReached() {
        return annuallyQuotaReached;
    }

    public void setAnnuallyQuotaReached(boolean annuallyQuotaReached) {
        this.annuallyQuotaReached = annuallyQuotaReached;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "annullyValumePercentage = '" + annullyValumePercentage + '\'' +
                        ",monthlyValumePercentage = '" + monthlyValumePercentage + '\'' +
                        ",monthlyQuotaReached = '" + monthlyQuotaReached + '\'' +
                        ",annuallyQuotaReached = '" + annuallyQuotaReached + '\'' +
                        "}";
    }
}