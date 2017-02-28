package com.hackspace.andy.readrss.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;

import com.hackspace.andy.readrss.R;

public class NetworkUtil {

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static void messageBox(String method, String message,Activity activity)
    {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(activity);
        messageBox.setMessage(String.format("%s\n%s",activity.getString(R.string.error_method)+method,activity.getString(R.string.error)+message))
                .setNeutralButton("OK", null)
                .show();
    }
}
