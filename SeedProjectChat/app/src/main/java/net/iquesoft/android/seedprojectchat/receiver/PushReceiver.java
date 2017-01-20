package net.iquesoft.android.seedprojectchat.receiver;

import com.backendless.push.BackendlessBroadcastReceiver;
import net.iquesoft.android.seedprojectchat.services.PushServices;

public class PushReceiver extends BackendlessBroadcastReceiver {
    @Override
    public Class<PushServices> getServiceClass() {
        return PushServices.class;
    }
}
