package com.locanix.fuelrefill;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.locanix.fuelrefill.Pref.SharedPrefrance;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.seekCompression)
    SeekBar seekCompression;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.tvCompressionLevel)
    TextView tvCompressionLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(SettingActivity.this);

        runOnUiThread(() -> tvCompressionLevel.setText(SharedPrefrance.getCompression(SettingActivity.this)+"%"));
        seekCompression.setMax(100);
        seekCompression.setProgress(SharedPrefrance.getCompression(SettingActivity.this));
        seekCompression.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPrefrance.setCompression(SettingActivity.this,seekCompression.getProgress());
                runOnUiThread(() -> tvCompressionLevel.setText((int) seekCompression.getProgress()+"%"));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        imgBack.setOnClickListener(v -> onBackPressed());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPrefrance.setCompression(SettingActivity.this,seekCompression.getProgress());
        runOnUiThread(() -> tvCompressionLevel.setText((int) seekCompression.getProgress()+"%"));
    }
}