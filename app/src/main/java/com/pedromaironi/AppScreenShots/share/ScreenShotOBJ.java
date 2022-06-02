package com.pedromaironi.AppScreenShots.share;

import android.net.Uri;

import java.io.File;
import java.nio.file.Path;

public class ScreenShotOBJ {

    private String image_title;
    private String image_path;

    public File getImage_file() {
        return image_file;
    }

    public void setImage_file(File image_file) {
        this.image_file = image_file;
    }

    private File image_file;

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }


    public String getImage_title() {
        return image_title;
    }

    public void setImage_title(String android_version_name) {
        this.image_title = android_version_name;
    }



}
