package com.cubezytech.fuelrefill.Activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.cubezytech.fuelrefill.BuildConfig;
import com.cubezytech.fuelrefill.Model.EntryFuel.EntryFuelResponse;
import com.cubezytech.fuelrefill.Model.UploadImage.UploadFileResponse;
import com.cubezytech.fuelrefill.Model.VehicalList.VehicleListResponse;
import com.cubezytech.fuelrefill.R;
import com.cubezytech.fuelrefill.Utils.Const;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class FewRefilleActivity extends AppCompatActivity {

    TextView tvVehicalNol;
    RelativeLayout rl_main;
    ImageView imgCarDriver, imgBeforeRefill, imgAfterRefill, imgBack;
    TextView tvSave, tvCancel;
    TextView currentTimeStamp;
    TextInputEditText etVolume;

    private String from = "";
    private String path = "";

    private String CarDriverPhotoFilename = "";
    private String FuelDispenserBeforeRefillPhotoFilename = "";
    private String FuelDispenserAfterRefillPhotoFilename = "";

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_few_refille);

        AndroidNetworking.initialize(FewRefilleActivity.this);

        path = getIntent().getStringExtra("path");
        dialog = new ProgressDialog(FewRefilleActivity.this);

        tvVehicalNol = findViewById(R.id.tvVehicalNol);
        imgCarDriver = findViewById(R.id.imgCarDriver);
        imgBeforeRefill = findViewById(R.id.imgBeforeRefill);
        imgAfterRefill = findViewById(R.id.imgAfterRefill);
        currentTimeStamp = findViewById(R.id.currentTimeStamp);
        imgBack = findViewById(R.id.imgBack);
        tvSave = findViewById(R.id.tvSave);
        tvCancel = findViewById(R.id.tvCancel);
        etVolume = findViewById(R.id.etVolume);
        rl_main = findViewById(R.id.rl_main);

        tvVehicalNol.setText(Const.vehicalNo);

        if (Const.currentTimeStamp.equals("")) {
            String dateString = DateFormat.format("dd-MM-yyyy hh:mm:ss", new Date(System.currentTimeMillis())).toString();
            Const.currentTimeStamp = dateString;
            Const.currentTimeStamp1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).format( new Date(System.currentTimeMillis()));
            Log.e("LLLL_date: ",Const.currentTimeStamp1);
        }
        currentTimeStamp.setText(Const.currentTimeStamp);

        if (Const.carDriver != null && !Const.carDriver.equals("")) {
            Bitmap bitmap = BitmapFactory.decodeFile(Const.carDriver);
            Glide
                    .with(FewRefilleActivity.this)
                    .load(bitmap)
                    .into(imgCarDriver);
        }
        if (Const.beforeRefill != null && !Const.beforeRefill.equals("")) {
            Bitmap bitmap = BitmapFactory.decodeFile(Const.beforeRefill);
            Glide
                    .with(FewRefilleActivity.this)
                    .load(bitmap)
                    .into(imgBeforeRefill);
        }
        if (Const.afterRefill != null && !Const.afterRefill.equals("")) {
            Bitmap bitmap = BitmapFactory.decodeFile(Const.afterRefill);
            Glide
                    .with(FewRefilleActivity.this)
                    .load(bitmap)
                    .into(imgAfterRefill);
        }

        imgCarDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from = "carDriver";
                Intent intent = new Intent(FewRefilleActivity.this, CameraActivity.class);
                intent.putExtra("from", from);
                startActivity(intent);
                finish();
            }
        });

        imgBeforeRefill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from = "beforeRefill";
                Intent intent = new Intent(FewRefilleActivity.this, CameraActivity.class);
                intent.putExtra("from", from);
                startActivity(intent);
                finish();
            }
        });

        imgAfterRefill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from = "afterRefill";
                Intent intent = new Intent(FewRefilleActivity.this, CameraActivity.class);
                intent.putExtra("from", from);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(v -> onBackPressed());

        tvCancel.setOnClickListener(v -> onBackPressed());

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Const.carDriver != null && !Const.carDriver.equals("")
                        && Const.beforeRefill != null && !Const.beforeRefill.equals("")
                        && Const.afterRefill != null && !Const.afterRefill.equals("")
                        && !etVolume.getText().toString().equals("")) {
                    if (Const.isInternetConnected(FewRefilleActivity.this)) {
                        new LongOperation().execute();
                    } else {
                        Toast.makeText(FewRefilleActivity.this, "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FewRefilleActivity.this, "Please enter all the details first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadCarFile(File file) {
        runOnUiThread(() -> {
            rl_main.setVisibility(View.VISIBLE);
        });

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
                        Log.e("LLLL_Response: ",uploadFileResponse.toString());
                        if (uploadFileResponse.isSuccess()) {
                            CarDriverPhotoFilename = uploadFileResponse.getData();
                            uploadBeforeRefillFile(new File(Const.beforeRefill));
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("LLL_error1: ",error.getErrorBody());
                        Log.e("LLL_error1: ",error.getErrorDetail());
                    }
                });
    }

    private void uploadBeforeRefillFile(File file) {
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
                        Log.e("LLLL_Response1: ",uploadFileResponse.toString());
                        if (uploadFileResponse.isSuccess()) {
                            FuelDispenserBeforeRefillPhotoFilename = uploadFileResponse.getData();
                            uploadAfterRefillFile(new File(Const.afterRefill));
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("LLL_error2: ",error.getErrorDetail());
                    }
                });
    }

    private void uploadAfterRefillFile(File file) {
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
                        Log.e("LLLL_Response2: ",uploadFileResponse.toString());
                        if (uploadFileResponse.isSuccess()) {
                            FuelDispenserAfterRefillPhotoFilename = uploadFileResponse.getData();
                            enterRefillData();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("LLL_error3: ",error.getErrorDetail());
                    }
                });
    }

    private void enterRefillData() {
        String FuelRefilled = String.format("%.2f", Float.parseFloat(etVolume.getText().toString().trim()));

        AndroidNetworking.post(BuildConfig.BASE_URL+"fuel/add/entry")
                .addHeaders("token", BuildConfig.TOKRN)
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter("VehicleId", String.valueOf(Const.vehicalID))
                .addBodyParameter("FuelRefilled",FuelRefilled)
                .addBodyParameter("RefillTime",Const.currentTimeStamp1)
                .addBodyParameter("CarDriverPhotoFilename",CarDriverPhotoFilename)
                .addBodyParameter("FuelDispenserBeforeRefillPhotoFilename",FuelDispenserBeforeRefillPhotoFilename)
                .addBodyParameter("FuelDispenserAfterRefillPhotoFilename",FuelDispenserAfterRefillPhotoFilename)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        EntryFuelResponse entryFuelResponse = new Gson().fromJson(response.toString(), EntryFuelResponse.class);
                        if (entryFuelResponse.isSuccess()) {
                            Log.i("LLLL_Entry_Res: ",entryFuelResponse.getData().toString());
                            runOnUiThread(() -> {
                                File tempFolder = new File(Const.IMAGE_PATH);
                                if (tempFolder.listFiles() != null) {
                                    for (File f : Objects.requireNonNull(tempFolder.listFiles())) {
                                        ContentResolver resolver = getApplicationContext().getContentResolver();
                                        resolver.delete(
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA +
                                                        " =?", new String[]{f.getAbsolutePath()});
                                    }
                                }
                                if (tempFolder.exists())
                                    tempFolder.delete();
                                Const.clearData();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rl_main.setVisibility(View.GONE);
                                    }
                                });
                                Toast.makeText(FewRefilleActivity.this, "Fuel Refill Successfully...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(FewRefilleActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            });
                        } else {
                            Log.i("LLLL_Entry_Res: ",entryFuelResponse.getError());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Error_Entry: ",anError.getErrorDetail());
                    }
                });
    }

    private final class LongOperation extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        public LongOperation() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            uploadCarFile(new File(Const.carDriver));
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Executed")) {

            }
        }
    }

    @Override
    public void onBackPressed() {
        try {
            File tempFolder = new File(Const.IMAGE_PATH);
            if (tempFolder.listFiles() != null) {
                for (File f : Objects.requireNonNull(tempFolder.listFiles())) {

                    ContentResolver resolver = getApplicationContext().getContentResolver();
                    resolver.delete(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA +
                                    " =?", new String[]{f.getAbsolutePath()});
                }
            }
            if (tempFolder.exists())
                tempFolder.delete();

            Const.clearData();
            Intent intent = new Intent(FewRefilleActivity.this, ScanActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}