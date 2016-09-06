package com.iquesoft.andrew.seedprojectchat.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by Andrew on 17.08.2016.
 */

public class DefaultBackendlessCallback<T> extends BackendlessCallback<T> {
    private Context context;

    public DefaultBackendlessCallback( Context context )
    {
        this.context = context;
    }

    public DefaultBackendlessCallback( Context context, String message )
    {
        this.context = context;
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
        Toast.makeText( context, fault.getMessage(), Toast.LENGTH_LONG ).show();
    }
}
