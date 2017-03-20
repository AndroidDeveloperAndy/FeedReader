package com.hackspace.andy.readrss.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    public static boolean isNetworkAvailable(Activity activity) {
        if (activity.getApplicationContext() == null) {
            return false;
        }
        ConnectivityManager sManagerConnect = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo sNetworkInfo = sManagerConnect.getActiveNetworkInfo();
        return sNetworkInfo != null && sNetworkInfo.isConnectedOrConnecting();
    }
}
