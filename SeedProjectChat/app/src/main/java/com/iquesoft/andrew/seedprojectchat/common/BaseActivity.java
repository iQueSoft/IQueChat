package com.iquesoft.andrew.seedprojectchat.common;

import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.iquesoft.andrew.seedprojectchat.app.SeedProjectChatApp;
import com.iquesoft.andrew.seedprojectchat.di.components.ISeedProjectChatComponent;

/**
 * Created by Andrew on 16.08.2016.
 */

public abstract class BaseActivity extends MvpAppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponent(SeedProjectChatApp.get(this).getAppComponent());
    }

    protected abstract void setupComponent(ISeedProjectChatComponent appComponent);
}
