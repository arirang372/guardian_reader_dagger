package com.john.grdagger.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by john on 7/12/2017.
 */

public class HttpSectionResponse
{
    @SerializedName("response")
    @Expose
    public GuardianSectionResponse response;



}
