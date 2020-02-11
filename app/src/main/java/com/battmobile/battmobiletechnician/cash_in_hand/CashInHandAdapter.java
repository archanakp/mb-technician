package com.battmobile.battmobiletechnician.cash_in_hand;

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


public class CashInHandAdapter extends RecyclerView.Adapter<CashInHandAdapter.EventViewHolder> {

    private Context context;
    private List<CashInHandModel> list;

    CashInHandAdapter(Context context, List<CashInHandModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_wallet, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        CashInHandModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.agentName.setText(model.getAgent_name());
        holder.tranferredToAgent.setText("Transfer to : " + model.getTranferred_to_agent());
        holder.date.setText(Utils.TimeConverter.converttoDate(model.getDate()));
        holder.amount.setText("AED " + model.getAmount());
        if (model.isTransferred_cash_received()) {
            holder.status.setText("Received");
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            holder.status.setText("Pending");
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView agentName, tranferredToAgent, amount, date, status;

        EventViewHolder(View itemView) {
            super(itemView);
            agentName = itemView.findViewById(R.id.agent_name);
            tranferredToAgent = itemView.findViewById(R.id.tranferred_to_agent);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            status = itemView.findViewById(R.id.status);
        }
    }
}