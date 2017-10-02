package com.john.grdagger.ui.main.core;

import android.app.Activity;
import com.john.grdagger.application.builder.rx.RxSchedulers;
import com.john.grdagger.models.GuardianContent;
import com.john.grdagger.models.GuardianSection;
import com.john.grdagger.ui.Presenter;
import com.john.grdagger.ui.details.DetailsNewsActivity;
import java.util.List;
import io.realm.RealmResults;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by johns on 9/28/2017.
 */

public class MainPresenter implements Presenter
{
    private RxSchedulers rxSchedulers;
    private MainModel model;
    private MainView view;
    private Subscription networkSubscription;
    private Subscription getAllSectionsSubscription;
    private Subscription contentSubscription;
    private List<GuardianContent> newsContents;

    public MainPresenter(RxSchedulers schedulers, MainView view, MainModel model)
    {
        this.rxSchedulers = schedulers;
        this.model = model;
        this.view = view;
    }

    @Override
    public void onCreate()
    {
        getAllSectionsSubscription = getAllSections();
    }

    @Override
    public void onPause()
    {
        if(networkSubscription != null)
            networkSubscription.unsubscribe();
    }

    @Override
    public void onResume()
    {
        if(networkSubscription != null)
            networkSubscription.unsubscribe();

        networkSubscription = subscribeNetwork();
    }

    @Override
    public void onDestroy()
    {
        if(networkSubscription != null)
            networkSubscription.unsubscribe();

        if(getAllSectionsSubscription != null)
            getAllSectionsSubscription.unsubscribe();
    }

    private Subscription subscribeNetwork()
    {
        return model.networkInUse().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean netyworkInUse)
            {
                view.showNetworkLoading(netyworkInUse);
            }
        });
    }

    private Subscription getAllSections()
    {
        return model.getAllSections()
                    .subscribe(new Action1<RealmResults<GuardianSection>>() {
                        @Override
                        public void call(RealmResults<GuardianSection> guardianSections)
                        {
                            view.setupToolBar(guardianSections);
                        }
                    });
    }

    public void listItemSelected(int position)
    {
        DetailsNewsActivity.startActivity((Activity) view, newsContents.get(position));
    }

    private void sectionSelected(GuardianSection section)
    {
        if(section == null)
            return;

        model.setSelectedSection(section);
        if(contentSubscription != null)
            contentSubscription.unsubscribe();

        contentSubscription = model.getSelectedNewsContent()
                .subscribe(new Action1<RealmResults<GuardianContent>>()
                {
                    @Override
                    public void call(RealmResults<GuardianContent> guardianContents)
                    {
                        newsContents = guardianContents;
                        view.showList(newsContents);
                    }
                });
    }

    public void titleSpinnerSelectionSelected(GuardianSection section)
    {
        sectionSelected(section);
    }

    public void refreshList()
    {
        model.reloadNewsContent();
        view.hideProgress();
    }

}
