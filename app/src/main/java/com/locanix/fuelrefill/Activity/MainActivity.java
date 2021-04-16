package com.locanix.fuelrefill.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.locanix.fuelrefill.BaseActivity;
import com.locanix.fuelrefill.R;
import com.locanix.fuelrefill.Utils.Const;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    MaterialCardView cvNewRefill, cvRefillHistory;
    TextView tvVehicalNol;
    ImageView imgBack;

    private FirebaseAnalytics mFirebaseAnalytics;

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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(MainActivity.this);

        cvNewRefill = findViewById(R.id.cvNewRefill);
        cvRefillHistory = findViewById(R.id.cvRefillHistory);
        tvVehicalNol = findViewById(R.id.tvVehicalNol);
        imgBack = findViewById(R.id.imgBack);

        cvNewRefill.setOnClickListener(this);
        cvRefillHistory.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        tvVehicalNol.setText(Const.vehicalNo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvNewRefill:
                fireAnalytics("New Fuel Refill Click", Const.vehicalNo);
                Intent intent = new Intent(MainActivity.this, FewRefilleActivity.class);
                intent.putExtra("from", "newRefill");
                startActivity(intent);
                break;
            case R.id.cvRefillHistory:
                fireAnalytics("Fuel Refill History View", Const.vehicalNo);
                Intent intent1 = new Intent(MainActivity.this, FuelHistoryActivity.class);
                intent1.putExtra("from", "refillHistory");
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
        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        startActivity(intent);
        finish();
    }
}