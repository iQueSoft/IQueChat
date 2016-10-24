package com.iquesoft.andrew.seedprojectchat.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.messaging.PublishOptions;
import com.backendless.push.BackendlessPushService;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.MainActivity;

/**
 * Created by andru on 9/7/2016.
 */

public class PushServices extends BackendlessPushService
{
    private int notifID = 0;

    @Override
    public void onRegistered(Context context, String registrationId )
    {
    }

    @Override
    public void onUnregistered( Context context, Boolean unregistered )
    {
    }

    @Override
    public boolean onMessage( Context context, Intent intent )
    {
        CharSequence tickerText = intent.getStringExtra(PublishOptions.ANDROID_TICKER_TEXT_TAG);
        CharSequence contentTitle = intent.getStringExtra(PublishOptions.ANDROID_CONTENT_TITLE_TAG);
        CharSequence contentText = intent.getStringExtra(PublishOptions.ANDROID_CONTENT_TEXT_TAG);
        String message = intent.getStringExtra("message");
        String publisherId = intent.getStringExtra("BL_PUBLISHER_ID");
        String myID = Backendless.UserService.CurrentUser().getUserId();
        if (!publisherId.equals(myID)){
            if (message.length() > 0) {
                int appIcon = context.getApplicationInfo().icon;
                if (appIcon == 0)
                    appIcon = R.drawable.seed_logo;
                Intent notificationIntent = new Intent(context, MainActivity.class);
                notificationIntent.putExtra("message", message);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
                notificationBuilder.setSmallIcon(appIcon);
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.seed_logo);
                notificationBuilder.setLargeIcon(bitmap);
                notificationBuilder.setTicker(tickerText);
                notificationBuilder.setWhen(System.currentTimeMillis());
                notificationBuilder.setContentTitle(tickerText);
                notificationBuilder.setContentText(message);
                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setContentIntent(contentIntent);

                Notification notification = notificationBuilder.build();

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(++notifID, notification);
            }
        }

        return false;
    }

    @Override
    public void onError( Context context, String message )
    {
        Toast.makeText( context, message, Toast.LENGTH_SHORT).show();
    }
}
