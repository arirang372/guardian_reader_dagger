package com.john.grdagger.ui.main.dagger;

import com.john.grdagger.application.builder.AppComponent;
import com.john.grdagger.ui.main.MainActivity;

import dagger.Component;

/**
 * Created by johns on 9/28/2017.
 */
@MainScope
@Component(dependencies = {AppComponent.class}, modules = {MainModule.class})
public interface MainComponent
{
    void inject(MainActivity mainActivity);
}
