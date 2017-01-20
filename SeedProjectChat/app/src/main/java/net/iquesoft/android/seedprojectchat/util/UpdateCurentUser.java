package net.iquesoft.android.seedprojectchat.util;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import net.iquesoft.android.seedprojectchat.common.DefaultBackendlessCallback;

public class UpdateCurentUser {
    public void update(BackendlessUser backendlessUser) {
        Backendless.UserService.update(backendlessUser, new DefaultBackendlessCallback<>());
    }
}

