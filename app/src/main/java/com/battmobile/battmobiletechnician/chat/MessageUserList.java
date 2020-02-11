package com.battmobile.battmobiletechnician.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.chat.model.ChatListPojo;
import com.battmobile.battmobiletechnician.chat.model.ChatMessageModel;
import com.battmobile.battmobiletechnician.chat.model.ChatMessageModelFetch;
import com.battmobile.battmobiletechnician.chat.model.ChatUserLoadModel;
import com.battmobile.battmobiletechnician.chat.model.DataItem;
import com.battmobile.battmobiletechnician.home.HomeActivity;
import com.battmobile.battmobiletechnician.utility.Apiurl;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MessageUserList extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    Disposable disposable;
    List<DataItem> newchtuserlist=new ArrayList<>();
    List<ChatUserLoadModel> chatMessageModelFetches=new ArrayList<>();
    UserProfileChatLoadAdapter userProfileChatLoadAdapter;

    ProgressBar progressBar;
    MessageUserListAdapter messageUserListAdapter;

    SessionManager sessionManager;
    String techid;

    ImageView backarrow;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_user_list);
        floatingActionButton=findViewById(R.id.newChat);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        recyclerView=(RecyclerView)findViewById(R.id.chatList);

        backarrow=(ImageView)findViewById(R.id.backarrow);

        sessionManager=new SessionManager(MessageUserList.this);
        techid= sessionManager.getUSER_NAME();

        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(MessageUserList.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(MessageUserList.this);
                dialog.setContentView(R.layout.chatusersearch);
                EditText editText=(EditText)dialog.findViewById(R.id.searchBar);
                RecyclerView recyclerView=(RecyclerView)dialog.findViewById(R.id.userList);
                ProgressBar progressBar=(ProgressBar)dialog.findViewById(R.id.progressBar);

                LinearLayoutManager linearLayoutManager;
                linearLayoutManager = new LinearLayoutManager(MessageUserList.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                searchUserlistLoad(recyclerView,progressBar,dialog);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        filter(editable.toString());
                    }
                });

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        alredyChatedUser();
    }

    private void alredyChatedUser() {

        progressBar.setVisibility(View.VISIBLE);
        chatMessageModelFetches.clear();


HomeActivity.secondaryDatabase.getReference("UserChats").child(techid).orderByValue().addChildEventListener(new ChildEventListener() {
    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


        HomeActivity.secondaryDatabase.getReference("ChatMaster").child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChatUserLoadModel cmf=dataSnapshot.getValue(ChatUserLoadModel.class);
                chatMessageModelFetches.add(cmf);
                userProfileChatLoadAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

        callofAdapter();
    }

    private void callofAdapter() {
        userProfileChatLoadAdapter=new UserProfileChatLoadAdapter(MessageUserList.this,chatMessageModelFetches);
        recyclerView.setAdapter(userProfileChatLoadAdapter);
        userProfileChatLoadAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MessageUserListAdapter.visible=true;
        UserProfileChatLoadAdapter.visible=true;
    }

    private void searchUserlistLoad(final RecyclerView recyclerView, final ProgressBar progressBar, final Dialog dialog) {
          progressBar.setVisibility(View.VISIBLE);
          newchtuserlist.clear();
        Apiurl.getClint().chatlistpojo("Customer Care Agent")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChatListPojo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable=d;
                    }

                    @Override
                    public void onNext(ChatListPojo value) {
                        newchtuserlist.addAll(value.getData());

                    }

                    @Override
                    public void onError(Throwable e) {
                progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onComplete() {
                         messageUserListAdapter=new MessageUserListAdapter(MessageUserList.this,newchtuserlist,dialog);
                        recyclerView.setAdapter(messageUserListAdapter);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

    }

    void filter(String text) {
      if (newchtuserlist.size()>0){
          List<DataItem> templlist = new ArrayList();
          for (DataItem d : newchtuserlist) {
              //or use .equal(text) with you want equal match
              //use .toLowerCase() for better matches
              if (d.getUsername().toLowerCase().contains(text.toLowerCase())) {
                  templlist.add(d);
              }
          }
          //update recyclerview
          messageUserListAdapter.updateList(templlist);
      }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
