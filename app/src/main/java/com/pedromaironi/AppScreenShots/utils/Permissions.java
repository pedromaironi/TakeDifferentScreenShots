package com.pedromaironi.AppScreenShots.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
/**
 * @author [Pedro M. Toribio]
 * @date 1/6/22
 */

public class Permissions implements ActivityCompat.OnRequestPermissionsResultCallback {

    // Constants for permissions
    public final int REQUEST_PERMISSIONS_READ = 201; // Number of request
    public final int REQUEST_PERMISSIONS_WRITE = 205; // Number of request
    Context mContext;
    Activity mActivity;

    public Permissions(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
    }

    public boolean verifyPermissions() {
        return this.isReadStoragePermissionGranted() && isWriteStoragePermissionGranted();
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this.mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                Log.v("TAG", "Permission is revoked1");
                ActivityCompat.requestPermissions(this.mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_READ);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this.mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted2");
                return true;
            } else {
                Log.v("TAG", "Permission is revoked2");
                ActivityCompat.requestPermissions(this.mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_WRITE);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted2");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults.length > 0) {
            switch (requestCode) {
                case REQUEST_PERMISSIONS_READ:
                    Log.d("TAG", "External storage2");
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.v("TAG", "Permission: " + permissions[0] + "was " + grantResults[0]);
                        //resume tasks needing this permission
                        // open document
                    }
                    break;
                case REQUEST_PERMISSIONS_WRITE:
                    Log.d("TAG", "External storage1");
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.v("TAG", "Permission: " + permissions[0] + "was " + grantResults[0]);
                        //resume tasks needing this permission
                    }
                    break;
            }
        }
    }
}
