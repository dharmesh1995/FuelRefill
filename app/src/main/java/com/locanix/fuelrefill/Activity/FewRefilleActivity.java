package com.locanix.fuelrefill.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.locanix.fuelrefill.BuildConfig;
import com.locanix.fuelrefill.DBHelper.DBHelper;
import com.locanix.fuelrefill.Model.EntryFuel.EntryFuelResponse;
import com.locanix.fuelrefill.Model.UploadImage.UploadFileResponse;
import com.locanix.fuelrefill.R;
import com.locanix.fuelrefill.Utils.ConnectionDetector;
import com.locanix.fuelrefill.Utils.Const;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class FewRefilleActivity extends AppCompatActivity {

    private static final Pattern sPattern
            = Pattern.compile("/^[0-9]*(\\.[0-9]{0,2})?$/");
    private final int CAMERA_PIC_REQUEST = 100;
    private final int CAMERA_PIC_REQUEST1 = 101;
    private final int CAMERA_PIC_REQUEST2 = 102;
    TextView tvVehicalNol;
    RelativeLayout rl_main;
    ImageView imgCarDriver, imgBeforeRefill, imgAfterRefill, imgBack;
    TextView tvSave, tvCancel;
    TextView currentTimeStamp;
    TextInputEditText etVolume;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    DBHelper dbHelper;
    private String from = "";
    private String path = "";
    private String CarDriverPhotoFilename = "";
    private String FuelDispenserBeforeRefillPhotoFilename = "";
    private String FuelDispenserAfterRefillPhotoFilename = "";
    private ProgressDialog dialog;
    private CharSequence mText;
    private FirebaseAnalytics mFirebaseAnalytics;

    private void fireAnalytics(String arg1, String arg2) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, arg1);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, arg2);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
    }

    private boolean isValid(CharSequence s) {
        return sPattern.matcher(s).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_few_refille);

        dbHelper = new DBHelper(FewRefilleActivity.this);
        AndroidNetworking.initialize(FewRefilleActivity.this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(FewRefilleActivity.this);

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

        if (!Const.vehicalNo.equals("")) {
            tvVehicalNol.setText(Const.vehicalNo);
        } else {
            tvVehicalNol.setText(dbHelper.getScanCode());
        }

        if (Const.currentTimeStamp.equals("")) {
            String dateString = DateFormat.format("dd-MMM-yyyy hh:mm a", new Date(System.currentTimeMillis())).toString();
            Const.currentTimeStamp = dateString;
            Const.currentTimeStamp1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).format(new Date(System.currentTimeMillis()));
            Log.e("LLLL_date: ", Const.currentTimeStamp1);
        }
        currentTimeStamp.setText(Const.currentTimeStamp);

        imgCarDriver.setOnClickListener(v -> {
            from = "carDriver";
            fireAnalytics("Car_Driver Picture click", Const.vehicalNo);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        });

        imgBeforeRefill.setOnClickListener(v -> {
            from = "beforeRefill";
            fireAnalytics("fuel refill dispenser Before picture", Const.vehicalNo);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST1);
        });

        imgAfterRefill.setOnClickListener(v -> {
            from = "afterRefill";
            fireAnalytics("fuel refill dispenser After picture", Const.vehicalNo);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST2);
        });

        imgBack.setOnClickListener(v -> onBackPressed());

        tvCancel.setOnClickListener(v -> onBackPressed());

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

        tvSave.setOnClickListener(v -> {

            if (Const.carDriver != null && !Const.carDriver.equals("")
                    && Const.beforeRefill != null && !Const.beforeRefill.equals("")
                    && Const.afterRefill != null && !Const.afterRefill.equals("")
                    && !etVolume.getText().toString().equals("")) {
                if (Const.isInternetConnected(FewRefilleActivity.this)) {
                    new LongOperation().execute();
                } else {
                    String FuelRefilled = String.format("%.2f", Float.parseFloat(etVolume.getText().toString().trim()));
                    String scanCode = dbHelper.getScanCode();
                    boolean isInsert = dbHelper.updateFuelNoRecords(String.valueOf(dbHelper.getId()), scanCode, String.valueOf(Const.vehicalID), Const.carDriver, Const.beforeRefill, Const.afterRefill, FuelRefilled, Const.currentTimeStamp1);
                    Log.e("LLL_Insert: ", String.valueOf(isInsert));
                    runOnUiThread(() -> Toasty.error(FewRefilleActivity.this, "No Internet Connection, please wait while you're in a network state", Toasty.LENGTH_LONG).show());
                    Intent intent = new Intent(FewRefilleActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(FewRefilleActivity.this, "Please enter all the details first.", Toast.LENGTH_SHORT).show();
            }
        });

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (data != null) {
                Bitmap image = (Bitmap) data.getExtras().get("data");

                Glide
                        .with(FewRefilleActivity.this)
                        .load(image)
                        .into(imgCarDriver);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), image);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));

                Log.e("LLLL_data: ", finalFile.getAbsolutePath());

                Const.carDriver = finalFile.getAbsolutePath();
            }

        } else if (requestCode == CAMERA_PIC_REQUEST1) {

            if (data != null) {
                Bitmap image = (Bitmap) data.getExtras().get("data");

                Glide
                        .with(FewRefilleActivity.this)
                        .load(image)
                        .into(imgBeforeRefill);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), image);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));

                Log.e("LLLL_data: ", finalFile.getAbsolutePath());

                Const.beforeRefill = finalFile.getAbsolutePath();
            }

        } else if (requestCode == CAMERA_PIC_REQUEST2) {
            if (data != null) {

                Bitmap image = (Bitmap) data.getExtras().get("data");

                Glide
                        .with(FewRefilleActivity.this)
                        .load(image)
                        .into(imgAfterRefill);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), image);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));

                Log.e("LLLL_data: ", finalFile.getAbsolutePath());

                Const.afterRefill = finalFile.getAbsolutePath();
            }

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
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
                        Log.e("LLLL_Response: ", uploadFileResponse.toString());
                        if (uploadFileResponse.isSuccess()) {
                            CarDriverPhotoFilename = uploadFileResponse.getData();
                            uploadBeforeRefillFile(new File(Const.beforeRefill));
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("LLL_error1: ", error.getErrorBody());
                        Log.e("LLL_error1: ", error.getErrorDetail());
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
                        Log.e("LLLL_Response1: ", uploadFileResponse.toString());
                        if (uploadFileResponse.isSuccess()) {
                            FuelDispenserBeforeRefillPhotoFilename = uploadFileResponse.getData();
                            uploadAfterRefillFile(new File(Const.afterRefill));
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("LLL_error2: ", error.getErrorDetail());
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
                        Log.e("LLLL_Response2: ", uploadFileResponse.toString());
                        if (uploadFileResponse.isSuccess()) {
                            FuelDispenserAfterRefillPhotoFilename = uploadFileResponse.getData();
                            enterRefillData();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("LLL_error3: ", error.getErrorDetail());
                    }
                });
    }

    private void enterRefillData() {
        String FuelRefilled = String.format("%.2f", Float.parseFloat(etVolume.getText().toString().trim()));

        AndroidNetworking.post(BuildConfig.BASE_URL + "fuel/add/entry")
                .addHeaders("token", BuildConfig.TOKRN)
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter("VehicleId", String.valueOf(Const.vehicalID))
                .addBodyParameter("FuelRefilled", FuelRefilled)
                .addBodyParameter("RefillTime", Const.currentTimeStamp1)
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
                            fireAnalytics("Fuel Entry Added Successful", Const.vehicalNo);
                            runOnUiThread(() -> {
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

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rl_main.setVisibility(View.GONE);
                                    }
                                });
                                conformationDialog("Fuel Refill Successful");
//                                Toast.makeText(FewRefilleActivity.this, "Fuel Refill Successfully...", Toast.LENGTH_SHORT).show();

                            });
                        } else {
                            conformationDialog(entryFuelResponse.getError());
                            Log.e("LLLL_Entry_Res: ", entryFuelResponse.getError());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Error_Entry: ", anError.getErrorDetail());
                    }
                });
    }

    private void conformationDialog(String msg) {
        final Dialog dial = new Dialog(FewRefilleActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dial.requestWindowFeature(1);
        dial.setContentView(R.layout.dialog_confiormation);
        dial.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dial.setCanceledOnTouchOutside(true);

        TextView toastMes = dial.findViewById(R.id.toastMes);
        toastMes.setText(msg);

        dial.findViewById(R.id.tvOk).setOnClickListener(view -> {
            dial.dismiss();
            Intent intent = new Intent(FewRefilleActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        dial.show();
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

                    if (f.exists())
                        f.delete();
                }
            }
            if (tempFolder.exists())
                tempFolder.delete();

            Const.clearData();
            Intent intent = new Intent(FewRefilleActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            if (isInternetPresent)
                uploadCarFile(new File(Const.carDriver));
            else {
                String FuelRefilled = String.format("%.2f", Float.parseFloat(etVolume.getText().toString().trim()));
                String scanCode = dbHelper.getScanCode();
                boolean isInsert = dbHelper.updateFuelNoRecords(String.valueOf(dbHelper.getId()), scanCode, String.valueOf(Const.vehicalID), Const.carDriver, Const.beforeRefill, Const.afterRefill, FuelRefilled, Const.currentTimeStamp1);
                Log.e("LLL_Insert: ", String.valueOf(isInsert));
                runOnUiThread(() -> Toasty.error(FewRefilleActivity.this, "No Internet Connection, please wait while you're in a network state", Toasty.LENGTH_LONG).show());
                Intent intent = new Intent(FewRefilleActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Executed")) {

            }
        }
    }

}