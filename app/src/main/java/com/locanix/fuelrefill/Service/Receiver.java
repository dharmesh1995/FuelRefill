package com.locanix.fuelrefill.Service;

import static android.content.Context.MODE_PRIVATE;

import static com.locanix.fuelrefill.Utils.Key.VehicleId;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.locanix.fuelrefill.BuildConfig;
import com.locanix.fuelrefill.DataBase.DataBase;
import com.locanix.fuelrefill.FuelrefillAddModel;
import com.locanix.fuelrefill.Model.GetfuelEntry.GetFuelEntryModel;
import com.locanix.fuelrefill.Retrofit.APIClient;
import com.locanix.fuelrefill.Retrofit.APIInterface;
import com.locanix.fuelrefill.SendMail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Receiver extends BroadcastReceiver {
    DataBase db;
    String VehicleId, FuelRefilled, RefillTime, CarDriverPhotoFilename, FuelDispenserBefo, FuelDispenserAfter;

    @Override
    public void onReceive(Context context, Intent intent) {

        /*db = new DataBase(context);*/

        SharedPreferences prefs = context.getSharedPreferences("n", MODE_PRIVATE);


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {


                VehicleId = prefs.getString("VehicleId", null);
                FuelRefilled = prefs.getString("FuelRefilled", null);
                RefillTime = prefs.getString("RefillTime", null);
                CarDriverPhotoFilename = prefs.getString("CarDriverPhotoFilename", null);
                FuelDispenserBefo = prefs.getString("FuelDispenserBefo", null);
                FuelDispenserAfter = prefs.getString("FuelDispenserAfter", null);

                Log.e("vid", "onReceive: "+VehicleId);
                Log.e("FuelRefilled", "onReceive: "+FuelRefilled);
                Log.e("RefillTime", "onReceive: "+RefillTime);
                Log.e("CarDriverPhotoFilename", "onReceive: "+CarDriverPhotoFilename);
                Log.e("FuelDispenserBefo", "onReceive: "+FuelDispenserBefo);
                Log.e("FuelDispenserAfter", "onReceive: "+FuelDispenserAfter);


                sendData(context, VehicleId, FuelRefilled, RefillTime, CarDriverPhotoFilename, FuelDispenserBefo, FuelDispenserAfter);

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                VehicleId = prefs.getString("VehicleId", null);
                FuelRefilled = prefs.getString("FuelRefilled", null);
                RefillTime = prefs.getString("RefillTime", null);
                CarDriverPhotoFilename = prefs.getString("CarDriverPhotoFilename", null);
                FuelDispenserBefo = prefs.getString("FuelDispenserBefo", null);
                FuelDispenserAfter = prefs.getString("FuelDispenserAfter", null);

                Log.e("vid", "onReceive: "+VehicleId);
                Log.e("FuelRefilled", "onReceive: "+FuelRefilled);
                Log.e("RefillTime", "onReceive: "+RefillTime);
                Log.e("CarDriverPhotoFilename", "onReceive: "+CarDriverPhotoFilename);
                Log.e("FuelDispenserBefo", "onReceive: "+FuelDispenserBefo);
                Log.e("FuelDispenserAfter", "onReceive: "+FuelDispenserAfter);

                sendData(context, VehicleId, FuelRefilled, RefillTime, CarDriverPhotoFilename, FuelDispenserBefo, FuelDispenserAfter);
            }
        }
    }

    private void sendData(Context context, String vehicleId, String fuelRefilled, String refillTime, String carDriverPhotoFilename, String fuelDispenserBefo, String fuelDispenserAfter) {


               /* Log.e("VehicleId", "sendData: " + c.getString(0));
                Log.e("FuelRefilled", "sendData: " + c.getString(1));
                Log.e("RefillTime", "sendData: " + c.getString(2));
                Log.e("CarDriverPhotoFilename", "sendData: " + c.getString(3));
                Log.e("FuelDispenserBefo", "sendData: " + c.getString(4));
                Log.e("FuelDispenserAfter", "sendData: " + c.getString(5));*/

        JsonObject object = new JsonObject();
        object.addProperty("VehicleId", vehicleId);
        object.addProperty("FuelRefilled", fuelRefilled);
        object.addProperty("RefillTime", refillTime);
        object.addProperty("CarDriverPhotoFilename", carDriverPhotoFilename);
        object.addProperty("FuelDispenserBeforeRefillPhotoFilename", fuelDispenserBefo);
        object.addProperty("FuelDispenserAfterRefillPhotoFilename", FuelDispenserAfter);

        Call<FuelrefillAddModel> call1 = APIClient.getClient().create(APIInterface.class).uploadRefillData(BuildConfig.TOKRN, "application/json", "application/json", object);
        call1.enqueue(new Callback<FuelrefillAddModel>() {
            @Override
            public void onResponse(@NonNull Call<FuelrefillAddModel> call, @NonNull Response<FuelrefillAddModel> response) {

                Log.e("1", "onResponse: " + response.isSuccessful());

                FuelrefillAddModel model = response.body();

                if (model != null) {

                    if (model.success) {

                        Log.e("entryid", "onriver: " + model.data.fuelEntryId);
                        GetFuelEntryData(context, vehicleId, model.data.fuelEntryId);
                        /*Toast.makeText(context, "Data Upload on Server", Toast.LENGTH_LONG).show();*/

                    } else {



                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<FuelrefillAddModel> call, @NonNull Throwable t) {
                call.cancel();
                Log.e("ff", "onResponse: " + t.getMessage());
            }
        });
    }


    public void GetFuelEntryData(Context context, String stringExtra, int fuelEntryId) {

        Call<GetFuelEntryModel> call1 = APIClient.getClient().create(APIInterface.class).getfuelentryresponse(BuildConfig.TOKRN, stringExtra, String.valueOf(fuelEntryId));

        call1.enqueue(new Callback<GetFuelEntryModel>() {
            @Override
            public void onResponse(@NonNull Call<GetFuelEntryModel> call, @NonNull Response<GetFuelEntryModel> response) {

                Log.e("2", "onResponse: " + response.isSuccessful());

                if (response.isSuccessful()) {

                    GetFuelEntryModel fuelEntryModel = response.body();

                    if (fuelEntryModel != null) {

                        if (fuelEntryModel.success) {

                            /*Toast.makeText(context, "All Data Sync to Server Successfully", Toast.LENGTH_SHORT).show();*/

                        } else {

                            SendMail sm = new SendMail(context, "vibeplen@gmail.com", "Fueld Refill", String.valueOf(fuelEntryId));
                            sm.execute();
                        }
                    }

                } else {

                    SendMail sm = new SendMail(context, "vibeplen@gmail.com", "Fueld Refill", String.valueOf(fuelEntryId));
                    sm.execute();
                }


            }

            @Override
            public void onFailure(@NonNull Call<GetFuelEntryModel> call, @NonNull Throwable throwable) {

                call.cancel();

            }
        });


    }
}