package com.battmobile.battmobiletechnician.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.cash_in_hand.CashInHandActivity;
import com.battmobile.battmobiletechnician.home.HomeActivity;
import com.battmobile.battmobiletechnician.job_module.JobActivity;
import com.battmobile.battmobiletechnician.job_module.JobDetailActivity;

import static android.content.Context.NOTIFICATION_SERVICE;


public class MyNotificationManager {

    private Context mCtx;
    private static MyNotificationManager mInstance;

    public MyNotificationManager(Context context) {
        mCtx = context;
    }

    public static synchronized MyNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title, String message,String type,String record_id) {



        Uri notificationsound= RingtoneManager.getActualDefaultRingtoneUri(mCtx,RingtoneManager.TYPE_NOTIFICATION);
        Intent resultIntent=null;
switch (type){
    case "cash_received" :
        resultIntent = new Intent(mCtx, CashInHandActivity.class);
        break;

    case "reschedule_accepted" :
        resultIntent = new Intent(mCtx, JobDetailActivity.class);
        resultIntent.putExtra("id",record_id);
        break;

    case "reschedule_rejected" :
        resultIntent = new Intent(mCtx, JobDetailActivity.class);
        resultIntent.putExtra("id",record_id);
        break;

    case "overtime_accepted" :
        resultIntent = new Intent(mCtx, JobDetailActivity.class);
        resultIntent.putExtra("id",record_id);
        break;

    case "overtime_rejected" :
        resultIntent = new Intent(mCtx, JobDetailActivity.class);
        resultIntent.putExtra("id",record_id);
        break;

    case "job_assigned" :
        resultIntent = new Intent(mCtx, JobDetailActivity.class);
        resultIntent.putExtra("id",record_id);
        break;

        default:
            resultIntent = new Intent(mCtx, HomeActivity.class);
            break;
}


        //resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent resultPendingIntent =PendingIntent.getActivity(mCtx,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.setShowBadge(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            NotificationCompat.Builder notificationcha = new NotificationCompat.Builder(mCtx, Constants.CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo_batt_mobile).setTicker(title).setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.logo_batt_mobile)
                    .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.logo_batt_mobile))
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setSound(notificationsound);

            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(1, notificationcha.build());
        }else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx,Constants.CHANNEL_ID);

            Notification notification = mBuilder.setSmallIcon(R.drawable.logo_batt_mobile).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.logo_batt_mobile)
                    .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.logo_batt_mobile))
                    .setContentText(message)
                    .setSound(notificationsound)
                    .build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(1, notification);
        }
    }
}
