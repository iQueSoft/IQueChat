package net.iquesoft.android.seedprojectchat.common;

import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import net.iquesoft.android.seedprojectchat.app.SeedProjectChatApp;
import net.iquesoft.android.seedprojectchat.di.components.ISeedProjectChatComponent;

public abstract class BaseActivity extends MvpAppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponent(SeedProjectChatApp.get(this).getAppComponent());
    }

    protected abstract void setupComponent(ISeedProjectChatComponent appComponent);
}
