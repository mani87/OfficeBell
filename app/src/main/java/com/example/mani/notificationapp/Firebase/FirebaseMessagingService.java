package com.example.mani.notificationapp.Firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.mani.notificationapp.R;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by mani on 1/29/18.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Context context = this;

        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();

        NotificationCompat.Builder mBuiler =
                new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000, 1000, 1000 ,1000})
                        .setSound(Uri.parse("android.resource://com.example.mani.notificationapp/raw/song"))
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        int mNotificationId = (int) System.currentTimeMillis();

        NotificationManager mNotifiMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifiMgr.notify(mNotificationId, mBuiler.build());

    }

}
