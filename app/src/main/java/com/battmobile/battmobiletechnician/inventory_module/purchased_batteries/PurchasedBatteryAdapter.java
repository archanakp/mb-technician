package com.battmobile.battmobiletechnician.inventory_module.purchased_batteries;

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


public class PurchasedBatteryAdapter extends RecyclerView.Adapter<PurchasedBatteryAdapter.EventViewHolder> {

    private Context context;
    private List<PurchasedBatteryModel> list;

    public PurchasedBatteryAdapter(Context context, List<PurchasedBatteryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_purchased_battery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        final PurchasedBatteryModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.brandName.setText(model.getBrand_name());
        holder.size.setText(model.getSize());
        holder.remainingQuantity.setText(model.getRemaining_quantity());
        holder.purchasedQuantity.setText(model.getPurchased_quantity());
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
        TextView brandName, size, remainingQuantity, purchasedQuantity, date;
        ImageView imgBattery;

        EventViewHolder(View itemView) {
            super(itemView);
            brandName = itemView.findViewById(R.id.brand_name);
            size = itemView.findViewById(R.id.size);
            remainingQuantity = itemView.findViewById(R.id.remaining_quantity);
            purchasedQuantity = itemView.findViewById(R.id.purchased_quantity);
            date = itemView.findViewById(R.id.date);
            imgBattery = itemView.findViewById(R.id.img_battery);
        }
    }
}