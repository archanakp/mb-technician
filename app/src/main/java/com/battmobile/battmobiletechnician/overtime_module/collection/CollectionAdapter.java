package com.battmobile.battmobiletechnician.overtime_module.collection;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.Utils;

import java.util.List;


public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.EventViewHolder> {

    private Context context;
    private List<CollectionModel> list;

    CollectionAdapter(Context context, List<CollectionModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_collection, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        CollectionModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.receipt.setText("Job ID : " + model.getJob_receipt_no());
        holder.date.setText(Utils.TimeConverter.converttoDate(model.getCreated_at()));
        holder.amount.setText("+ AED " + model.getBonus_amount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView receipt, amount, date;

        EventViewHolder(View itemView) {
            super(itemView);
            receipt = itemView.findViewById(R.id.receipt);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
        }
    }
}