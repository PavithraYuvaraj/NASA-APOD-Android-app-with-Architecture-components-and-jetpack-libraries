package com.education.apictureofthedaynasa.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

public class NetworkUtils {
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return connectivityManager.getActiveNetwork() != null
                        && connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork()) != null;
            }
        }
        return false;
    }
}
