package psni.example.psniproject.LoginScreen.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import psni.example.psniproject.MainApp.MainActivity;
import psni.example.psniproject.R;

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
                .setSmallIcon(R.drawable.ic_mail_black_24dp)
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
}
