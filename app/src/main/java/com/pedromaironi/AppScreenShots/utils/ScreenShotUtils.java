package com.pedromaironi.AppScreenShots.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.util.Date;

import static java.lang.System.out;

/**
 * @author [Pedro M. Toribio]
 * @date 1/6/22
 */
public class ScreenShotUtils {

    private static final String FILE_DIR = "LongScreenShot";

    /*
     *
     * ShotActivity
     * */
    public static Bitmap shotActivity(Activity activity) {

        // [View] : For take a screenshot
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap b1 = view.getDrawingCache();

        // [Bar] : Get status bar height
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        // [Screen] : Get screen height and length
        DisplayMetrics displayMetrics = new DisplayMetrics();
        // @Deprecated getDefaultDisplay() at api level 30(Android 11)
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // [Title bar] Remove
        Bitmap bitmap = Bitmap.createBitmap(
                b1, 0, statusBarHeight, width, height - statusBarHeight
        );
        view.destroyDrawingCache();

        // [Picture] Save picture
        //savePicture(activity, bitmap);
        return bitmap;
    }

    /**
     * http://stackoverflow.com/questions/9791714/take-a-screenshot-of-a-whole-view
     */
    public static Bitmap shotView(View v) {
        if (v == null) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 15) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(),
                    (int) v.getY(),
                    (int) v.getX() + v.getMeasuredWidth(),
                    (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0,
                v.getMeasuredWidth(), v.getMeasuredHeight());

        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();

        return b;
    }

    public static Bitmap shotViewWithoutBottom(View v) {
        if (v == null) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 15) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(), (int) v.getY(),
                    (int) v.getX() + v.getMeasuredWidth(),
                    (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }

        // Difference getPaddingBottom
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0,
                v.getMeasuredWidth(), v.getMeasuredHeight() - v.getPaddingBottom());

        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();

        return b;
    }

    /**
     * shotViewInScreen
     *
     * @param view
     * @return bitmap
     */
    public static Bitmap shotViewInScreen(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        //savePicture(view.getContext(), bitmap);
        return bitmap;
    }


    /**
     * getBitmapByView
     * @param scrollView
     * @return bitmap
     */
    public static Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;

        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#FFFFFF"));
        }

        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);

        Bitmap newBitmap = BITMAP_RESIZER(bitmap, scrollView.getWidth(), h);
        final Canvas canvas = new Canvas(newBitmap);
        scrollView.draw(canvas);
        return newBitmap;
    }

    /*
    *
    * BITMAP_RESIZER
    *
    * */
    public static Bitmap BITMAP_RESIZER(Bitmap bitmap,int newWidth,int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.RGB_565);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }

    /**
     * savePicture
     *
     * @param context
     * @param bitmap
     */
    public static File savePicture(final Context context, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

//        final File[] imagePath = {null};
        File photoFile = null;

        try {
            photoFile = createPhotoFile(context);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if (photoFile != null) {
            compressData(bitmap, getPathFile(photoFile));
        }

        return photoFile;
    }

    /**
     * getUriFromFile
     *
     * @param file
     * @param mContext
     *
     * @return Uri
     */
    public static File getPathFile(File file){
        return new File(file.getAbsolutePath());
    }

    /**
     * getUriFromFile
     *
     * @param file
     * @param mContext
     *
     * @return Uri
     */
    public static Uri getUriFromFile(File file, Context mContext){
        return FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".FileProvider", file);
    }

    /**
     * compressData
     *
     * @param bitmap
     * @param imagePath
     */
    //Create screenshot
    private static void compressData(Bitmap bitmap, File imagePath) {
        try {
            FileOutputStream fos;
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                out.close();
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }

//        File newFile = new CompressHelper.Builder(context.getApplicationContext())
//                // The default maximum width is 720
//                .setMaxWidth(10000)
//                // The default maximum height is 960
//                .setMaxHeight(50000)
//                // Default compression quality is 80
//                .setQuality(80)
//                // Set the file name you need to modify
//                .setFileName(fileName)
//                // Set the default compression to jpEg format
//                .setCompressFormat(Bitmap.CompressFormat.JPEG)
////                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
////                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                .setDestinationDirectoryPath(dir.getAbsolutePath())
//                .build()
//                .compressToFile(mFile);
//
//        Uri uri = Uri.fromFile(newFile);
//        Intent mIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
//        context.sendBroadcast(mIntent);
//
//        return newFile;
    }

    /**
     * createPhotoFile
     *
     * @return File
     */
    public static File createPhotoFile(Context mContext) throws IOException {
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timestamp = "/" + DateFormat.getDateTimeInstance().format(new Date());
        return new File(storageDir + timestamp + ".jpg");
    }

    /**
     * MD5
     *
     * @param info
     */
    private static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                md5.update(info.getBytes(StandardCharsets.UTF_8));
            } else {
                md5.update(info.getBytes("UTF-8"));
            }
            byte[] encryption = md5.digest();

            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuilder.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuilder.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuilder.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * loadBitmapFromView
     *
     * @param v
     * @param width
     * @param height
     */
    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    /*
     * @ Activity
     * */
    public static int getScreenWidth(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
    }



}
