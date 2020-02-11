package com.battmobile.battmobiletechnician.referral;

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

import java.util.List;


public class ReferralAdapter extends RecyclerView.Adapter<ReferralAdapter.EventViewHolder> {

    Context context;
    private List<ReferralModel> list;
    ReferralActivity activity;
    private static MyClickListener myClickListener;

    public ReferralAdapter(ReferralActivity activity, Context context, List<ReferralModel> list) {
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
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_referral, parent, false));
    }

    @Override
    public void onBindViewHolder(final @NonNull EventViewHolder holder, int position) {

        final ReferralModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.date.setText(model.getDate());
        holder.source.setText(model.getSource());
        holder.name.setText(model.getName());
        holder.number.setText(model.getNumber());
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
        TextView source, name, number, date;
        ImageView imgEdit;

        EventViewHolder(View itemView) {
            super(itemView);
            source = itemView.findViewById(R.id.source);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
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