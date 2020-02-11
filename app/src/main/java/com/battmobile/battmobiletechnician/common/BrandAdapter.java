package com.battmobile.battmobiletechnician.common;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.battmobile.battmobiletechnician.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {

    private List<BrandModel> orignalList;
    ArrayList<BrandModel> filterList;
    private Context context;
    private static MyClickListener myClickListener;
    String type;

    public BrandAdapter(Context contextl, List<BrandModel> orignalList, String type) {
        this.context = contextl;
        this.orignalList = orignalList;
        this.filterList = new ArrayList<>();
        filterList.addAll(orignalList);
        this.type = type;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_brand, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       holder.textViewModel.setText(filterList.get(position).getTitle());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewModel;

        private ViewHolder(View itemView) {
            super(itemView);
            textViewModel = itemView.findViewById(R.id.tv_name);
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
        public void onItemClick(int position, View v);
    }

    public ArrayList<BrandModel> filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filterList.clear();
        if (charText.length() == 0) {
            filterList.addAll(orignalList);
        } else {
            for (BrandModel sessionListModel : orignalList) {
                if (sessionListModel.getTitle().toLowerCase().contains(charText)) {
                    filterList.add(sessionListModel);
                }
            }
        }
        notifyDataSetChanged();
        return filterList;
    }
}
