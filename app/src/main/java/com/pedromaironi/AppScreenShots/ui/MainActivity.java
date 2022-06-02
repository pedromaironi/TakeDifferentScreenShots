package com.pedromaironi.AppScreenShots.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pedromaironi.AppScreenShots.R;
import com.pedromaironi.AppScreenShots.utils.LoadingDialog;
import com.pedromaironi.AppScreenShots.utils.Permissions;
import com.pedromaironi.AppScreenShots.utils.ScreenShotUtils;

import java.io.File;

/**
 * @author [Pedro M. Toribio]
 * @date 1/6/22
 */

public class MainActivity extends AppCompatActivity {

    // Classes
    Permissions mPermissions;
    ScreenShotUtils mScreenShotUtils;
    LoadingDialog mLoadingDialog;

    // Variables
    ScrollView scrollView;
    LinearLayout ll_linear;

    Toolbar mToolbar;

    View mActionBarView;

    // Make sure to use the FloatingActionButton
    // for all the FABs
    FloatingActionButton mAddFab, mTakeLongScreenShot, mTakeNormalScreenShot, mGallery, mShare;

    // These are taken to make visible and invisible along
    // with FABs
    TextView addTakeLongScreenShotText, addTakeNormalScreenShot, mGalleryText, mShareText;

    // to check whether sub FAB buttons are visible or not.
    boolean isAllFabsVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Init
        mPermissions = new Permissions(this, MainActivity.this);
        mLoadingDialog = new LoadingDialog(this);
        mScreenShotUtils = new ScreenShotUtils();

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);

        showCustomToolbar(R.layout.custom_toolbar);

        SreenShotsInit();
    }

    /*
     * TakeScreenShot
     *
     * */
    public void takeScreenshot(int id) {
        Bitmap mBitmap = null;

        Intent i = null;

//        mLoadingDialog.startLoadingDialog();
        if (this.mPermissions.verifyPermissions()) {
            switch (id) {
                case R.id.btn_screenshot_long:
                    // Take bitmap
                    mBitmap = ScreenShotUtils.getBitmapByView(scrollView);

                    // Open image
                    openImage(mBitmap);

                    break;
                case R.id.btn_screenshot_activity:
                    // Take bitmap
                    mBitmap = ScreenShotUtils.shotView(scrollView);

                    // Open image
                    openImage(mBitmap);

                    break;
                case R.id.btn_activity:
                    i = new Intent(this, ScreenShotActivity.class);
                    startActivity(i);
                    break;
                default:
                    break;
            }
        }

    }

    /*
    *
    * openImage
    *
    * */
    private void openImage(Bitmap mBitmap){
        File pictureFile = null;
        File picturePath = null;

        // Save picture
        pictureFile = ScreenShotUtils.savePicture(this, mBitmap);

        // Take path
        picturePath = ScreenShotUtils.getPathFile(pictureFile);

        Intent intent = new Intent(this, ShowImage.class);
        Uri uri = Uri.fromFile(new File(String.valueOf(picturePath)));
        intent.putExtra("path", String.valueOf(uri));
        intent.putExtra("file", String.valueOf(pictureFile));
        startActivity(intent);
    }


    /*
    *
    * showCustomToolbar
    *
    * */
    private void showCustomToolbar(int resource) {
        Toolbar toolbar = findViewById(R.id.idToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Screenshots");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActionBarView = inflater.inflate(resource, null);
        actionBar.setCustomView(mActionBarView);
    }

    /*
    *
    * ScreenShotsInit
    *
    * */
    public void SreenShotsInit() {
        // FAB button
        mAddFab = findViewById(R.id.add_fab);

        // FAB button's
        //Long ScreenShot
        mTakeLongScreenShot = findViewById(R.id.btn_screenshot_long);
        addTakeLongScreenShotText = findViewById(R.id.add_btn_screenshot_long_text);

        //Short ScreenShot
        mTakeNormalScreenShot = findViewById(R.id.btn_screenshot_activity);
        addTakeNormalScreenShot = findViewById(R.id.add_btn_screenshot_activity_text);

        //Activity
        mGallery = findViewById(R.id.btn_activity);
        mGalleryText = findViewById(R.id.add_btn_activity_text);

        mAddFab.setVisibility(View.VISIBLE);

        mAddFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Show all buttons
                        //((isAllFabsVisible) ? showAllButtons() : hideAllButtons());
                        if (!isAllFabsVisible) {
                            showAllButtons();
                        } else {
                            hideAllButtons();
                        }
                    }
                });

        mTakeLongScreenShot.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideAllButtons();
                        takeScreenshot(mTakeLongScreenShot.getId());
                    }
                });

        mTakeNormalScreenShot.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideAllButtons();
                        takeScreenshot(mTakeNormalScreenShot.getId());
                    }
                });

        mGallery.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideAllButtons();
                        takeScreenshot(mGallery.getId());
                    }
                });


    }

    /*
     * isAllFabsVisible
     *
     * @return boolean
     * */
    public void showAllButtons(){
        mTakeLongScreenShot.show();
        addTakeLongScreenShotText.setVisibility(View.VISIBLE);

        mTakeNormalScreenShot.show();
        addTakeNormalScreenShot.setVisibility(View.VISIBLE);

        mGallery.show();
        mGalleryText.setVisibility(View.VISIBLE);

        this.isAllFabsVisible = !this.isAllFabsVisible;
    }

    public void hideAllButtons(){
        mTakeLongScreenShot.hide();
        addTakeLongScreenShotText.setVisibility(View.GONE);

        mTakeNormalScreenShot.hide();
        addTakeNormalScreenShot.setVisibility(View.GONE);

        mGallery.hide();
        mGalleryText.setVisibility(View.GONE);
        this.isAllFabsVisible = !this.isAllFabsVisible;
    }

}