package net.iquesoft.android.seedprojectchat.app;

import android.app.Application;
import android.content.Context;

import com.backendless.Backendless;
import net.iquesoft.android.seedprojectchat.common.DefaultsBackendlessKey;
import net.iquesoft.android.seedprojectchat.di.components.DaggerISeedProjectChatComponent;
import net.iquesoft.android.seedprojectchat.di.components.ISeedProjectChatComponent;
import net.iquesoft.android.seedprojectchat.di.modules.SeedProjectChatModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SeedProjectChatApp extends Application {

    private ISeedProjectChatComponent appComponent;

    public static SeedProjectChatApp get(Context context){
        return (SeedProjectChatApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //MultiDex.install(this);
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
        buildGraphAndInject();
        Backendless.setUrl( DefaultsBackendlessKey.SERVER_URL );
        Backendless.initApp( this, DefaultsBackendlessKey.APPLICATION_ID, DefaultsBackendlessKey.SECRET_KEY, DefaultsBackendlessKey.VERSION );
        Backendless.Logging.setLogReportingPolicy(100, 60);
    }

    public ISeedProjectChatComponent getAppComponent(){
        return appComponent;
    }

    public void buildGraphAndInject() {
        appComponent = DaggerISeedProjectChatComponent.builder()
                .seedProjectChatModule(new SeedProjectChatModule(this))
                .build();
        appComponent.inject(this);
    }
}
