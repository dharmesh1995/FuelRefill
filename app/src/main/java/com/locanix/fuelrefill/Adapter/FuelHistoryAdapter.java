package com.locanix.fuelrefill.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.locanix.fuelrefill.Model.FuelHistory.DataItem;
import com.locanix.fuelrefill.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FuelHistoryAdapter extends RecyclerView.Adapter<FuelHistoryAdapter.MyClassView> {

    ArrayList<DataItem> dataItems;
    Activity mActivity;

    public FuelHistoryAdapter(ArrayList<DataItem> dataItems, Activity mActivity) {
        this.dataItems = dataItems;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_fuel_refill_history, parent, false);
        return new MyClassView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        DataItem dataItem = dataItems.get(position);

        try {
            String dateString = dataItem.getRefilledOnIST();

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            Date date = inputFormat.parse(dateString);
            String formattedDate = outputFormat.format(date);
            System.out.println(formattedDate); // prints 10-04-2018

//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
//            Date date = sdf.parse(dateString);
//
//            long startDate = date.getTime();
//            String dateString1 = DateFormat.format("dd-MMM-yyyy hh:mm a", startDate).toString();
            holder.tvDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tvVolume.setText(String.valueOf(dataItem.getVolume()));
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public void addAll(List<DataItem> dataItems) {
        this.dataItems.clear();
        this.dataItems.addAll(dataItems);
        notifyDataSetChanged();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        TextView tvDate, tvVolume;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvVolume = itemView.findViewById(R.id.tvVolume);
        }
    }
}
