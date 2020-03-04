package com.example.psniproject.LoginScreen.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.psniproject.MainApp.MainActivity;
import com.example.psniproject.R;

public class MyNotificationManager {

    private Context mCtx;
    private static MyNotificationManager mInstance;

    public MyNotificationManager(Context ctx) {
        this.mCtx = ctx;
    }

    public static synchronized MyNotificationManager getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title, String body) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.lock_icon)
                .setContentTitle(title)
                .setContentText(body);

        Intent intent = new Intent(mCtx, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        if(mNotificationManager != null) {
            mNotificationManager.notify(1, mBuilder.build());
        }



    }





    /*public void showNotification(String from, String notification, Intent intent) {

        PendingIntent pendingIntent= PendingIntent.getActivity(
                mCtx,
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mCtx);
        Notification mNotification = builder.setSmallIcon(R.drawable.lock_icon)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(from)
                .setContentText(notification)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.lock_icon))
                .build();

        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, mNotification);
    }*/
}