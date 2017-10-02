package com.john.grdagger.ui.details.core;

import com.john.grdagger.models.GuardianContent;
import com.john.grdagger.ui.details.DetailsNewsActivity;
import java.util.concurrent.TimeUnit;
import io.realm.Realm;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by john on 9/29/2017.
 */

public class DetailsNewsModel
{
    private static final String TAG = "DetailsNewsModel";
    private Realm realm;

    public DetailsNewsModel(Realm realm)
    {
        this.realm = realm;
    }

    public Observable<Long> getTimerObservable()
    {
        return Observable.timer(2, TimeUnit.SECONDS)
                         .observeOn(AndroidSchedulers.mainThread());
    }


    public void markAsRead(final String sectionId, final boolean read)
    {
        realm.executeTransactionAsync(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                GuardianContent news = realm.where(GuardianContent.class)
                                            .equalTo("sectionId", sectionId)
                                            .findFirst();
                if(news != null)
                {
                    news.setIsRead(read);
                }
            }
        }, new Realm.Transaction.OnError()
        {

            @Override
            public void onError(Throwable error)
            {
                Timber.d(TAG, error.getMessage());
            }
        });
    }

}
