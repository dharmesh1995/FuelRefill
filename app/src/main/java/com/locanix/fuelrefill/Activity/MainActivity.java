package com.locanix.fuelrefill.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.card.MaterialCardView;
import com.locanix.fuelrefill.R;
import com.locanix.fuelrefill.Service.Receiver;
import com.locanix.fuelrefill.SettingActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MaterialCardView scanQrCode;
    private ImageView imgSetting;

    private BroadcastReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        receiver = new Receiver();
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        inti();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void inti() {
        imgSetting = findViewById(R.id.imgSetting);
        imgSetting.setOnClickListener(this);
        scanQrCode = findViewById(R.id.scanQrCode);
        scanQrCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgSetting:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.scanQrCode:
                startActivity(new Intent(this,ScanActivity.class));
                break;
        }
    }
}