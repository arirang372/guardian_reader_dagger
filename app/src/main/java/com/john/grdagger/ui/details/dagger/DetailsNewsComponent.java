package com.john.grdagger.ui.details.dagger;

import com.john.grdagger.ui.details.DetailsNewsActivity;

import dagger.Component;

/**
 * Created by john on 9/29/2017.
 */

@DetailsNewsScope
@Component(modules = {DetailsNewsModule.class})
public interface DetailsNewsComponent
{
    void inject(DetailsNewsActivity newsActivity);
}
