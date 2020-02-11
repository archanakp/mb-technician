package com.battmobile.battmobiletechnician.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.chat.model.ChatListPojo;
import com.battmobile.battmobiletechnician.chat.model.ChatMessageModel;
import com.battmobile.battmobiletechnician.chat.model.DataItem;
import com.battmobile.battmobiletechnician.utility.Apiurl;
import com.battmobile.battmobiletechnician.utility.SessionManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChatUserList extends AppCompatActivity {

    //Button fire;
    ListView listView;
    Disposable disposable;
    List<DataItem> chtlist=new ArrayList<>();
    List<String>name=new ArrayList<>();
    String techid;
    FirebaseDatabase secondaryDatabase;
    SessionManager sessionManager;
    Map<String, String> timestamp;
    String uniqid,chatid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user_list);

        listView=(ListView)findViewById(R.id.recycle);

         sessionManager=new SessionManager(ChatUserList.this);
       techid= sessionManager.getTECHNICIAN_ID();

        functioncall();

       // fire=(Button)findViewById(R.id.fire);

/*
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve my other app.

                Log.e("secvalue",String.valueOf(secondaryDatabase));
                Log.d("secvalue",String.valueOf(secondaryDatabase));
                Log.i("secvalue",String.valueOf(secondaryDatabase));
                Toast.makeText(getApplicationContext(),String.valueOf(secondaryDatabase.getReference()),Toast.LENGTH_SHORT).show();
            }
        });
*/

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:523469674629:android:95a532fefa8c227c3a71ec") // Required for Analytics.
                .setApiKey("AIzaSyDY2FltcrzCUWF-rFtolJRFbPjm6exmkmM") // Required for Auth.
                .setDatabaseUrl("https://battery-mobile.firebaseio.com/") // Required for RTDB.
                .build();
        FirebaseApp.initializeApp(this  , options, "secondary");
        FirebaseApp app = FirebaseApp.getInstance("secondary");
// Get the database for the other app.
         secondaryDatabase = FirebaseDatabase.getInstance(app);
    }

    private void functioncall() {

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
                chtlist.addAll(value.getData());
                for (int i=0;i<value.getData().size();i++){
                    name.add(value.getData().get(i).getName());
                }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChatUserList.this, android.R.layout.simple_list_item_1,android.R.id.text1 ,name);
                        listView.setAdapter(arrayAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                uniqid=techid+"_"+chtlist.get(position).getUsername();
                                uniqid=Base64.encodeToString(uniqid.getBytes(), Base64.NO_WRAP);
                                chatid= UUID.randomUUID().toString();
                                chatid="cuid_"+chatid;

timestamp=ServerValue.TIMESTAMP;

secondaryDatabase.getReference("UserChats").child(sessionManager.getUSER_NAME()).child(uniqid).setValue("");
secondaryDatabase.getReference("UserChats").child(chtlist.get(position).getUsername()).child(uniqid).setValue("");


                                final ChatMessageModel chatMessageModel=new ChatMessageModel("hey customer_care",chtlist.get(position).getUsername(),sessionManager.getUSER_NAME(),timestamp,chtlist.get(position).getUsername(),sessionManager.getUSER_NAME(),"",chatid);
                                HashMap<String,Object>updatevalue=new HashMap<>();
                                updatevalue.put("message","hey customer_care");
                                updatevalue.put("to",chtlist.get(position).getUsername());
                                updatevalue.put("from",sessionManager.getUSER_NAME());
                                updatevalue.put("timestamp",timestamp);
                                secondaryDatabase.getReference("MessageMaster").child(chatid).child(String.valueOf(System.currentTimeMillis())).updateChildren(updatevalue, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
secondaryDatabase.getReference("ChatMaster").child(uniqid).setValue(chatMessageModel, new DatabaseReference.CompletionListener() {
    @Override
    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

    }
});
                                    }
                                });

                            }
                        });
                    }
                });
    }
}
