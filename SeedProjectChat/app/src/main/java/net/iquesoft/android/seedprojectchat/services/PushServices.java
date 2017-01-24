package net.iquesoft.android.seedprojectchat.services;

import android.content.Context;
import android.content.Intent;

import com.backendless.push.BackendlessPushService;

import net.iquesoft.android.seedprojectchat.util.PushNotificationUtil;
import net.iquesoft.android.seedprojectchat.util.StringToMapUtils;

import java.util.Map;

public class PushServices extends BackendlessPushService
{

    @Override
    public boolean onMessage(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        Map<String, String> messageMap = StringToMapUtils.splitToMap(message, ", ", "=");
        if (!messageMap.get("message").equals("")){
            PushNotificationUtil.getInstance(context).createInfoNotification(messageMap.get("message"));
        }
        return false;
    }

    @Override
    public void onError(Context context, String message) {
        super.onError(context, message);
    }
}
