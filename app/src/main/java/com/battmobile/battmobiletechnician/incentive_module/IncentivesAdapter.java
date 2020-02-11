package com.battmobile.battmobiletechnician.incentive_module;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.battmobile.battmobiletechnician.R;

import java.util.List;


public class IncentivesAdapter extends RecyclerView.Adapter<IncentivesAdapter.EventViewHolder> {

    private Context context;
    private List<IncentivesModel> list;

    public IncentivesAdapter(Context context, List<IncentivesModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_incentive, parent, false));
    }

    @Override
    public void onBindViewHolder(final @NonNull EventViewHolder holder, int position) {

        IncentivesModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.statusText.setText("You got AED " + model.getIncentive_amount() + " incentive");
        holder.date.setText(model.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView statusText, date;

        EventViewHolder(View itemView) {
            super(itemView);
            statusText = itemView.findViewById(R.id.status_text);
            date = itemView.findViewById(R.id.date);
        }
    }
}