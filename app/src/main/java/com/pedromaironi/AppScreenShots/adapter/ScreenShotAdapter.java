package com.pedromaironi.AppScreenShots.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pedromaironi.AppScreenShots.R;
import com.pedromaironi.AppScreenShots.ui.ShowImage;

import java.io.File;
import java.util.ArrayList;

/**
 * @author [Pedro M. Toribio]
 * @date 1/6/22
 */

public class ScreenShotAdapter extends RecyclerView.Adapter<ScreenShotAdapter.ViewHolder> {
    private ArrayList<File> galleryList;
    private Context context;

    public ScreenShotAdapter(Context context, ArrayList<File> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public ScreenShotAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScreenShotAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.title.setText(galleryList.get(i).getName());
            Glide.with(context).load(galleryList.get(i).getPath()).error(R.mipmap.ic_launcher).into(viewHolder.img);
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // To detail
                    Intent intent = new Intent(context, ShowImage.class);
                    Uri uri = Uri.fromFile(new File(galleryList.get(i).getPath()));
                    intent.putExtra("path", String.valueOf(uri));
                    intent.putExtra("file", String.valueOf(galleryList.get(i)));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView img;

        public ViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.name);
            img = (ImageView) view.findViewById(R.id.image);
        }
    }
}