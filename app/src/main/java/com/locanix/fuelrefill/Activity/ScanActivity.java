package com.locanix.fuelrefill.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.zxing.Result;
import com.locanix.fuelrefill.BaseActivity;
import com.locanix.fuelrefill.BuildConfig;
import com.locanix.fuelrefill.Model.ScanModel;
import com.locanix.fuelrefill.ProgressBar.Progressbar;
import com.locanix.fuelrefill.R;
import com.locanix.fuelrefill.Retrofit.APIClient;
import com.locanix.fuelrefill.Retrofit.APIInterface;
import com.locanix.fuelrefill.SendMail;
import com.locanix.fuelrefill.Utils.Const;
import com.locanix.fuelrefill.Utils.Key;

import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends BaseActivity {

    CodeScannerView scanner_view;
    String from = "";
    private CodeScanner mCodeScanner;
    //private FirebaseAnalytics mFirebaseAnalytics;
    private Progressbar progressbar;
    private boolean exist = false;


    private void fireAnalytics(String arg1, String arg2) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, arg1);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, arg2);
        //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        //mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
    }

    @Override
    public void permissionGranted() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(ScanActivity.this);

        progressbar = new Progressbar(this);
        AndroidNetworking.initialize(ScanActivity.this);
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(ScanActivity.this);

        from = getIntent().getStringExtra("from");

        scanner_view = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(ScanActivity.this, scanner_view);
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                fireAnalytics("Scan QR Code", result.toString());
                Log.e("Test","Resu "+result.toString());

                if (Const.isInternetConnected(ScanActivity.this)) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressbar.show();
                        }
                    });

                    getVehicleList(result.toString());

                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            Toast.makeText(getApplicationContext(),"No Internet Connection, please wait while you're in a network state",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
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

        Call<ScanModel> call1 = APIClient.getClient().create(APIInterface.class).getVehicleResponse(BuildConfig.TOKRN,"application/json");

        call1.enqueue(new Callback<ScanModel>() {
            @Override
            public void onResponse(@NonNull Call<ScanModel> call, @NonNull Response<ScanModel> response) {

                List<ScanModel.DataItem> list = response.body().data;

                for (int i=0;i<list.size();i++){

                    if (list.get(i).description != null) {

                        if (list.get(i).description.equals(scanCode)) {

                            exist = true;
                            Intent intent = new Intent(ScanActivity.this, HomeActivity.class);
                            intent.putExtra(Key.VehicleNumber,list.get(i).vehiclenumber);
                            intent.putExtra(Key.VehicleId,String.valueOf(list.get(i).vehicleid));
                            startActivity(intent);
                            finish();

                        }else {

                           /* Log.e("server_response", " onResponse: "+list.get(0).description);*/
                        }
                    }
                }

                if (!exist) {
                    Toast.makeText(getApplicationContext(),"QR Code not valid.",Toast.LENGTH_LONG).show();
                    SendMail sm = new SendMail(ScanActivity.this, "vibeplen@gmail.com", "QR Code not valid",scanCode);
                    sm.execute();
                    finish();
                }

                if (progressbar.isShowing()){
                    progressbar.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ScanModel> call, @NonNull Throwable t) {
                call.cancel();


            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}