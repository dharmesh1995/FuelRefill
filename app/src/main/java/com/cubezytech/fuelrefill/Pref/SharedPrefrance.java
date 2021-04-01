package com.cubezytech.fuelrefill.Pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.cubezytech.fuelrefill.BuildConfig;


public class SharedPrefrance {
    public static final String MyPREFERENCES = BuildConfig.APPNAME;
    public static String FLASH = "Flash";

    public static String getFlash(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(FLASH, "");
        return ans;
    }

    public static void setFlash(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(FLASH, value);
        editor.apply();
    }
}
