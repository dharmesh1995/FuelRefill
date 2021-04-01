package com.cubezytech.fuelrefill.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.cubezytech.fuelrefill.BaseActivity;
import com.cubezytech.fuelrefill.BuildConfig;
import com.cubezytech.fuelrefill.Model.VehicalList.DataItem;
import com.cubezytech.fuelrefill.Model.VehicalList.VehicleListResponse;
import com.cubezytech.fuelrefill.R;
import com.cubezytech.fuelrefill.Utils.Const;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.zxing.Result;

import org.json.JSONObject;

public class ScanActivity extends BaseActivity {

    MaterialCardView cvScan;
    FrameLayout frameBottom;
    RelativeLayout rlTop;
    CodeScannerView scanner_view;

    private CodeScanner mCodeScanner;
    String from = "";

    @Override
    public void permissionGranted() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        AndroidNetworking.initialize(ScanActivity.this);

        from = getIntent().getStringExtra("from");

        cvScan = findViewById(R.id.cvScan);
        rlTop = findViewById(R.id.rlTop);
        frameBottom = findViewById(R.id.frameBottom);
        scanner_view = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(ScanActivity.this, scanner_view);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                if (Const.isInternetConnected(ScanActivity.this)) {
                    runOnUiThread(() -> getVehicleList(result.toString()));
                } else {
                    Toast.makeText(ScanActivity.this, "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cvScan.setOnClickListener(v -> {
            rlTop.setVisibility(View.GONE);
            frameBottom.setVisibility(View.VISIBLE);
            mCodeScanner.startPreview();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void getVehicleList(String scanCode) {
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
                                if (dataItem.getDescription()!=null) {
                                    if (dataItem.getDescription().equals(scanCode)) {
                                        Const.vehicalNo = dataItem.getVehiclenumber();
                                        Const.vehicalID = dataItem.getVehicleid();
                                        Intent intent;
                                        intent = new Intent(ScanActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(ScanActivity.this, "Something went wrong....", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLL_Error: ", anError.getErrorDetail());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}