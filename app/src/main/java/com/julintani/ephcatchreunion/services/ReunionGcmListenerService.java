package com.julintani.ephcatchreunion.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.julintani.ephcatchreunion.R;
import com.julintani.ephcatchreunion.activities.MainActivity;
import com.julintani.ephcatchreunion.models.MessagingManager;

/**
 * Created by ell on 6/8/16.
 */
public class ReunionGcmListenerService extends GcmListenerService {
    private static final String TAG = "ReunionListenerService";
    public static final String MESSAGE_RECEIVED = "MESSAGE_RECEIVED";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        sendNotification(message);
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_cowiconwhite)
                .setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary))
                .setContentTitle("New Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        MessagingManager.getInstance().refreshConversations(getApplicationContext());

        Intent registrationComplete = new Intent(MESSAGE_RECEIVED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}
