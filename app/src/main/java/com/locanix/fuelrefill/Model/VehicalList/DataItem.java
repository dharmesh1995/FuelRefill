package com.locanix.fuelrefill.Model.VehicalList;

import com.google.gson.annotations.SerializedName;

public class DataItem {

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

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    public String getVehiclenumber() {
        return vehiclenumber;
    }

    public void setVehiclenumber(String vehiclenumber) {
        this.vehiclenumber = vehiclenumber;
    }

    public int getSatelliteCount() {
        return satelliteCount;
    }

    public void setSatelliteCount(int satelliteCount) {
        this.satelliteCount = satelliteCount;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public Object getExternalPowerSupply() {
        return externalPowerSupply;
    }

    public void setExternalPowerSupply(Object externalPowerSupply) {
        this.externalPowerSupply = externalPowerSupply;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVehicleid() {
        return vehicleid;
    }

    public void setVehicleid(int vehicleid) {
        this.vehicleid = vehicleid;
    }

    public boolean isIgnition() {
        return ignition;
    }

    public void setIgnition(boolean ignition) {
        this.ignition = ignition;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
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