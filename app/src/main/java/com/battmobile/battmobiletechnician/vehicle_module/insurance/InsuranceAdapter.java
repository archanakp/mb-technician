package com.battmobile.battmobiletechnician.vehicle_module.insurance;

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


public class InsuranceAdapter extends RecyclerView.Adapter<InsuranceAdapter.EventViewHolder> {

    Context context;
    InsuranceActivity activity;
    private List<InsuranceModel> list;

    public InsuranceAdapter(InsuranceActivity activity, Context context, List<InsuranceModel> list) {
        this.activity = activity;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_insurance, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        final InsuranceModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.insuranceID.setText(model.getInsurance_id());
        holder.insuranceDate.setText(model.getInsurance_date());
        holder.renewDate.setText(model.getRenew_date());
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
        TextView insuranceID, insuranceDate, renewDate, receiptID;
        ImageView imgEdit, imgReceipt;

        EventViewHolder(View itemView) {
            super(itemView);
            insuranceID = itemView.findViewById(R.id.insurance_id);
            insuranceDate = itemView.findViewById(R.id.insurance_date);
            renewDate = itemView.findViewById(R.id.renew_date);
            receiptID = itemView.findViewById(R.id.receipt_id);
            imgReceipt = itemView.findViewById(R.id.img_receipt);
            imgEdit = itemView.findViewById(R.id.img_edit);
        }
    }
}