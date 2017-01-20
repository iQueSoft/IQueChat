package net.iquesoft.android.seedprojectchat.di.modules;

import net.iquesoft.android.seedprojectchat.view.interfaces.activity.ILoginActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginActivityModule {

    ILoginActivity view;

    public LoginActivityModule(ILoginActivity view){
        this.view = view;
    }

    @Provides
    ILoginActivity provideView(){
        return view;
    }
}
