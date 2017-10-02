package com.john.grdagger.ui.main.core;

import com.john.grdagger.application.builder.GuardianApi;
import com.john.grdagger.models.GuardianContent;
import com.john.grdagger.models.GuardianSection;
import com.john.grdagger.models.rest.GuardianContentResponse;
import com.john.grdagger.models.rest.GuardianSectionResponse;
import com.john.grdagger.models.rest.HttpContentResponse;
import com.john.grdagger.models.rest.HttpSectionResponse;
import com.john.grdagger.utils.PreferenceManager;
import com.john.grdagger.utils.Utils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

import static com.john.grdagger.utils.LogUtils.LOGD;

/**
 * Created by johns on 9/28/2017.
 */

public class MainModel
{
    private static final String TAG ="MainModel" ;
    private static final long MINIMUM_NETWORK_WAIT_SEC = 120;
    private Map<String, Long> lastNetworkRequest;
    private Realm realm;
    private GuardianApi service;
    private PreferenceManager preferenceManager;
    private String apiKey;
    private BehaviorSubject<Boolean> networkLoading;
    private GuardianSection selectedSection;
    private Subscription contentSubscription;

    public MainModel(PreferenceManager preferenceManager, String apiKey, GuardianApi api,
                     Map<String, Long> lastNetworkRequest, BehaviorSubject<Boolean> networkLoading, Realm realm )
    {
        this.service = api;
        this.realm = realm;
        this.preferenceManager = preferenceManager;
        this.apiKey = apiKey;
        this.lastNetworkRequest = lastNetworkRequest;
        this.networkLoading = networkLoading;
    }

    public Observable<Boolean> networkInUse()
    {
        return networkLoading.asObservable();
    }

    public Observable<RealmResults<GuardianSection>> getAllSections()
    {
        if(!preferenceManager.getBooleanValue("is_loaded"))
        {
            loadAllSections();
            preferenceManager.put("is_loaded", true);
        }

        return     realm.where(GuardianSection.class)
                        .findAllAsync()
                        .asObservable();
    }

    private void loadAllSections()
    {
        networkLoading.onNext(true);
        service.getSectionNames("", apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<HttpSectionResponse, GuardianSectionResponse>()
                {
                    @Override
                    public GuardianSectionResponse call(HttpSectionResponse httpSectionResponse) {
                        return httpSectionResponse.response;
                    }
                })
                .flatMap(new Func1<GuardianSectionResponse, Observable<List<GuardianSection>>>() {
                    @Override
                    public Observable<List<GuardianSection>> call(GuardianSectionResponse guardianSectionResponse) {
                        return Observable.just(guardianSectionResponse.results);
                    }
                })
                .subscribe(new Action1<List<GuardianSection>>() {
                    @Override
                    public void call(List<GuardianSection> listGuardianResponse)
                    {
                        Timber.d(TAG, "Success - Section received ...");

                        networkLoading.onNext(false);
                        processNewsSections(realm, listGuardianResponse);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        networkLoading.onNext(false);
                        Timber.d(TAG, "Fail - error occurred ...");
                    }
                });
    }

    private void processNewsSections(final Realm realm, final List<GuardianSection> sections)
    {
        if(sections.isEmpty())
            return;

        realm.executeTransactionAsync(new Realm.Transaction()
                                      {
                                          @Override
                                          public void execute(Realm realm)
                                          {
                                              for(GuardianSection s : sections)
                                              {
                                                  realm.copyToRealmOrUpdate(s);
                                              }
                                          }
                                      },
                new Realm.Transaction.OnError()
                {
                    @Override
                    public void onError(Throwable error)
                    {
                        Timber.d(TAG, String.format("Error on saving the sections %s", error.getMessage()));
                    }
                });
    }


    public void setSelectedSection(GuardianSection section)
    {
        selectedSection = section;
        loadNewsFeed(selectedSection.id, false);
    }


    public Observable<RealmResults<GuardianContent>> loadNewsFeed(String sectionId, boolean forceReload)
    {
        if(forceReload || timeSinceLastRequest(sectionId) > MINIMUM_NETWORK_WAIT_SEC)
        {
            loadNextContents(sectionId);
            lastNetworkRequest.put(sectionId, System.currentTimeMillis());
        }

        return realm.where(GuardianContent.class)
                    .equalTo("sectionId", sectionId)
                    .findAllSortedAsync("webPublicationDate", Sort.DESCENDING)
                    .asObservable();
    }


    private void loadNextContents(final String sectionId)
    {
        this.networkLoading.onNext(true);

        if(contentSubscription != null)
            contentSubscription.unsubscribe();

        contentSubscription = service.getNewsContents(sectionId, apiKey)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map(new Func1<HttpContentResponse, GuardianContentResponse>()
                                    {
                                        @Override
                                        public GuardianContentResponse call(HttpContentResponse httpContentResponse)
                                        {
                                            return httpContentResponse.response;
                                        }
                                    })
                                    .flatMap(new Func1<GuardianContentResponse, Observable<List<GuardianContent>>>()
                                    {
                                        @Override
                                        public Observable<List<GuardianContent>> call(GuardianContentResponse guardianContentResponse)
                                        {
                                            return Observable.just(guardianContentResponse.results);
                                        }
                                    })
                                    .subscribe(new Action1<List<GuardianContent>>()
                                    {
                                        @Override
                                        public void call(List<GuardianContent> contents)
                                        {
                                            LOGD(TAG, String.format("Success - Data received : %s", sectionId) );
                                            processNewsContents(realm, contents);
                                            networkLoading.onNext(false);
                                        }
                                    }, new Action1<Throwable>()
                                    {
                                        @Override
                                        public void call(Throwable throwable) {

                                            networkLoading.onNext(false);
                                        }
                                    });
    }

    private void processNewsContents(final Realm realm, final List<GuardianContent> contents)
    {
        if(contents.isEmpty())
            return;

        realm.executeTransactionAsync(new Realm.Transaction()
                                      {
                                          @Override
                                          public void execute(Realm realm)
                                          {
                                              for(GuardianContent c : contents)
                                              {
                                                  c.webPublicationDate = Utils.reformatDate(c.webPublicationDate);

                                                  GuardianContent localContent = realm.where(GuardianContent.class)
                                                          .equalTo("id", c.id).findFirst();
                                                  if(localContent != null)
                                                  {
                                                      c.setIsRead(localContent.getIsRead());
                                                  }

                                                  if(localContent == null || !localContent.webPublicationDate.equals(c.webPublicationDate))
                                                  {
                                                      realm.copyToRealmOrUpdate(c);
                                                  }
                                              }
                                          }
                                      },
                new Realm.Transaction.OnError()
                {
                    @Override
                    public void onError(Throwable error)
                    {
                        LOGD(TAG, String.format("Error on saving the contents %s", error.getMessage()));
                    }
                });
    }

    private long timeSinceLastRequest(String sectionId)
    {
        Long lastRequest = lastNetworkRequest.get(sectionId);
        if(lastRequest != null)
            return TimeUnit.SECONDS.convert(System.currentTimeMillis() - lastRequest, TimeUnit.MILLISECONDS);
        else
            return Long.MAX_VALUE;
    }


    public Observable<RealmResults<GuardianContent>> getSelectedNewsContent()
    {
        return loadNewsFeed(selectedSection.id, false);
    }

    public void reloadNewsContent()
    {
        loadNewsFeed(selectedSection.id, false);
    }



}
