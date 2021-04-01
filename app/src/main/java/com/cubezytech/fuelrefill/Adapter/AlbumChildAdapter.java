package com.cubezytech.fuelrefill.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.cubezytech.fuelrefill.Activity.FewRefilleActivity;
import com.cubezytech.fuelrefill.Model.ImageFile;
import com.cubezytech.fuelrefill.R;
import com.cubezytech.fuelrefill.Utils.Const;


import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AlbumChildAdapter extends RecyclerView.Adapter<AlbumChildAdapter.MyClassView> {

    List<ImageFile> images;
    Activity activity;
    int direPos = 0;
    String from = "";

    public AlbumChildAdapter(List<ImageFile> images, Activity activity, int i, String from) {
        this.images = images;
        this.activity = activity;
        this.direPos = i;
        this.from = from;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.image_view, parent, false);
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        if (params != null) {
            WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            params.height = width / 3;
        }
        return new MyClassView(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        ImageFile file = images.get(position);

        holder.mImage.setClipToOutline(true);


        RequestOptions options = new RequestOptions();
        if (file.getPath().endsWith(".PNG") || file.getPath().endsWith(".png")) {
            Glide.with(activity)
                    .load(file.getPath())
                    .apply(options.centerCrop()
                            .skipMemoryCache(true)
                            .priority(Priority.LOW)
                            .format(DecodeFormat.PREFER_ARGB_8888))
                    .transition(withCrossFade())
                    .transition(new DrawableTransitionOptions().crossFade(500))
                    .into(holder.mImage);
        } else {
            Glide.with(activity)
                    .load(file.getPath())
                    .apply(options.centerCrop()
                            .skipMemoryCache(true)
                            .priority(Priority.LOW))
                    .transition(withCrossFade())
                    .transition(new DrawableTransitionOptions().crossFade(500))
                    .into(holder.mImage);
        }

        holder.itemView.setOnClickListener(v -> {

            String filePath = file.getPath();
            switch (from) {
                case "carDriver":
                    Const.carDriver = filePath;
                    break;
                case "beforeRefill":
                    Const.beforeRefill = filePath;
                    break;
                case "afterRefill":
                    Const.afterRefill = filePath;
                    break;
            }

            Intent intent = new Intent(activity, FewRefilleActivity.class);
            intent.putExtra("path", filePath);
            activity.startActivity(intent);
            activity.finish();
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        ImageView mImage;

        public MyClassView(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.mImage);
        }
    }
}
