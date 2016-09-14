package com.iquesoft.andrew.seedprojectchat.receiver;

import com.backendless.push.BackendlessBroadcastReceiver;
import com.iquesoft.andrew.seedprojectchat.services.PushServices;

/**
 * Created by andru on 9/6/2016.
 */

public class PushReceiver extends BackendlessBroadcastReceiver {
    @Override
    public Class<PushServices> getServiceClass() {
        return PushServices.class;
    }
}
