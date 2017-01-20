package net.iquesoft.android.seedprojectchat.di.components;

import net.iquesoft.android.seedprojectchat.di.modules.LoginActivityModule;
import net.iquesoft.android.seedprojectchat.di.scope.ActivityScope;
import net.iquesoft.android.seedprojectchat.view.classes.activity.LoginActivity;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.LoginFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.RegisterFragment;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = ISeedProjectChatComponent.class,
        modules = LoginActivityModule.class
)
public interface ILoginActivityComponent {
    void inject(LoginActivity activity);
    void inject(LoginFragment loginFragment);
    void inject(RegisterFragment registerFragment);
}
