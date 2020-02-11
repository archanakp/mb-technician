package com.battmobile.battmobiletechnician.job_module;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.Utils;

import java.util.List;


public class JobAdapter extends RecyclerView.Adapter<JobAdapter.EventViewHolder> {

    Context context;
    private List<JobModel> list;
    private static MyClickListener myClickListener;

    public JobAdapter(Context context, List<JobModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_job, parent, false));
    }

    @Override
    public void onBindViewHolder(final @NonNull EventViewHolder holder, int position) {

        JobModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.tvReceipt.setText(model.getReceipt_no());
        holder.tvName.setText(model.getCustomer_name());
        holder.tvAddress.setText(model.getLocation());
        holder.tvDate.setText(Utils.TimeConverter.converttoDate(model.getDate()));
        if (model.getJob_status().equalsIgnoreCase("pending")) {
            holder.tvDate.setBackgroundResource(R.drawable.background_errow_red);
        } else if (model.getJob_status().equalsIgnoreCase("processing")) {
            holder.tvDate.setBackgroundResource(R.drawable.background_errow_color_primary);
        } else if (model.getJob_status().equalsIgnoreCase("completed")) {
            holder.tvDate.setBackgroundResource(R.drawable.background_errow_green);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvReceipt, tvName, tvAddress, tvDate;

        EventViewHolder(View itemView) {
            super(itemView);
            tvReceipt = itemView.findViewById(R.id.tv_receipt);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvDate = itemView.findViewById(R.id.tv_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                if (null != myClickListener) {
                    myClickListener.onItemClick(getLayoutPosition(), view);
                } else {
                    Toast.makeText(view.getContext(), "Click Event is Null", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                Toast.makeText(view.getContext(), "Click Event is Null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}