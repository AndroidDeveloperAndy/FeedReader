package com.hackspace.andy.readrss.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.hackspace.andy.readrss.R;

import static com.hackspace.andy.readrss.util.StringsUtils.TAB;

public final class DialogFactory {

    private static Handler h;

    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createSimpleOkErrorDialog(Context context,
                                                   @StringRes int titleResource,
                                                   @StringRes int messageResource) {

        return createSimpleOkErrorDialog(context,
                context.getString(titleResource),
                context.getString(messageResource));
    }

    public static Dialog createGenericErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_error_title))
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createGenericErrorDialog(Context context, @StringRes int messageResource) {
        return createGenericErrorDialog(context, context.getString(messageResource));
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(20);
        progressDialog.setIndeterminate(true);
        h = new Handler() {
            public void handle() {
                progressDialog.setIndeterminate(false);
                if (progressDialog.getProgress() < progressDialog.getMax()) {
                    progressDialog.incrementProgressBy(50);
                    progressDialog.incrementSecondaryProgressBy(75);
                    h.sendEmptyMessageDelayed(0, 100);
                } else {
                    progressDialog.dismiss();
                }
            }
        };
        h.sendEmptyMessageDelayed(0, 250);
        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context,
                                                      @StringRes int messageResource) {
        return createProgressDialog(context, context.getString(messageResource));
    }

    public static void messageBox(String method, String message,Activity activity)
    {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(activity);
        messageBox.setMessage(String.format(TAB,activity.getString(R.string.error_method)+method,activity.getString(R.string.error)+message))
                .setNeutralButton(R.string.dialog_action_ok, null)
                .show();
    }
}

