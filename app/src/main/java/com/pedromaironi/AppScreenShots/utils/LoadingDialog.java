package com.pedromaironi.AppScreenShots.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.pedromaironi.AppScreenShots.R;

/**
 * @author [Pedro M. Toribio]
 * @date 1/6/22
 */

public class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity mActivity){
        activity = mActivity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progressbar, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog(){
        if(dialog == null){
            return;
        }
        dialog.dismiss();
    }
}
