package com.john.grdagger.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by john on 7/11/2017.
 */
public class GuardianSection extends RealmObject
{
    @PrimaryKey
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


    @SerializedName("editions")
    @Expose
    public RealmList<Edition> editions;


}
