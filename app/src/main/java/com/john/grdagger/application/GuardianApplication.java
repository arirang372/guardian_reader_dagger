package com.john.grdagger.application;

import android.app.Application;
import com.john.grdagger.application.builder.AppComponent;
import com.john.grdagger.application.builder.AppContextModule;
import com.john.grdagger.application.builder.DaggerAppComponent;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.BuildConfig;
import timber.log.Timber;

/**
 * Created by john on 7/11/2017.
 */

public class GuardianApplication extends Application
{
    private final String TAG = this.getClass().getSimpleName();
    private static AppComponent appComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                                         .appContextModule(new AppContextModule(this))
                                         .build();

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfig);

        initializeLogger();
    }

    private void initializeLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new Timber.Tree() {
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {

                }
            });
        }
    }

    public static AppComponent getAppComponent()
    {
        return appComponent;
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }

}
