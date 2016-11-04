package com.iquesoft.andrew.seedprojectchat.util;

import android.content.Context;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;

/**
 * Created by andru on 9/6/2016.
 */

public class UpdateCurentUser {

    public void update(BackendlessUser backendlessUser, Context context){
        Thread updateUserThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Backendless.UserService.update(backendlessUser, new DefaultBackendlessCallback<BackendlessUser>(){
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        super.handleResponse(response);
                        Log.i("userService", "Complete");
                    }
                });
            }
        });
        updateUserThread.run();
    }


}
