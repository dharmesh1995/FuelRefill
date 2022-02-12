package com.locanix.fuelrefill.Service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.locanix.fuelrefill.DataBase.DataBase;
import com.locanix.fuelrefill.Utils.ConnectionDetector;

public class AlarmService extends JobIntentService {

    static final int JOB_ID = 1000;
    public LocationManager mLocManager;
    public LocationManager locationManager;
    public Location previousBestLocation = null;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    DataBase db;

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, AlarmService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        db = new DataBase(getApplicationContext());
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        Cursor c = db.getData();
        if (c != null) {
            while (c.moveToNext()) {
                Log.e("Test","data "+c.getString(3));
            }
        }
        if (isInternetPresent){

        }
    }
}
