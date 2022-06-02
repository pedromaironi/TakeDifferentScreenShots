package com.pedromaironi.AppScreenShots.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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
                openPhoto(ScreenShotUtils.getUriFromFile(new File(imageFile), getApplicationContext()));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhoto(ScreenShotUtils.getUriFromFile(new File(imageFile), getApplicationContext()));
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

    private void openPhoto(Uri photoUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final Intent takeScreenShotIntent = new Intent(Intent.ACTION_VIEW);
            takeScreenShotIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takeScreenShotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            takeScreenShotIntent.setDataAndType(photoUri, "image/*");
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

}