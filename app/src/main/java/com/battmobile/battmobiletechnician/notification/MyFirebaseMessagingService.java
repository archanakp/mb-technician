package com.battmobile.battmobiletechnician.notification;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if(remoteMessage.getData().size() > 0){

            Map<String, String> params = remoteMessage.getData();
            String cus=remoteMessage.getData().get("custom_notification");
            try {
                JSONObject jsonObject2=new JSONObject(cus);
                String title = jsonObject2.getString("title");
                String body = jsonObject2.getString("body");
                String type = jsonObject2.getString("type");
                String record_id=jsonObject2.getString("record_id");

                MyNotificationManager myNotificationManager=new MyNotificationManager(getApplicationContext());
                MyNotificationManager.getInstance(this).displayNotification(title,body,type,record_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
