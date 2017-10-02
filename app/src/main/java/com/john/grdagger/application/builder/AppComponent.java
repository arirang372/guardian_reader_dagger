package com.john.grdagger.application.builder;

import com.john.grdagger.application.builder.rx.RxSchedulers;
import dagger.Component;

/**
 * Created by johns on 9/28/2017.
 */

@AppScope
@Component(modules= {NetworkModule.class, AppContextModule.class, RxModule.class, GuaridanApiServiceModule.class})
public interface AppComponent {

    RxSchedulers rxSchedulers();

    GuardianApi apiService();
}
