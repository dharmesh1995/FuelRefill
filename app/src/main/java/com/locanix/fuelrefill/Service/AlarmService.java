package com.locanix.fuelrefill.Service;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.gson.Gson;
import com.locanix.fuelrefill.BuildConfig;
import com.locanix.fuelrefill.DBHelper.DBHelper;
import com.locanix.fuelrefill.Model.EntryFuel.EntryFuelResponse;
import com.locanix.fuelrefill.Model.EntryFuel.FuelRefill;
import com.locanix.fuelrefill.Model.UploadImage.UploadFileResponse;
import com.locanix.fuelrefill.Model.VehicalList.DataItem;
import com.locanix.fuelrefill.Model.VehicalList.VehicleListResponse;
import com.locanix.fuelrefill.Utils.ConnectionDetector;
import com.locanix.fuelrefill.Utils.Const;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class AlarmService extends JobIntentService {

    public static final String ANDROID_CHANNEL_ID = "com.bbotdev.weather";
    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager mLocManager;
    public LocationManager locationManager;
    public Location previousBestLocation = null;
    String mainKey = "";
    int Id;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    DBHelper dbHelper;
    private String CarDriverPhotoFilename = "";
    private String FuelDispenserBeforeRefillPhotoFilename = "";
    private String FuelDispenserAfterRefillPhotoFilename = "";

    /**
     * Convenience method for enqueuing work in to this service.
     */
    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, AlarmService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.e("LLL_Service: ", "Running");
        dbHelper = new DBHelper(getApplicationContext());
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        ArrayList<FuelRefill> fuelRefills;
        fuelRefills = dbHelper.getPillsNoRecords();

        if (isInternetPresent) {
            if (fuelRefills != null || !fuelRefills.isEmpty()) {
                for (int i = 0; i < fuelRefills.size(); i++) {
                    FuelRefill fuelRefill = fuelRefills.get(i);
                    if (fuelRefill.getFAfterRefill() != null && !fuelRefill.getFAfterRefill().equals("")) {
                        getVehicleList(fuelRefill.get_id(), fuelRefill.getScanCode(), fuelRefill);
                    }
                }
            }
        }
    }

    private void uploadCarFile(File file, FuelRefill fuelRefill) {
        AndroidNetworking.upload(BuildConfig.BASE_URL + "fuel/upload/file?photoType=CARDRIVER")
                .addHeaders("token", BuildConfig.TOKRN)
                .addMultipartFile(file.getName(), file)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UploadFileResponse uploadFileResponse = new Gson().fromJson(response.toString(), UploadFileResponse.class);
                        Log.e("LLLL_Response: ", uploadFileResponse.toString());
                        if (uploadFileResponse.isSuccess()) {
                            CarDriverPhotoFilename = uploadFileResponse.getData();
                            uploadBeforeRefillFile(new File(fuelRefill.getFBeforeRefill()), fuelRefill);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("LLL_error1: ", error.getErrorBody());
                        Log.e("LLL_error1: ", error.getErrorDetail());
                    }
                });
    }

    private void uploadBeforeRefillFile(File file, FuelRefill fuelRefill) {
        AndroidNetworking.upload(BuildConfig.BASE_URL + "fuel/upload/file?photoType=BEFOREREFILL")
                .addHeaders("token", BuildConfig.TOKRN)
                .addHeaders("Content-Type", "application/json")
                .addMultipartFile(file.getName(), file)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UploadFileResponse uploadFileResponse = new Gson().fromJson(response.toString(), UploadFileResponse.class);
                        Log.e("LLLL_Response1: ", uploadFileResponse.toString());
                        if (uploadFileResponse.isSuccess()) {
                            FuelDispenserBeforeRefillPhotoFilename = uploadFileResponse.getData();
                            uploadAfterRefillFile(new File(fuelRefill.getFAfterRefill()), fuelRefill);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("LLL_error2: ", error.getErrorDetail());
                    }
                });
    }

    private void uploadAfterRefillFile(File file, FuelRefill fuelRefill) {
        AndroidNetworking.upload(BuildConfig.BASE_URL + "fuel/upload/file?photoType= AFTERREFILL")
                .addHeaders("token", BuildConfig.TOKRN)
                .addHeaders("Content-Type", "application/json")
                .addMultipartFile(file.getName(), file)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UploadFileResponse uploadFileResponse = new Gson().fromJson(response.toString(), UploadFileResponse.class);
                        Log.e("LLLL_Response2: ", uploadFileResponse.toString());
                        if (uploadFileResponse.isSuccess()) {
                            FuelDispenserAfterRefillPhotoFilename = uploadFileResponse.getData();
                            enterRefillData(fuelRefill);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("LLL_error3: ", error.getErrorDetail());
                    }
                });
    }

    private void enterRefillData(FuelRefill fuelRefill) {

        AndroidNetworking.post(BuildConfig.BASE_URL + "fuel/add/entry")
                .addHeaders("token", BuildConfig.TOKRN)
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter("VehicleId", fuelRefill.getVehicleId())
                .addBodyParameter("FuelRefilled", fuelRefill.getFRefill())
                .addBodyParameter("RefillTime", fuelRefill.getTimeOfRefill())
                .addBodyParameter("CarDriverPhotoFilename", CarDriverPhotoFilename)
                .addBodyParameter("FuelDispenserBeforeRefillPhotoFilename", FuelDispenserBeforeRefillPhotoFilename)
                .addBodyParameter("FuelDispenserAfterRefillPhotoFilename", FuelDispenserAfterRefillPhotoFilename)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        EntryFuelResponse entryFuelResponse = new Gson().fromJson(response.toString(), EntryFuelResponse.class);
                        if (entryFuelResponse.isSuccess()) {
                            Log.i("LLLL_Entry_Res: ", entryFuelResponse.getData().toString());

                            File tempFolder = new File(Const.IMAGE_PATH);
                            if (tempFolder.listFiles() != null) {
                                for (File f : Objects.requireNonNull(tempFolder.listFiles())) {
                                    ContentResolver resolver = getApplicationContext().getContentResolver();
                                    resolver.delete(
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA +
                                                    " =?", new String[]{f.getAbsolutePath()});
                                    if (f.exists())
                                        f.delete();
                                }
                            }
                            if (tempFolder.exists())
                                tempFolder.delete();
                            Const.clearData();
                            dbHelper.deletePillsNoRecords(fuelRefill.get_id(), fuelRefill.getVehicleId(), fuelRefill.getTimeOfRefill());
                        } else {
                            Log.e("LLLL_Entry_Res: ", entryFuelResponse.getError());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Error_Entry: ", anError.getErrorDetail());
                    }
                });
    }

    private void getVehicleList(int id, String scanCode, FuelRefill fuelRefill) {
        AndroidNetworking.get(BuildConfig.BASE_URL + "vehicles")
                .addHeaders("token", BuildConfig.TOKRN)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VehicleListResponse vehicleListResponse = new Gson().fromJson(response.toString(), VehicleListResponse.class);
                        Log.v("LLLL_Response: ", vehicleListResponse.toString());
                        if (vehicleListResponse.isSuccess()) {
                            for (int i = 0; i < vehicleListResponse.getData().size(); i++) {
                                DataItem dataItem = vehicleListResponse.getData().get(i);
                                if (dataItem.getDescription() != null) {
                                    if (dataItem.getDescription().equals(scanCode)) {
                                        Const.vehicalNo = dataItem.getVehiclenumber();
                                        Const.vehicalID = dataItem.getVehicleid();
                                        dbHelper.updateVehicleNo(scanCode, id, String.valueOf(dataItem.getVehicleid()));
                                        new LongOperation(fuelRefill).execute();
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong....", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLL_Error: ", anError.getErrorDetail());
                    }
                });
    }


    private final class LongOperation extends AsyncTask<Void, Void, String> {
        FuelRefill fuelRefill;
        private ProgressDialog dialog;

        public LongOperation(FuelRefill fuelRefill) {
            this.fuelRefill = fuelRefill;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            uploadCarFile(new File(fuelRefill.getFDriver()), fuelRefill);

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Executed")) {

            }
        }
    }

}
