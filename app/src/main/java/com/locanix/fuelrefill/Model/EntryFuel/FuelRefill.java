package com.locanix.fuelrefill.Model.EntryFuel;

public class FuelRefill {

    int _id;

    String scanCode, vehicleId, fDriver, fBeforeRefill, fAfterRefill, fRefill, timeOfRefill;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getScanCode() {
        return scanCode;
    }

    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getFDriver() {
        return fDriver;
    }

    public void setFDriver(String fDriver) {
        this.fDriver = fDriver;
    }

    public String getFBeforeRefill() {
        return fBeforeRefill;
    }

    public void setFBeforeRefill(String fBeforeRefill) {
        this.fBeforeRefill = fBeforeRefill;
    }

    public String getFAfterRefill() {
        return fAfterRefill;
    }

    public void setFAfterRefill(String fAfterRefill) {
        this.fAfterRefill = fAfterRefill;
    }

    public String getFRefill() {
        return fRefill;
    }

    public void setFRefill(String fRefill) {
        this.fRefill = fRefill;
    }

    public String getTimeOfRefill() {
        return timeOfRefill;
    }

    public void setTimeOfRefill(String timeOfRefill) {
        this.timeOfRefill = timeOfRefill;
    }
}
