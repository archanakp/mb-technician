package com.battmobile.battmobiletechnician.inventory_module.scrap_batteries;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.common.FullImageActivity;
import com.battmobile.battmobiletechnician.utility.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ScrapBatteryAdapter extends RecyclerView.Adapter<ScrapBatteryAdapter.EventViewHolder> {

    private Context context;
    private List<ScrapBatteryModel> list;

    public ScrapBatteryAdapter(Context context, List<ScrapBatteryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_scrap_battery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        final ScrapBatteryModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.brandName.setText(model.getBrand_name());
        holder.batteryModel.setText(model.getBattery_model());
        holder.size.setText(model.getSize());
        holder.quantity.setText(model.getQuantity());
        holder.date.setText(Utils.TimeConverter.converttoDate(model.getDate()));
        Picasso.with(context).load(model.getBattery_image()).placeholder(R.drawable.default_dp).into(holder.imgBattery);
        holder.imgBattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, FullImageActivity.class)
                        .putExtra("image", model.getBattery_image()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView brandName, batteryModel, size, quantity, date;
        ImageView imgBattery;

        EventViewHolder(View itemView) {
            super(itemView);
            brandName = itemView.findViewById(R.id.brand_name);
            size = itemView.findViewById(R.id.size);
            batteryModel = itemView.findViewById(R.id.battery_model);
            quantity = itemView.findViewById(R.id.quantity);
            date = itemView.findViewById(R.id.date);
            imgBattery = itemView.findViewById(R.id.img_battery);
        }
    }
}