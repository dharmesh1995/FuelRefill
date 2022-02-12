package com.locanix.fuelrefill.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonObject;
import com.locanix.fuelrefill.BuildConfig;
import com.locanix.fuelrefill.DataBase.DataBase;
import com.locanix.fuelrefill.FuelrefillAddModel;
import com.locanix.fuelrefill.Model.CarDriverModel;
import com.locanix.fuelrefill.Model.GetfuelEntry.GetFuelEntryModel;
import com.locanix.fuelrefill.ProgressBar.Progressbar;
import com.locanix.fuelrefill.R;
import com.locanix.fuelrefill.Retrofit.APIClient;
import com.locanix.fuelrefill.Retrofit.APIInterface;
import com.locanix.fuelrefill.SendMail;
import com.locanix.fuelrefill.Utils.ConnectionDetector;
import com.locanix.fuelrefill.Utils.Key;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

;

public class FuelRefilleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final Pattern sPattern
            = Pattern.compile("/^[0-9]*(\\.[0-9]{0,2})?$/");
    private static final String MY_PREFS_NAME = "n";
    private final int CAMERA_CarDriver = 100;
    private final int CAMERA_BeforeRefill = 101;
    private final int CAMERA_AfterRefill = 102;
    TextView tvVehicalNol;
    private CircleImageView imgCarDriver, imgBeforeRefill, imgAfterRefill;
    private TextView tvSave, tvCancel;
    private String vehicleNumber = "";
    private String carDriver = null, beforeRefill = null, afterRefill = null;
    private File carDriverFile, beforeRefillFile, afterRefillFile;
    private TextView currentTimeStamp;
    private String time;
    private Progressbar progressbar;
    TextInputEditText etVolume;
    ConnectionDetector cd;

    private String CarDriverPhotoFilename = "";
    private String FuelDispenserBeforeRefillPhotoFilename = "";
    private String FuelDispenserAfterRefillPhotoFilename = "";
    private ProgressDialog dialog;
    private FirebaseAnalytics mFirebaseAnalytics;
    public GetFuelEntryModel fuelEntryModel;
    DataBase db;



    private void fireAnalytics(String arg1, String arg2) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, arg1);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, arg2);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_refille);
        db = new DataBase(this);
        progressbar = new Progressbar(this);
        AndroidNetworking.initialize(FuelRefilleActivity.this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(FuelRefilleActivity.this);


        /*StrictMode.ThreadPolicy policy  =new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/

        dialog = new ProgressDialog(FuelRefilleActivity.this);

        tvVehicalNol = findViewById(R.id.tvVehicalNol);
        imgCarDriver = findViewById(R.id.imgCarDriver);
        imgCarDriver.setOnClickListener(this);
        imgBeforeRefill = findViewById(R.id.imgBeforeRefill);
        imgBeforeRefill.setOnClickListener(this);
        imgAfterRefill = findViewById(R.id.imgAfterRefill);
        imgAfterRefill.setOnClickListener(this);
        currentTimeStamp = findViewById(R.id.currentTimeStamp);
        tvSave = findViewById(R.id.tvSave);
        tvSave.setOnClickListener(this);
        tvCancel = findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(this);
        etVolume = findViewById(R.id.etVolume);

        if (getIntent().hasExtra(Key.VehicleNumber)) {

            vehicleNumber = getIntent().getStringExtra(Key.VehicleNumber);
            tvVehicalNol.setText(vehicleNumber);


            currentTimeStamp.setText(DateFormat.format("dd-MMM-yyyy hh:mm a", new Date(System.currentTimeMillis())).toString());
            time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).format(new Date(System.currentTimeMillis()));
        }

        etVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int length = text.length();

                if (etVolume.getText().toString().contains(".")) {
                    String string = etVolume.getText().toString().substring(etVolume.getText().toString().indexOf("."));
                    if (string != null && string.length() > 3) {
                        s.delete(length - 1, length);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                onBackPressed();
                break;
            case R.id.imgCarDriver:
                fireAnalytics("Car_Driver Picture click", vehicleNumber);
                Intent CarDriverIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(CarDriverIntent, CAMERA_CarDriver);
                break;
            case R.id.imgBeforeRefill:
                fireAnalytics("fuel refill dispenser Before picture", vehicleNumber);
                Intent BeforeRefillIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(BeforeRefillIntent, CAMERA_BeforeRefill);
                break;
            case R.id.imgAfterRefill:
                fireAnalytics("fuel refill dispenser After picture", vehicleNumber);
                Intent AfterRefillIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(AfterRefillIntent, CAMERA_AfterRefill);
                break;
            case R.id.tvSave:

                if (carDriver == null) {
                    Toast.makeText(getApplicationContext(), "Please Take Car & Driver Picture.", Toast.LENGTH_LONG).show();
                } else if (beforeRefill == null) {
                    Toast.makeText(getApplicationContext(), "Please Take Picture Fuel Dispenser Before Refill.", Toast.LENGTH_LONG).show();
                } else if (afterRefill == null) {
                    Toast.makeText(getApplicationContext(), "Please Take Picture Fuel Dispenser After Refill.", Toast.LENGTH_LONG).show();
                } else if (etVolume.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Please Enter Fuel Refilled Value.", Toast.LENGTH_LONG).show();
                } else {
                    progressbar.show();

                    if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                        enterRefillData();
                    } else {



                        Log.e("VehicleId", "f: "+getIntent().getStringExtra(Key.VehicleId));
                        Log.e("FuelRefilled", "f: "+String.format("%.2f", Float.parseFloat(etVolume.getText().toString().trim())));
                        Log.e("RefillTime", "f: "+time);
                        Log.e("CarDriverPhotoFilename", "f: "+carDriver);
                        Log.e("FuelDispenserBefo", "f: "+beforeRefill);
                        Log.e("FuelDispenserAfter", "f: "+afterRefill);


                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("VehicleId", getIntent().getStringExtra(Key.VehicleId));
                        editor.putString("FuelRefilled", String.format("%.2f", Float.parseFloat(etVolume.getText().toString().trim())));
                        editor.putString("RefillTime", time);
                        editor.putString("CarDriverPhotoFilename", carDriver);
                        editor.putString("FuelDispenserBefo", beforeRefill);
                        editor.putString("FuelDispenserAfter", afterRefill);
                        editor.apply();

                        if (new DataBase(this).insertData(getIntent().getStringExtra(Key.VehicleId),String.format("%.2f", Float.parseFloat(etVolume.getText().toString().trim())), time, carDriver, beforeRefill, afterRefill)) {

                            if (progressbar.isShowing()) {
                                progressbar.dismiss();
                            }

                            Toast.makeText(getApplicationContext(), " If Internet Connection back your data send server. ", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_CarDriver:
                    Bitmap carDriver = (Bitmap) data.getExtras().get("data");
                    if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                        progressbar.show();
                        uploadCarFile(storeImage(carDriver));
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection, please wait while you're in a network state", Toast.LENGTH_LONG).show();
                    }
                    break;
                case CAMERA_BeforeRefill:
                    Bitmap beforeRefill = (Bitmap) data.getExtras().get("data");
                    if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                        progressbar.show();
                        uploadBeforeRefillFile(storeImage(beforeRefill));
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection, please wait while you're in a network state", Toast.LENGTH_LONG).show();
                    }
                    break;
                case CAMERA_AfterRefill:
                    Bitmap afterRefill = (Bitmap) data.getExtras().get("data");
                    if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                        progressbar.show();
                        uploadAfterRefillFile(storeImage(afterRefill));
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection, please wait while you're in a network state", Toast.LENGTH_LONG).show();
                    }

                    break;
            }
        }
    }

    private File storeImage(Bitmap image) {

        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("Tets", "Error creating media file, check storage permissions: ");
            return pictureFile;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("Tets", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("Tets", "Error accessing file: " + e.getMessage());
        }
        return pictureFile;
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private void uploadCarFile(File file) {
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        Call<CarDriverModel> call1 = APIClient.getClient().create(APIInterface.class).uploadCar(BuildConfig.TOKRN, multipartBody);
        call1.enqueue(new Callback<CarDriverModel>() {
            @Override
            public void onResponse(Call<CarDriverModel> call, Response<CarDriverModel> response) {
                CarDriverModel model = response.body();
                if (model.success) {
                    carDriver = model.data;
                    Glide.with(FuelRefilleActivity.this)
                            .load(file)
                            .into(imgCarDriver);
                    if (progressbar.isShowing()) {
                        progressbar.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<CarDriverModel> call, Throwable t) {
                call.cancel();
                if (progressbar.isShowing()) {
                    progressbar.dismiss();
                }
            }
        });
    }

    private void uploadBeforeRefillFile(File file) {

        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        Call<CarDriverModel> call1 = APIClient.getClient().create(APIInterface.class).uploadBeforeRefill(BuildConfig.TOKRN, multipartBody);
        call1.enqueue(new Callback<CarDriverModel>() {
            @Override
            public void onResponse(Call<CarDriverModel> call, Response<CarDriverModel> response) {
                CarDriverModel model = response.body();
                if (model.success) {
                    beforeRefill = model.data;
                    Glide.with(FuelRefilleActivity.this)
                            .load(file)
                            .into(imgBeforeRefill);
                    if (progressbar.isShowing()) {
                        progressbar.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<CarDriverModel> call, Throwable t) {
                call.cancel();
                if (progressbar.isShowing()) {
                    progressbar.dismiss();
                }
            }
        });
    }

    private void uploadAfterRefillFile(File file) {
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        Call<CarDriverModel> call1 = APIClient.getClient().create(APIInterface.class).uploadAfterRefill(BuildConfig.TOKRN, multipartBody);
        call1.enqueue(new Callback<CarDriverModel>() {
            @Override
            public void onResponse(@NonNull Call<CarDriverModel> call, @NonNull Response<CarDriverModel> response) {
                CarDriverModel model = response.body();

                if (model.success) {
                    afterRefill = model.data;
                    Glide.with(FuelRefilleActivity.this)
                            .load(file)
                            .into(imgAfterRefill);
                    if (progressbar.isShowing()) {
                        progressbar.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CarDriverModel> call, @NonNull Throwable t) {
                call.cancel();
                if (progressbar.isShowing()) {
                    progressbar.dismiss();
                }
            }
        });
    }

    private void enterRefillData() {


        /*Log.e("VehicleId", "f: "+getIntent().getStringExtra(Key.VehicleId));
        Log.e("FuelRefilled", "f: "+String.format("%.2f", Float.parseFloat(etVolume.getText().toString().trim())));
        Log.e("RefillTime", "f: "+time);
        Log.e("CarDriverPhotoFilename", "f: "+carDriver);
        Log.e("FuelDispenserBefo", "f: "+beforeRefill);
        Log.e("FuelDispenserAfter", "f: "+afterRefill);*/

        JsonObject object = new JsonObject();
        object.addProperty("VehicleId", getIntent().getStringExtra(Key.VehicleId));
        object.addProperty("FuelRefilled", String.format("%.2f", Float.parseFloat(etVolume.getText().toString().trim())));
        object.addProperty("RefillTime", time);
        object.addProperty("CarDriverPhotoFilename", carDriver);
        object.addProperty("FuelDispenserBeforeRefillPhotoFilename", beforeRefill);
        object.addProperty("FuelDispenserAfterRefillPhotoFilename", afterRefill);
        Call<FuelrefillAddModel> call1 = APIClient.getClient().create(APIInterface.class).uploadRefillData(BuildConfig.TOKRN, "application/json", "application/json", object);
        call1.enqueue(new Callback<FuelrefillAddModel>() {
            @Override
            public void onResponse(@NonNull Call<FuelrefillAddModel> call, @NonNull Response<FuelrefillAddModel> response) {

                    FuelrefillAddModel model = response.body();

                    if (model != null) {

                        if (model.success) {

                            Log.e("id", "onResponse: " + model.data.fuelEntryId);
                            GetFuelEntryData(getIntent().getStringExtra(Key.VehicleId), model.data.fuelEntryId);
                            Toast.makeText(getApplicationContext(), "All data save successfully.", Toast.LENGTH_LONG).show();
                            onBackPressed();

                            if (progressbar.isShowing()) {
                                progressbar.dismiss();
                            }

                        } else {

                        }

                    } else {


                    }

                }




            @Override
            public void onFailure(Call<FuelrefillAddModel> call, Throwable t) {
                call.cancel();
                if (progressbar.isShowing()) {
                    progressbar.dismiss();
                }
            }
        });
    }

    public void GetFuelEntryData(String stringExtra, int fuelEntryId) {

        Call<GetFuelEntryModel> call1 = APIClient.getClient().create(APIInterface.class).getfuelentryresponse(BuildConfig.TOKRN, stringExtra, String.valueOf(fuelEntryId));

        call1.enqueue(new Callback<GetFuelEntryModel>() {
            @Override
            public void onResponse(@NonNull Call<GetFuelEntryModel> call, @NonNull Response<GetFuelEntryModel> response) {
                if (response.isSuccessful()){

                    fuelEntryModel = response.body();

                    if (fuelEntryModel != null) {

                        if (fuelEntryModel.success) {

                            Toast.makeText(FuelRefilleActivity.this, "All Data Sync to Server Successfully", Toast.LENGTH_SHORT).show();

                            if (progressbar.isShowing()) {
                                progressbar.dismiss();
                            }
                        }else {

                            SendMail sm = new SendMail(FuelRefilleActivity.this, "vibeplen@gmail.com", "Fueld Refill",String.valueOf(fuelEntryId));
                            sm.execute();
                        }
                    }

                }else {

                    SendMail sm = new SendMail(FuelRefilleActivity.this, "vibeplen@gmail.com", "Fueld Refill",String.valueOf(fuelEntryId));
                    sm.execute();
                }



            }

            @Override
            public void onFailure(@NonNull Call<GetFuelEntryModel> call, @NonNull Throwable throwable) {

                call.cancel();

                if (progressbar.isShowing()) {
                    progressbar.dismiss();
                }
            }
        });


    }



}