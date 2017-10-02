package com.john.grdagger.application.builder;

import com.john.grdagger.application.builder.rx.AppRxSchedulers;
import com.john.grdagger.application.builder.rx.RxSchedulers;

import dagger.Module;
import dagger.Provides;

/**
 * Created by johns on 9/28/2017.
 */

@Module
public class RxModule
{
    @Provides
    RxSchedulers provideRxSchedulers()
    {
        return new AppRxSchedulers();
    }
}
