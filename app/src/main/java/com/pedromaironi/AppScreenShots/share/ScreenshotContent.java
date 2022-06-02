package com.pedromaironi.AppScreenShots.share;

import android.net.Uri;
import android.os.Environment;

import com.pedromaironi.AppScreenShots.ui.MainActivity;
import com.pedromaironi.AppScreenShots.ui.ScreenShotActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * Helper class for providing sample content for user interfaces
 */
public class ScreenshotContent {
//    static final List<ScreenshotsOBJ> ITEMS = new ArrayList<>();
//
//    public static void loadSavedImages(File dir, ScreenShotActivity mActivity) {
//        ITEMS.clear();
//        if (dir.exists()) {
//            File[] files = dir.listFiles();
//            for (File file : files) {
//                // getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//                String absolutePath = String.valueOf(mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
//                String extension = absolutePath.substring(absolutePath.lastIndexOf("."));
//                if (extension.equals(".jpg")) {
//                    loadImage(file);
//                }
//            }
//        }
//    }
//
//
//    public static void deleteSavedImages(File dir) {
//        if (dir.exists()) {
//            File[] files = dir.listFiles();
//            for (File file : files) {
//                String absolutePath = file.getAbsolutePath();
//                String extension = absolutePath.substring(absolutePath.lastIndexOf("."));
//                if (extension.equals(".jpg")) {
//                    file.delete();
//                }
//            }
//        }
//        ITEMS.clear();
//    }
//
//    private static String getDateFromUri(Uri uri){
//        String[] split = uri.getPath().split("/");
//        String fileName = split[split.length - 1];
//        String fileNameNoExt = fileName.split("\\.")[0];
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dateString = format.format(new Date(Long.parseLong(fileNameNoExt)));
//        return dateString;
//    }
//
//    public static void loadImage(File file) {
//        ScreenshotsOBJ newItem = new ScreenshotsOBJ();
//        newItem.Image = Uri.fromFile(file);
//        newItem.date = getDateFromUri(newItem.Image);
//        addItem(newItem);
//    }
//
//    private static void addItem(ScreenshotsOBJ item) {
//        ITEMS.add(0, item);
//    }
//
//    public static List<ScreenshotsOBJ> getImages(){
//        return ITEMS;
//    }



}
