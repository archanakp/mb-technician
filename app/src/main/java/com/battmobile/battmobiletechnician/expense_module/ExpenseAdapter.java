package com.battmobile.battmobiletechnician.expense_module;

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


public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.EventViewHolder> {

    private Context context;
    private ExpenseActivity activity;
    private List<ExpenseModel> list;

    public ExpenseAdapter(ExpenseActivity activity, Context context, List<ExpenseModel> list) {
        this.activity = activity;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_expense, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        final ExpenseModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.amount.setText(model.getAmount());
        holder.date.setText(model.getDate());
        holder.purpose.setText(model.getPurpose());
        holder.description.setText(model.getDescription());
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
        TextView amount, date, description, purpose;
        ImageView imgEdit, imgReceipt;

        EventViewHolder(View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            purpose = itemView.findViewById(R.id.purpose);
            description = itemView.findViewById(R.id.description);
            imgReceipt = itemView.findViewById(R.id.img_receipt);
            imgEdit = itemView.findViewById(R.id.img_edit);
        }
    }
}