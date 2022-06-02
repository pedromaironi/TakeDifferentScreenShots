package com.pedromaironi.AppScreenShots.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;

import com.pedromaironi.AppScreenShots.R;
import com.pedromaironi.AppScreenShots.adapter.ScreenShotAdapter;

import java.io.File;
import java.util.ArrayList;


public class ScreenShotActivity extends AppCompatActivity {

    View mActionBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot);

        showCustomToolbar(R.layout.custom_toolbar);

        final String imagePath = getIntent().getStringExtra("path");
        final String imageFile = getIntent().getStringExtra("file");

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<File> createLists = prepareData();
        ScreenShotAdapter adapter = new ScreenShotAdapter(getApplicationContext(), createLists);
        recyclerView.setAdapter(adapter);

//        LoadingDialog mLoadingDialog = new LoadingDialog(this);
//        mLoadingDialog.dismissDialog();
    }

    private ArrayList<File> prepareData(){
        ArrayList<File> theimage = new ArrayList<>();

        String path = String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        File f = new File(path);
        File[] file = f.listFiles();
        if (file != null)
        for (int i=0; i < file.length; i++)
        {
            theimage.add(i, file[i]);
        }

        return theimage;
    }

    private void showCustomToolbar(int resource) {
        Toolbar toolbar = findViewById(R.id.idToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Gallery");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActionBarView = inflater.inflate(resource, null);
        actionBar.setCustomView(mActionBarView);
    }

}
