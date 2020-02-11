package com.battmobile.battmobiletechnician.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.chat.model.ChatMessageModel;
import com.battmobile.battmobiletechnician.chat.model.ChatMessageModelFetch;
import com.battmobile.battmobiletechnician.chat.model.ChatUserLoadModel;
import com.battmobile.battmobiletechnician.home.HomeActivity;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.UUID;

class UserProfileChatLoadAdapter extends RecyclerView.Adapter<UserProfileChatLoadAdapter.MyViewHolder>{

    MessageUserList messageUserListClass;
    List<ChatUserLoadModel> chatMessageModelFetches;
    SessionManager sessionManager;
    ChatUserLoadModel chatMessageModelFetch;
    String  uniqid;
    static boolean visible=true;

    public UserProfileChatLoadAdapter(MessageUserList messageUserListClass, List<ChatUserLoadModel> chatMessageModelFetches) {
        this.messageUserListClass=messageUserListClass;
        this.chatMessageModelFetches=chatMessageModelFetches;
        sessionManager=new SessionManager(messageUserListClass);
    }

    @NonNull
    @Override
    public UserProfileChatLoadAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chatlist, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProfileChatLoadAdapter.MyViewHolder holder, final int position) {
         chatMessageModelFetch=chatMessageModelFetches.get(position);
      holder.username.setText(chatMessageModelFetch.getTo());
      holder.lastmsg.setText(chatMessageModelFetch.getLastMessage());
        holder.date.setText(DateUtils.getRelativeTimeSpanString(chatMessageModelFetch.getTimestamp()).toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(messageUserListClass);
                progressDialog.show();

                uniqid=sessionManager.getUSER_NAME()+"_"+chatMessageModelFetches.get(position).getTo();
                uniqid= Base64.encodeToString(uniqid.getBytes(), Base64.NO_WRAP);




                HomeActivity.secondaryDatabase.getReference("ChatMaster").child(uniqid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        launchMode(uniqid,chatMessageModelFetches.get(position).getTo());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    private void launchMode(String uniqid, final String usernm) {


        HomeActivity.secondaryDatabase.getReference("ChatMaster").child(uniqid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (visible){
                    visible=false;

                    ChatMessageModelFetch chatMessageModel=dataSnapshot.getValue(ChatMessageModelFetch.class);
                    Intent intent=new Intent(messageUserListClass,ChatActivity.class);
                    intent.putExtra("username",usernm);
                    intent.putExtra("msgmodel",  chatMessageModel);
                    messageUserListClass.startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (chatMessageModelFetches.size() > 0 && chatMessageModelFetches != null) {
            return chatMessageModelFetches.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date,username,lastmsg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date=(TextView)itemView.findViewById(R.id.date);
            username=(TextView)itemView.findViewById(R.id.userName);
            lastmsg=(TextView)itemView.findViewById(R.id.lastMessageTxt);
        }
    }
}
