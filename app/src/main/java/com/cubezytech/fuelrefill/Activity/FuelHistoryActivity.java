package com.cubezytech.fuelrefill.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.cubezytech.fuelrefill.Adapter.FuelHistoryAdapter;
import com.cubezytech.fuelrefill.BaseActivity;
import com.cubezytech.fuelrefill.BuildConfig;
import com.cubezytech.fuelrefill.Model.FuelHistory.DataItem;
import com.cubezytech.fuelrefill.Model.FuelHistory.FuelHistoryResponse;
import com.cubezytech.fuelrefill.R;
import com.cubezytech.fuelrefill.Utils.Const;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class FuelHistoryActivity extends BaseActivity {

    ImageView imgBack;
    RecyclerView rvImages;
    TextView tvVehicalNol;
    RelativeLayout rl_main;

    FuelHistoryAdapter fuelHistoryAdapter;
    ArrayList<DataItem> dataItems = new ArrayList<>();

    private ProgressDialog dialog;

    @Override
    public void permissionGranted() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_history);

        AndroidNetworking.initialize(FuelHistoryActivity.this);

        dialog = new ProgressDialog(FuelHistoryActivity.this);

        imgBack = findViewById(R.id.imgBack);
        rl_main = findViewById(R.id.rl_main);
        tvVehicalNol = findViewById(R.id.tvVehicalNol);
        imgBack.setOnClickListener(v -> onBackPressed());

        tvVehicalNol.setText(Const.vehicalNo);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        String tomorrowDate = DateFormat.format("dd/MM/yyyy", tomorrow).toString();

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(tomorrow);
        calendar1.add(Calendar.DAY_OF_YEAR, -7);
        Date newDate = calendar1.getTime();

        String _7daysBeforeDate = DateFormat.format("dd/MM/yyyy", newDate).toString();

        Log.e("LLLL_tomorrow: ",tomorrowDate);
        Log.e("LLLL_7daysBeforeDate: ",_7daysBeforeDate);

        rvImages = findViewById(R.id.rvImages);
        rvImages.setLayoutManager(new LinearLayoutManager(FuelHistoryActivity.this,RecyclerView.VERTICAL,false));
        fuelHistoryAdapter = new FuelHistoryAdapter(dataItems,FuelHistoryActivity.this);
        rvImages.setAdapter(fuelHistoryAdapter);

        getHistoryData(_7daysBeforeDate, tomorrowDate);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FuelHistoryActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getHistoryData(String fromDate, String toDate) {
        runOnUiThread(() -> {
            rl_main.setVisibility(View.VISIBLE);
        });
        AndroidNetworking.get(BuildConfig.BASE_URL+"fuel/history")
                .addHeaders("token", BuildConfig.TOKRN)
                .addQueryParameter("vehicleId", String.valueOf(Const.vehicalID))
                .addQueryParameter("fromDate",fromDate)
                .addQueryParameter("toDate",toDate)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        FuelHistoryResponse fuelHistoryResponse = new Gson().fromJson(response.toString(), FuelHistoryResponse.class);
                        if (fuelHistoryResponse.isSuccess()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Collections.reverse(fuelHistoryResponse.getData());
                                    fuelHistoryAdapter.addAll(fuelHistoryResponse.getData());
                                    runOnUiThread(() -> {
                                        rl_main.setVisibility(View.GONE);
                                    });
                                }
                            });
                        } else {
                            Toast.makeText(FuelHistoryActivity.this, fuelHistoryResponse.getError(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}