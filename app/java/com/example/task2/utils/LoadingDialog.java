package com.example.task2.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog {
    private ProgressDialog progressDialog;
    private Context context;

    public LoadingDialog(Context context) {
        this.context = context;
        initializeDialog();
    }

    private void initializeDialog() {
        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(String message) {
        try {
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.setMessage(message != null ? message : "Loading...");
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShowing() {
        return progressDialog != null && progressDialog.isShowing();
    }
}