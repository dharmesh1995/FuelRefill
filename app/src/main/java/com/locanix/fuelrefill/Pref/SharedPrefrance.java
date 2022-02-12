package com.locanix.fuelrefill.Pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.locanix.fuelrefill.BuildConfig;


public class SharedPrefrance {
    public static final String MyPREFERENCES = BuildConfig.APPNAME;
    public static String FLASH = "Flash";
    public static String COMPRESSION = "Compression";

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

    public static int getCompression(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int ans = sharedpreferences.getInt(COMPRESSION, 100);
        return ans;
    }

    public static void setCompression(Context c1, int value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(COMPRESSION, value);
        editor.apply();
    }
}
