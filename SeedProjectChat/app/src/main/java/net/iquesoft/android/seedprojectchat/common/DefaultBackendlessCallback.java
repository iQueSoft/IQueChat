package net.iquesoft.android.seedprojectchat.common;

import android.util.Log;

import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;

public class DefaultBackendlessCallback<T> extends BackendlessCallback<T> {

    public DefaultBackendlessCallback()
    {
    }

    @Override
    public void handleResponse( T response )
    {
        if (response != null){
            try {
                Log.d("response", response.toString());
            } catch (Throwable t){
                t.printStackTrace();
            }
        } else {
            return;
        }
    }

    @Override
    public void handleFault( BackendlessFault fault )
    {
        Log.d("fault", fault.getMessage());
    }
}
