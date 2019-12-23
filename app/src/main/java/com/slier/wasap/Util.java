package com.slier.wasap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

public class Util {
    public static ProgressDialog showProgressDialog(Activity activity, String s) {
        ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage(s);
        pd.setCancelable(false);
        pd.show();

        return pd;
    }

    public static void showErrorDialog(Activity activity, String s) {
        new AlertDialog.Builder(activity)
                .setTitle("Opps")
                .setMessage(s)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .create()
                .show();
    }

    public static void gotoActivity(Activity context, Class<?> activity) {
        Util.gotoActivity(context, activity, false);
    }

    public static void gotoActivity(Activity context, Class<?> activity, boolean isFinish) {
        Intent intent = new Intent(context, activity);

        if (isFinish) {
            context.finish();
        }

        context.startActivity(intent);
    }
}
