package com.john.grdagger.ui.details.dagger;

import com.john.grdagger.ui.details.DetailsNewsActivity;
import com.john.grdagger.ui.details.core.DetailsNewsModel;
import com.john.grdagger.ui.details.core.DetailsNewsPresenter;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by john on 9/29/2017.
 */

@Module
public class DetailsNewsModule
{
    DetailsNewsActivity detailsNewsActivity;

    public DetailsNewsModule(DetailsNewsActivity context)
    {
        this.detailsNewsActivity = context;
    }

    @DetailsNewsScope
    @Provides
    DetailsNewsPresenter providePresenter(DetailsNewsModel model)
    {
        return new DetailsNewsPresenter(detailsNewsActivity, model);
    }

    @DetailsNewsScope
    @Provides
    DetailsNewsActivity provideContext()
    {
        return detailsNewsActivity;
    }

    @DetailsNewsScope
    @Provides
    DetailsNewsModel provideDetailsNewsModel()
    {
        return new DetailsNewsModel(Realm.getDefaultInstance());
    }


}
