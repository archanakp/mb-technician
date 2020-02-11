package com.battmobile.battmobiletechnician.vehicle_module.odometer;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.Utils;

import java.util.List;


public class OdometerAdapter extends RecyclerView.Adapter<OdometerAdapter.EventViewHolder> {

    Context context;
    private List<OdometerModel> list;
    OdometerActivity activity;
    private static MyClickListener myClickListener;

    public OdometerAdapter(OdometerActivity activity, Context context, List<OdometerModel> list) {
        this.activity = activity;
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_odometer, parent, false));
    }

    @Override
    public void onBindViewHolder(final @NonNull EventViewHolder holder, int position) {

        final OdometerModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.date.setText(Utils.TimeConverter.converttoDate(model.getDate()));
        holder.currentReading.setText(model.getCurrent_reading() + " Km");
        holder.previousReading.setText(model.getPrevious_reading() + " Km");
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.editProduct(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date, currentReading, previousReading;
        ImageView imgEdit;

        EventViewHolder(View itemView) {
            super(itemView);
            currentReading = itemView.findViewById(R.id.current_reading);
            previousReading = itemView.findViewById(R.id.previous_reading);
            date = itemView.findViewById(R.id.date);
            imgEdit = itemView.findViewById(R.id.img_edit);
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