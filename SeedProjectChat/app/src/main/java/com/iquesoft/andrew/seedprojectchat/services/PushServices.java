package com.iquesoft.andrew.seedprojectchat.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.MainActivity;

/**
 * Created by andru on 9/7/2016.
 */

public class PushServices extends GcmListenerService
{
    private int NOTIFICATION_ID = 0;

    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        super.onMessageReceived(s, bundle);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.seed_logo)
                        .setContentTitle(bundle.getCharSequence("You have new message"))
                        .setContentText(bundle.getCharSequence("message"));

        Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(++NOTIFICATION_ID, builder.build());
    }
}
