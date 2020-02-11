package com.battmobile.battmobiletechnician.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.chat.model.ChatMessageModel;
import com.battmobile.battmobiletechnician.chat.model.ChatMessageModelFetch;
import com.battmobile.battmobiletechnician.chat.model.LoadMessageModel;
import com.battmobile.battmobiletechnician.home.HomeActivity;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    EditText chteditText;
    ImageView sendbtn,backbutton;
    TextView urnm;
    String username;

   public static int VIEW_TYPE_USER_MESSAGE = 0;
  public static   int VIEW_TYPE_FRIEND_MESSAGE = 1;

    SessionManager sessionManager;
    Map<String, String> timestamp;
    ArrayList<LoadMessageModel>loadMessageModels=new ArrayList<>();
    String uniqid;
    public static String techid;
    ChatMessageModelFetch cmd;
    MessageLoadAdapter messageLoadAdapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        urnm=(TextView)findViewById(R.id.userName);
        backbutton=(ImageView)findViewById(R.id.backBtn);
        recyclerView=(RecyclerView)findViewById(R.id.chatList);

        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        username=getIntent().getExtras().getString("username");
        cmd=(ChatMessageModelFetch)getIntent().getSerializableExtra("msgmodel");
        if (cmd==null){
            finish();
        }
        chteditText=(EditText)findViewById(R.id.chatText);
        sendbtn=(ImageView)findViewById(R.id.btn_send);
        urnm.setText(username);

        MessageUserListAdapter.visible=false;
        UserProfileChatLoadAdapter.visible=false;

        sessionManager=new SessionManager(ChatActivity.this);
        techid= sessionManager.getUSER_NAME();


        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chteditText.getText().toString().equals("")){
                    uniqid=techid+"_"+username;
                    uniqid= Base64.encodeToString(uniqid.getBytes(), Base64.NO_WRAP);

                    timestamp= ServerValue.TIMESTAMP;

                    HashMap<String,Object> updatevalue=new HashMap<>();
                    updatevalue.put("message",chteditText.getText().toString());
                    updatevalue.put("to",username);
                    updatevalue.put("from",techid);
                    updatevalue.put("timestamp",timestamp);

                    final ChatMessageModel chatMessageModel=new ChatMessageModel(chteditText.getText().toString(),username,techid,timestamp,username,techid,"",cmd.getChatUID());
                    chteditText.getText().clear();

                    DatabaseReference dr=HomeActivity.secondaryDatabase.getReference("MessageMaster").child(cmd.getChatUID()).child(String.valueOf(System.currentTimeMillis()));
                    dr.updateChildren(updatevalue, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            HomeActivity.secondaryDatabase.getReference("ChatMaster").child(uniqid).setValue(chatMessageModel, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                }
                            });

                        }
                    });

                }
            }
        });
fetchAllmsgs();

backbutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onBackPressed();
    }
});

    }

    private void fetchAllmsgs() {

        messageLoadAdapter=new MessageLoadAdapter(ChatActivity.this,loadMessageModels,uniqid);

        HomeActivity.secondaryDatabase.getReference("MessageMaster").child(cmd.getChatUID()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LoadMessageModel ld=dataSnapshot.getValue(LoadMessageModel.class);
                loadMessageModels.add(ld);
                messageLoadAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(loadMessageModels.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
recyclerView.setAdapter(messageLoadAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
