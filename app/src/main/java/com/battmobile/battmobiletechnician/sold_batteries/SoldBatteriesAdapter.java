package com.battmobile.battmobiletechnician.sold_batteries;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.battmobile.battmobiletechnician.R;

import java.util.List;


public class SoldBatteriesAdapter extends RecyclerView.Adapter<SoldBatteriesAdapter.EventViewHolder> {

    Context context;
    private List<SoldBatteriesModel> list;

    public SoldBatteriesAdapter(Context context, List<SoldBatteriesModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_sold_batteries, parent, false));
    }

    @Override
    public void onBindViewHolder(final @NonNull EventViewHolder holder, int position) {

        final SoldBatteriesModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.brandName.setText(model.getBrand_name());
        holder.brandModel.setText(model.getBattery_model());
        holder.size.setText(model.getBattery_size());
        holder.price.setText("AED " + model.getPrice());
        holder.date.setText(model.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView brandName, brandModel, size, price, date;

        EventViewHolder(View itemView) {
            super(itemView);
            brandName = itemView.findViewById(R.id.brand_name);
            brandModel = itemView.findViewById(R.id.brand_model);
            size = itemView.findViewById(R.id.size);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);
        }
    }
}