package com.pedromaironi.AppScreenShots.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pedromaironi.AppScreenShots.R;
import com.pedromaironi.AppScreenShots.utils.ScreenShotUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * @author [Pedro M. Toribio]
 * @date 1/6/22
 */

public class ShowImage extends AppCompatActivity {

    ImageView img;

    // Make sure to use the FloatingActionButton
    // for all the FABs
    FloatingActionButton mAddFab, mShare;

    // These are taken to make visible and invisible along
    // with FABs
    TextView mShareText;

    // to check whether sub FAB buttons are visible or not.
    boolean isAllFabsVisible;

    String imagePath;
    String imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);

        img = findViewById(R.id.image);

        imagePath = getIntent().getStringExtra("path");
        imageFile = getIntent().getStringExtra("file");
//        loadImage(imagePath);
        imageView.setImage(ImageSource.uri(ScreenShotUtils.getUriFromFile(new File(imageFile), this)));
        initFav();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhoto(ScreenShotUtils.getUriFromFile(new File(imageFile), getApplicationContext()),String.valueOf(ScreenShotUtils.getPathFile(new File(imageFile))));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhoto(ScreenShotUtils.getUriFromFile(new File(imageFile), getApplicationContext()),String.valueOf(ScreenShotUtils.getPathFile(new File(imageFile))));
            }
        });
    }

    private void loadImage(String imagePath) {
        Picasso.with(this).load(imagePath).into(img);
    }

    public void initFav() {
        // FAB button
        mAddFab = findViewById(R.id.add_fab);
        mAddFab.setVisibility(View.VISIBLE);

        // ChildButtons
        mShare = findViewById(R.id.btn_share);
        mShareText = findViewById(R.id.add_btn_share_text);

        mAddFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Show all buttons
                        if (!isAllFabsVisible) {
                            mShare.setVisibility(View.VISIBLE);
                            mShareText.setVisibility(View.VISIBLE);
                            isAllFabsVisible = !isAllFabsVisible;
                        } else {
                            mShare.setVisibility(View.GONE);
                            mShareText.setVisibility(View.GONE);
                            isAllFabsVisible = !isAllFabsVisible;
                        }
                    }
                });

        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ShareImage(imagePath, new File(imageFile));
                sendImageWhatsApp(imageFile);
            }
        });
    }

    private void ShareImage(String pathImage, File imageFile) {
        // Compartir imagen a whatsapp
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(String.valueOf(imageFile))));
        startActivity(Intent.createChooser(shareIntent, "Choose an app"));
    }

    private void openPhoto(Uri photoUri, String locationFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final Intent takeScreenShotIntent = new Intent(Intent.ACTION_VIEW);
            takeScreenShotIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takeScreenShotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            takeScreenShotIntent.setDataAndType(photoUri, "image/*");
            sendSMS("8292174893", "Ticket", photoUri, locationFile);
            startActivity(takeScreenShotIntent);
        }
    }

    private void sendImageWhatsApp(String imagePath) {
        /**
         * Show share dialog BOTH image and text
         */
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        //Target whatsapp:
        shareIntent.setPackage("com.whatsapp");

        //Add text and then Image URI
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is your ticket");
        shareIntent.putExtra(Intent.EXTRA_STREAM, ScreenShotUtils.getUriFromFile(new File(imageFile), this));
        Log.e("ext", String.valueOf(ScreenShotUtils.getUriFromFile(new File(imageFile), this)));
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void sendSMS(final String phoneNumber, final String message, final Uri imageUri, final String locationFile) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(
                    SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0,
                    new Intent(DELIVERED), 0);

            // ---when the SMS has been sent---
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {

                        case Activity.RESULT_OK:
                            ContentValues values = new ContentValues();
//                        String[] MobNumber = new String[2];
//                        MobNumber[0] = "9999999999";
//                        for (int i = 0; i < MobNumber.size() - 1; i++) {
//                            values.put("address", MobNumber.get(i).toString());// txtPhoneNo.getText().toString());
//                            values.put("body", MessageText.getText().toString());
//                        }
                            values.put("address", phoneNumber);
                            values.put("body", message);
                            getContentResolver().insert(
                                    Uri.parse("content://sms/sent"), values);
                            Toast.makeText(getBaseContext(), "SMS sent",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getBaseContext(), "Generic failure",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getBaseContext(), "No service",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getBaseContext(), "Null PDU",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getBaseContext(), "Radio off",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            // ---when the SMS has been delivered---
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(getBaseContext(), "SMS not delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
            sms.sendMultimediaMessage(getApplicationContext(), imageUri, locationFile, null, sentPI);
        }else{
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
        }
    }


}