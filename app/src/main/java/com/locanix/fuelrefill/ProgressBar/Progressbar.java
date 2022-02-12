package com.locanix.fuelrefill.ProgressBar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.locanix.fuelrefill.R;


public class Progressbar extends Dialog {

    public Progressbar(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_progressbar);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
}
