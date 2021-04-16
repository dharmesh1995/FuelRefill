package com.locanix.fuelrefill.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.locanix.fuelrefill.BuildConfig;

import java.io.File;
import java.util.Objects;

public class Const {

    public final static String IMAGE_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/" + BuildConfig.APPNAME;
    public static String vehicalNo = "";
    public static int vehicalID = 0;
    public static String carDriver = "";
    public static String beforeRefill = "";
    public static String afterRefill = "";

    public static String currentTimeStamp = "";
    public static String currentTimeStamp1 = "";

    public static void clearData() {
        carDriver = "";
        beforeRefill = "";
        afterRefill = "";
        currentTimeStamp = "";
    }

    public static String extractPathWithoutSeparator(String url) {
        return url.substring(0, url.lastIndexOf("/"));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean delete(final Context context, final File file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[]{
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");
        contentResolver.delete(filesUri, where, selectionArgs);

        if (file.exists()) {
            contentResolver.delete(filesUri, where, selectionArgs);
        }
        return !file.exists();
    }

    public static boolean isInternetConnected(Context mContext) {
        try {
            ConnectivityManager connect = (ConnectivityManager) mContext.getSystemService("connectivity");
            if (connect != null) {
                NetworkInfo resultMobile = connect.getNetworkInfo(0);
                NetworkInfo resultWifi = connect.getNetworkInfo(1);
                return (resultMobile != null && resultMobile.isConnectedOrConnecting()) || (resultWifi != null && resultWifi.isConnectedOrConnecting());
            }
        } catch (Exception e) {
            Log.e("LLLL_Debug: ", Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }
}
