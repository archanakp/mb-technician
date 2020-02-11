package com.battmobile.battmobiletechnician.vehicle_module.service;

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
import com.squareup.picasso.Picasso;

import java.util.List;


public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.EventViewHolder> {

    Context context;
    ServiceActivity activity;
    private List<ServiceModel> list;

    public ServiceAdapter(ServiceActivity activity, Context context, List<ServiceModel> list) {
        this.activity = activity;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_service, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        final ServiceModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.serviceID.setText(model.getService_id());
        holder.serviceDate.setText(model.getService_date());
        holder.kmCompleted.setText(model.getKm_completed() + " Km");
        holder.nextServiceKm.setText(model.getNext_service_km() + " Km");
        holder.nextServiceDate.setText(model.getNext_service_date());
        holder.receiptID.setText(model.getReceipt_id());
        Picasso.with(context).load(model.getReceipt_image()).placeholder(R.drawable.default_dp).into(holder.imgReceipt);
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.editProduct(model);
            }
        });
        holder.imgReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, FullImageActivity.class)
                        .putExtra("image", model.getReceipt_image()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView serviceID, serviceDate, kmCompleted, nextServiceKm, nextServiceDate, receiptID;
        ImageView imgEdit, imgReceipt;

        EventViewHolder(View itemView) {
            super(itemView);
            serviceID = itemView.findViewById(R.id.service_id);
            serviceDate = itemView.findViewById(R.id.service_date);
            kmCompleted = itemView.findViewById(R.id.km_completed);
            nextServiceKm = itemView.findViewById(R.id.next_service_km);
            nextServiceDate = itemView.findViewById(R.id.next_service_date);
            receiptID = itemView.findViewById(R.id.receipt_id);
            imgReceipt = itemView.findViewById(R.id.img_receipt);
            imgEdit = itemView.findViewById(R.id.img_edit);
        }
    }
}