package com.battmobile.battmobiletechnician.chat;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.chat.model.LoadMessageModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

class MessageLoadAdapter extends RecyclerView.Adapter<MessageLoadAdapter.MyViewHolder>{
    ChatActivity chatActivity;
    ArrayList<LoadMessageModel> loadMessageModels;
    String currentuser;



    public MessageLoadAdapter(ChatActivity chatActivity, ArrayList<LoadMessageModel> loadMessageModels, String currentuser) {
        this.chatActivity=chatActivity;
        this.loadMessageModels=loadMessageModels;
        this.currentuser=currentuser;
    }

    @NonNull
    @Override
    public MessageLoadAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType==ChatActivity.VIEW_TYPE_FRIEND_MESSAGE){
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_row, viewGroup, false);
            return new MyViewHolder(itemView);
        }else if (viewType==ChatActivity.VIEW_TYPE_USER_MESSAGE){
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_frnd_chat_row, viewGroup, false);
            return new MyViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageLoadAdapter.MyViewHolder holder, int position) {

        LoadMessageModel loadMessageModel=loadMessageModels.get(position);

        holder.msgtext.setText(loadMessageModel.getMessage());
        holder.timetext.setText(DateUtils.getRelativeTimeSpanString(loadMessageModel.getTimestamp()).toString());

    }

    @Override
    public int getItemCount() {
        if (loadMessageModels.size() > 0 && loadMessageModels != null) {
            return loadMessageModels.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {

        return loadMessageModels.get(position).getFrom().equals(ChatActivity.techid) ?ChatActivity.VIEW_TYPE_FRIEND_MESSAGE : ChatActivity.VIEW_TYPE_USER_MESSAGE;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView msgtext,timetext;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.msgBox);
            msgtext=(TextView)itemView.findViewById(R.id.messageTxt);
            timetext=(TextView)itemView.findViewById(R.id.time);
        }
    }
}
