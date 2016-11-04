package com.iquesoft.andrew.seedprojectchat.common;

import android.util.Log;

import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by Andrew on 17.08.2016.
 */

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
