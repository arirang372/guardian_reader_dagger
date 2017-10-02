package com.john.grdagger.application.builder;

import com.john.grdagger.models.rest.HttpContentResponse;
import com.john.grdagger.models.rest.HttpSectionResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by johns on 9/28/2017.
 */

public interface GuardianApi
{

    @GET("/sections")
    Observable<HttpSectionResponse> getSectionNames(@Query(value = "sections", encoded = true) String sectionId,
                                                    @Query(value = "api-key", encoded = true) String apiKey);


    @GET("/{sectionId}")
    Observable<HttpContentResponse> getNewsContents(@Path("sectionId") String sectionId,
                                                    @Query("api-key") String apiKey);

}
