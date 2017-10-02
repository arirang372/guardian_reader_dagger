package com.john.grdagger.application.builder;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by johns on 9/28/2017.
 */

@Module
public class GuaridanApiServiceModule {
    private static final String BASE_URL = "https://content.guardianapis.com";

    @AppScope
    @Provides
    GuardianApi provideApiService(OkHttpClient client, GsonConverterFactory gson, RxJavaCallAdapterFactory rxAdapter)
    {
        Retrofit retrofit = new Retrofit.Builder().client(client)
                                                  .baseUrl(BASE_URL)
                                                  .addConverterFactory(gson)
                                                  .addCallAdapterFactory(rxAdapter)
                                                  .build();

        return retrofit.create(GuardianApi.class);
    }
}
