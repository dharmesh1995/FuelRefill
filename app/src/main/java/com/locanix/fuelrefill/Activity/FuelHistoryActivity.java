package com.locanix.fuelrefill.Activity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.locanix.fuelrefill.Adapter.FuelHistoryAdapter;
import com.locanix.fuelrefill.Adapter.FuelPendingData;
import com.locanix.fuelrefill.BaseActivity;
import com.locanix.fuelrefill.BuildConfig;
import com.locanix.fuelrefill.DataBase.DBHelper;
import com.locanix.fuelrefill.Model.EntryFuel.FuelRefill;
import com.locanix.fuelrefill.Model.FuelHistory.DataItem;
import com.locanix.fuelrefill.Model.FuelHistory.FuelHistoryResponse;
import com.locanix.fuelrefill.ProgressBar.Progressbar;
import com.locanix.fuelrefill.R;
import com.locanix.fuelrefill.Utils.Const;
import com.locanix.fuelrefill.Utils.Key;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class FuelHistoryActivity extends BaseActivity {

    ImageView imgBack;
    RecyclerView rvImages;
    TextView tvVehicalNol;

    FuelHistoryAdapter fuelHistoryAdapter;
    FuelPendingData fuelPendingData;
    ArrayList<DataItem> dataItems = new ArrayList<>();
    ArrayList<FuelRefill> fuelRefills = new ArrayList<>();

    DBHelper dbHelper;
    private Progressbar progressbar;

    @Override
    public void permissionGranted() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_history);

        dbHelper = new DBHelper(FuelHistoryActivity.this);
        AndroidNetworking.initialize(FuelHistoryActivity.this);

        progressbar = new Progressbar(FuelHistoryActivity.this);

        imgBack = findViewById(R.id.imgBack);
        tvVehicalNol = findViewById(R.id.tvVehicalNol);
        imgBack.setOnClickListener(v -> onBackPressed());

        tvVehicalNol.setText(getIntent().getStringExtra(Key.VehicleNumber));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        String tomorrowDate = DateFormat.format("dd/MM/yyyy", tomorrow).toString();

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(tomorrow);
        calendar1.add(Calendar.DAY_OF_YEAR, -7);
        Date newDate = calendar1.getTime();

        String _7daysBeforeDate = DateFormat.format("dd/MM/yyyy", newDate).toString();

        rvImages = findViewById(R.id.rvImages);
        rvImages.setLayoutManager(new LinearLayoutManager(FuelHistoryActivity.this, RecyclerView.VERTICAL, false));
        fuelHistoryAdapter = new FuelHistoryAdapter(dataItems, FuelHistoryActivity.this);

        if (!Const.isInternetConnected(FuelHistoryActivity.this)) {

            fuelRefills.clear();
            ArrayList<FuelRefill> fuelRefills1;
            fuelRefills1 = dbHelper.getPillsNoRecords();
            for (int i = 0; i < fuelRefills1.size(); i++) {
                FuelRefill fuelRefill = fuelRefills1.get(i);
                if (fuelRefill.getFDriver() != null && !fuelRefill.getFDriver().equals("")) {
                    fuelRefills.add(fuelRefill);
                }
            }
            fuelPendingData = new FuelPendingData(fuelRefills, FuelHistoryActivity.this);
            rvImages.setAdapter(fuelPendingData);
        } else {
            rvImages.setAdapter(fuelHistoryAdapter);
            getHistoryData(_7daysBeforeDate, tomorrowDate);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getHistoryData(String fromDate, String toDate) {
        progressbar.show();
        AndroidNetworking.get(BuildConfig.BASE_URL + "fuel/history")
                .addHeaders("token", BuildConfig.TOKRN)
                .addQueryParameter("vehicleId", getIntent().getStringExtra(Key.VehicleId))
                .addQueryParameter("fromDate", fromDate)
                .addQueryParameter("toDate", toDate)
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
                                    if (progressbar.isShowing()){
                                        progressbar.dismiss();
                                    }
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