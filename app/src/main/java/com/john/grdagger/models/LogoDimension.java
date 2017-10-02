package com.john.grdagger.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by john on 7/12/2017.
 */

public class LogoDimension extends RealmObject
{
    @SerializedName("width")
    @Expose
    public String width;//sectionId

    @SerializedName("height")
    @Expose
    public String height;

}
