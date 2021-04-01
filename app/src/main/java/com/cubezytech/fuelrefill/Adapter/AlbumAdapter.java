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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.cubezytech.fuelrefill.Activity.AlbumChildActivity;
import com.cubezytech.fuelrefill.Model.Directory;
import com.cubezytech.fuelrefill.Model.ImageFile;
import com.cubezytech.fuelrefill.R;


import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyClassView> {

    private static final String TAG = AlbumAdapter.class.getSimpleName();
    List<Directory<ImageFile>> files;
    Activity activity;
    String from = "";
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public AlbumAdapter(List<Directory<ImageFile>> files, Activity activity,String from) {
        this.files = files;
        this.activity = activity;
        this.from = from;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.list_album, parent, false);
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
        Directory<ImageFile> DateName = files.get(position);
        holder.tv_album.setText(DateName.getName());

        holder.tv_album_count.setText("("+DateName.getFiles().size()+")");

        ImageFile file = DateName.getFiles().get(0);

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
                    .transition(new DrawableTransitionOptions().crossFade(300))
                    .into(holder.mImage);
        } else {
            Glide.with(activity)
                    .load(file.getPath())
                    .apply(options.centerCrop()
                            .skipMemoryCache(true)
                            .priority(Priority.LOW))
                    .transition(withCrossFade())
                    .transition(new DrawableTransitionOptions().crossFade(300))
                    .into(holder.mImage);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, AlbumChildActivity.class);
            intent.putExtra("DirName",DateName.getName());
            intent.putExtra("from",from);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.fade_in, R.anim.stay);
            activity.finish();
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public void addAll(List<Directory<ImageFile>> imgMain1DownloadList) {
        this.files.addAll(imgMain1DownloadList);
        if (this.files.size() >= 10)
            notifyItemRangeChanged(this.files.size() - 10,this.files.size());
        else
            notifyDataSetChanged();
    }

    public void clearData(){
        this.files.clear();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView tv_album,tv_album_count;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.mImage);
            tv_album = itemView.findViewById(R.id.tv_album);
            tv_album_count = itemView.findViewById(R.id.tv_album_count);
        }
    }
}
