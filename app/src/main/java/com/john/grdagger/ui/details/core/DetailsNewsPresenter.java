package com.john.grdagger.ui.details.core;

import com.john.grdagger.ui.Presenter;
import com.john.grdagger.ui.details.DetailsNewsActivity;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by john on 9/29/2017.
 */

public class DetailsNewsPresenter implements Presenter
{
    private DetailsNewsView view;
    private DetailsNewsModel model;

    private Subscription timerSubscription;

    public DetailsNewsPresenter(DetailsNewsActivity context, DetailsNewsModel model)
    {
        view = context;
        this.model = model;
    }


    @Override
    public void onCreate() {

        this.view.showProgress();
    }

    @Override
    public void onPause() {
        if(timerSubscription != null)
            timerSubscription.unsubscribe();
    }

    @Override
    public void onResume() {
        timerSubscription = model.getTimerObservable()
                                 .subscribe(new Action1<Long>() {
                                     @Override
                                     public void call(Long aLong) {
                                         model.markAsRead(view.getContent().sectionId, true);
                                         DetailsNewsPresenter.this.view.hideProgress();
                                     }
                                 });
    }

    @Override
    public void onDestroy() {
        if(timerSubscription != null)
            timerSubscription.unsubscribe();
    }
}
