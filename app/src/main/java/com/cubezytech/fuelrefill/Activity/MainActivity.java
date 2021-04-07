package com.cubezytech.fuelrefill.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cubezytech.fuelrefill.BaseActivity;
import com.cubezytech.fuelrefill.R;
import com.cubezytech.fuelrefill.Utils.Const;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    MaterialCardView cvNewRefill, cvRefillHistory;
    TextView tvVehicalNol;
    ImageView imgBack;

    @Override
    public void permissionGranted() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Intent intent = new Intent(MainActivity.this, FewRefilleActivity.class);
                intent.putExtra("from", "newRefill");
                startActivity(intent);
                break;
            case R.id.cvRefillHistory:
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
        Intent intent = new Intent(MainActivity.this,ScanActivity.class);
        startActivity(intent);
        finish();
    }
}