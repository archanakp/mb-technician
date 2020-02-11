package com.battmobile.battmobiletechnician.overtime_module.overtime;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.utility.SessionManager;

import java.util.List;


public class OvertimeAdapter extends RecyclerView.Adapter<OvertimeAdapter.EventViewHolder> {

    Context context;
    private List<OvertimeModel> list;
    SessionManager sessionManager;

    public OvertimeAdapter(Context context, List<OvertimeModel> list) {
        this.context = context;
        this.list = list;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_overtime, parent, false));
    }

    @Override
    public void onBindViewHolder(final @NonNull EventViewHolder holder, int position) {

        OvertimeModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.receipt.setText("Job ID : " + model.getReceipt_no());
        holder.status.setText(model.getStatus());
        holder.date.setText(model.getDate());
        if (model.getStatus().equalsIgnoreCase("Pending")) {
            holder.imgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.overtime_pending));
            holder.statusText.setText("Overtime request pending");
            holder.statusText.setTextColor(context.getResources().getColor(R.color.light_orange));
            holder.status.setBackground(context.getResources().getDrawable(R.drawable.filled_round_corner_rect_orange));

        } else if (model.getStatus().equalsIgnoreCase("Approve")) {
            holder.imgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.overtime_accepted));
            holder.statusText.setText("Overtime request accepted");
            holder.statusText.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.status.setBackground(context.getResources().getDrawable(R.drawable.filled_round_corner_rect_color_accent));

        } else if (model.getStatus().equalsIgnoreCase("Rejected")) {
            holder.imgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.overtime_rejected));
            holder.statusText.setText("Overtime request rejected");
            holder.statusText.setTextColor(context.getResources().getColor(R.color.red));
            holder.status.setBackground(context.getResources().getDrawable(R.drawable.filled_round_corner_rect_red));

        }
        holder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.date);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_scrap, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(context, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView receipt, statusText, status, date;
        ImageView imgStatus;

        EventViewHolder(View itemView) {
            super(itemView);
            receipt = itemView.findViewById(R.id.receipt);
            statusText = itemView.findViewById(R.id.status_text);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
            imgStatus = itemView.findViewById(R.id.img_status);
        }
    }
}