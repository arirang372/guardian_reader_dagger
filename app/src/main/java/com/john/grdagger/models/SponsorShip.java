package com.john.grdagger.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by john on 7/12/2017.
 */

public class SponsorShip extends RealmObject
{
    @SerializedName("sponsorshipType")
    @Expose
    public String sponsorshipType;//sectionId

    @SerializedName("sponsorName")
    @Expose
    public String sponsorName;

    @SerializedName("sponsorLogo")
    @Expose
    public String sponsorLogo;

    @SerializedName("sponsorLink")
    @Expose
    public String sponsorLink;

    @SerializedName("aboutLink")
    @Expose
    public String aboutLink;

    @SerializedName("sponsorLogoDimensions")
    @Expose
    public LogoDimension sponsorLogoDimensions;
}
