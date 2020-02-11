package com.battmobile.battmobiletechnician.vehicle_module.vehicle;

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


public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.EventViewHolder> {

    Context context;
    private List<VehicleModel> list;
    private static MyClickListener myClickListener;

    public VehicleAdapter(Context context, List<VehicleModel> list) {
        this.context = context;
        this.list = list;
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_vehicle, parent, false));
    }

    @Override
    public void onBindViewHolder(final @NonNull EventViewHolder holder, int position) {

        VehicleModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.vehicleNo.setText(model.getVehicle_no());
        holder.date.setText(Utils.TimeConverter.converttoDate(model.getDate()));
        holder.lastReading.setText(model.getLast_reading() + " Km");
        holder.previousReading.setText(model.getPrevious_reading() + " Km");
        holder.serviceDate.setText(model.getService_date());
        holder.insuranceDate.setText(model.getInsurance_date());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView vehicleNo, date, lastReading, previousReading, serviceDate, insuranceDate;

        EventViewHolder(View itemView) {
            super(itemView);
            vehicleNo = itemView.findViewById(R.id.vehicle_no);
            date = itemView.findViewById(R.id.date);
            lastReading = itemView.findViewById(R.id.last_reading);
            previousReading = itemView.findViewById(R.id.previous_reading);
            serviceDate = itemView.findViewById(R.id.service_date);
            insuranceDate = itemView.findViewById(R.id.insurance_date);
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