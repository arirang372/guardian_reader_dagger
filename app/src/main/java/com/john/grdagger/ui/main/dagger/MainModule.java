package com.john.grdagger.ui.main.dagger;

import com.john.grdagger.R;
import com.john.grdagger.application.builder.GuardianApi;
import com.john.grdagger.application.builder.rx.RxSchedulers;
import com.john.grdagger.ui.main.MainActivity;
import com.john.grdagger.ui.main.core.MainModel;
import com.john.grdagger.ui.main.core.MainPresenter;
import com.john.grdagger.ui.main.core.MainView;
import com.john.grdagger.utils.PreferenceManager;

import java.util.HashMap;
import java.util.Map;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johns on 9/28/2017.
 */
@Module
public class MainModule
{
    MainActivity mainActivityContext;

    public MainModule(MainActivity context)
    {
        this.mainActivityContext = context;
    }

    @MainScope
    @Provides
    MainPresenter providePresenter(RxSchedulers schedulers, MainModel model)
    {
        return new MainPresenter(schedulers, mainActivityContext, model);
    }

    @MainScope
    @Provides
    MainActivity provideContext()
    {
        return mainActivityContext;
    }

    @MainScope
    @Provides
    MainModel provideModel(GuardianApi api)
    {
        PreferenceManager pref = new PreferenceManager(mainActivityContext);
        Map<String, Long> lastNetworkRequest = new HashMap<>();
        BehaviorSubject<Boolean> networkLoading = BehaviorSubject.create(false);
        return new MainModel( pref, mainActivityContext.getString(R.string.api_key), api,
                    lastNetworkRequest, networkLoading, Realm.getDefaultInstance());
    }

}
