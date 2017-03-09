package com.hackspace.andy.readrss.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.hackspace.andy.readrss.R;

import static com.hackspace.andy.readrss.util.ResourceUtils.TAB;

public final class DialogFactory {

    private static Handler handle;

    public static Dialog createGenericErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_error_title))
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    private static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(20);
        progressDialog.show();
        new Thread(() -> {
            try {
                while (progressDialog.getProgress() <= progressDialog
                        .getMax()) {
                    Thread.sleep(200);
                    handle.sendMessage(handle.obtainMessage());
                    if (progressDialog.getProgress() == progressDialog
                            .getMax()) {
                        progressDialog.dismiss();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

       handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.incrementProgressBy(1);
            }
        };
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

