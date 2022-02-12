package com.locanix.fuelrefill.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.locanix.fuelrefill.BaseActivity;
import com.locanix.fuelrefill.DataBase.DataBase;
import com.locanix.fuelrefill.R;
import com.locanix.fuelrefill.Service.AlarmReceiver;
import com.locanix.fuelrefill.Utils.Key;

import java.util.Calendar;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    MaterialCardView cvNewRefill, cvRefillHistory;
    TextView tvVehicalNol;
    ImageView imgBack;
    String vehicleNumber,vehicleId;
    private FirebaseAnalytics mFirebaseAnalytics;
    DataBase db;

    @Override
    public void permissionGranted() {

    }

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
        setContentView(R.layout.activity_main);



        Calendar calendar = Calendar.getInstance();
        new AlarmReceiver().setRepeatAlarm(getApplicationContext(), 1001, calendar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(HomeActivity.this);

        cvNewRefill = findViewById(R.id.cvNewRefill);
        cvRefillHistory = findViewById(R.id.cvRefillHistory);
        tvVehicalNol = findViewById(R.id.tvVehicalNol);
        imgBack = findViewById(R.id.imgBack);

        cvNewRefill.setOnClickListener(this);
        cvRefillHistory.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        vehicleNumber = getIntent().getStringExtra(Key.VehicleNumber);
        vehicleId = getIntent().getStringExtra(Key.VehicleId);
        Log.e("vid", "onCreate: "+vehicleId);
        tvVehicalNol.setText(vehicleNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvNewRefill:

                fireAnalytics("New Fuel Refill Click", vehicleNumber);
                Intent intent = new Intent(HomeActivity.this, FuelRefilleActivity.class);
                intent.putExtra(Key.VehicleNumber, vehicleNumber);
                intent.putExtra(Key.VehicleId, vehicleId);
                startActivity(intent);

                break;
            case R.id.cvRefillHistory:

                fireAnalytics("Fuel Refill History View",vehicleNumber);
                Intent intent1 = new Intent(HomeActivity.this, FuelHistoryActivity.class);
                intent1.putExtra(Key.VehicleNumber, vehicleNumber);
                intent1.putExtra(Key.VehicleId, vehicleId);
                startActivity(intent1);

                break;
            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}