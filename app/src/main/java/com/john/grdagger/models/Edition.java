package com.john.grdagger.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by john on 7/12/2017.
 */

public class Edition extends RealmObject
{
    @SerializedName("id")
    @Expose
    public String id;//sectionId

    @SerializedName("webTitle")
    @Expose
    public String webTitle;

    @SerializedName("webUrl")
    @Expose
    public String webUrl;

    @SerializedName("apiUrl")
    @Expose
    public String apiUrl;

    @SerializedName("code")
    @Expose
    public String code;
}
