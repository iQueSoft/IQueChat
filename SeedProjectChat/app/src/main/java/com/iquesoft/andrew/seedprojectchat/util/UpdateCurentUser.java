package com.iquesoft.andrew.seedprojectchat.util;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;

/**
 * Created by andru on 9/6/2016.
 */

public class UpdateCurentUser {
    public void update(BackendlessUser backendlessUser) {
        Backendless.UserService.update(backendlessUser, new DefaultBackendlessCallback<>());
    }
}

