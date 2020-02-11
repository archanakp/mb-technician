package com.battmobile.battmobiletechnician.chat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.chat.model.ChatMessageModel;
import com.battmobile.battmobiletechnician.chat.model.ChatMessageModelFetch;
import com.battmobile.battmobiletechnician.chat.model.DataItem;
import com.battmobile.battmobiletechnician.home.HomeActivity;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

class MessageUserListAdapter extends RecyclerView.Adapter<MessageUserListAdapter.MyViewHolder> {

    MessageUserList messageUserList;
    List<DataItem> newchtuserlist;
    SessionManager sessionManager;
    String  uniqid,chatid;
   static boolean visible=true;
    Dialog dialog;

    public MessageUserListAdapter(MessageUserList messageUserList, List<DataItem> newchtuserlist, Dialog dialog) {
        this.messageUserList=messageUserList;
        this.newchtuserlist=newchtuserlist;
        sessionManager=new SessionManager(messageUserList);
        this.dialog=dialog;
    }

    @NonNull
    @Override
    public MessageUserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_type, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageUserListAdapter.MyViewHolder holder, int position) {
       final DataItem dataItem=newchtuserlist.get(position);
       holder.username.setText(dataItem.getUsername());
       holder.email.setText(dataItem.getEmail());

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               final ProgressDialog progressDialog=new ProgressDialog(messageUserList);
               progressDialog.show();

               uniqid=sessionManager.getUSER_NAME()+"_"+dataItem.getUsername();
               uniqid= Base64.encodeToString(uniqid.getBytes(), Base64.NO_WRAP);

               chatid= UUID.randomUUID().toString();
               chatid="cuid_"+chatid;

           HomeActivity.secondaryDatabase.getReference("ChatMaster").child(uniqid).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          if (dataSnapshot.getValue()==null){
              HomeActivity.secondaryDatabase.getReference("UserChats").child(sessionManager.getUSER_NAME()).child(uniqid).setValue("");
              HomeActivity.secondaryDatabase.getReference("UserChats").child(dataItem.getUsername()).child(uniqid).setValue("");

              HomeActivity.secondaryDatabase.getReference("ChatMaster").child(uniqid).setValue("");


              ChatMessageModel chatMessageModel=new ChatMessageModel("Empty Message",dataItem.getUsername(),sessionManager.getUSER_NAME(), ServerValue.TIMESTAMP,dataItem.getUsername(),sessionManager.getUSER_NAME(),"",chatid);
              HomeActivity.secondaryDatabase.getReference("ChatMaster").child(uniqid).setValue(chatMessageModel, new DatabaseReference.CompletionListener() {
                  @Override
                  public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                      HomeActivity.secondaryDatabase.getReference("MessageMaster").child(chatid).setValue("");

                      launchMode(uniqid,dataItem.getUsername());
                      progressDialog.dismiss();

                  }
              });


          }else {
              launchMode(uniqid,dataItem.getUsername());
              progressDialog.dismiss();
          }
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
                   dialog.dismiss();
                   ChatMessageModelFetch chatMessageModel=dataSnapshot.getValue(ChatMessageModelFetch.class);
                   Intent intent=new Intent(messageUserList,ChatActivity.class);
                   intent.putExtra("username",usernm);
                   intent.putExtra("msgmodel",  chatMessageModel);
                   messageUserList.startActivity(intent);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (newchtuserlist.size() > 0 && newchtuserlist != null) {
            return newchtuserlist.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView username,email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=(CircleImageView)itemView.findViewById(R.id.userImage);
            username=(TextView)itemView.findViewById(R.id.userName);
            email=(TextView)itemView.findViewById(R.id.userEmail);
        }
    }

    public void updateList(List<DataItem> newchtuserlist) {
        this.newchtuserlist = newchtuserlist;
        notifyDataSetChanged();
    }
}
