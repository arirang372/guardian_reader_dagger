package com.john.grdagger.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by john on 7/11/2017.
 */
public class GuardianContent extends RealmObject implements Parcelable
{
    @PrimaryKey
    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("sectionId")
    @Expose
    public String sectionId;

    @SerializedName("sectionName")
    @Expose
    public String sectionName;

    @SerializedName("webPublicationDate")
    @Expose
    public String webPublicationDate;

    @SerializedName("webTitle")
    @Expose
    public String webTitle;

    @SerializedName("webUrl")
    @Expose
    public String webUrl;

    @SerializedName("apiUrl")
    @Expose
    public String apiUrl;

    @SerializedName("isHosted")
    @Expose
    public boolean isHosted;

    private boolean isRead;

    public GuardianContent()
    {

    }

    protected GuardianContent(Parcel in) {
        id = in.readString();
        type = in.readString();
        sectionId = in.readString();
        sectionName = in.readString();
        webPublicationDate = in.readString();
        webTitle = in.readString();
        webUrl = in.readString();
        apiUrl = in.readString();
        isHosted = in.readByte() != 0;
        isRead = in.readByte() != 0;
    }

    public static final Creator<GuardianContent> CREATOR = new Creator<GuardianContent>() {
        @Override
        public GuardianContent createFromParcel(Parcel in) {
            return new GuardianContent(in);
        }

        @Override
        public GuardianContent[] newArray(int size) {
            return new GuardianContent[size];
        }
    };

    public void setIsRead(boolean read)
    {
        this.isRead = read;
    }

    public boolean getIsRead()
    {
        return this.isRead;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(sectionId);
        dest.writeString(sectionName);
        dest.writeString(webPublicationDate);
        dest.writeString(webTitle);
        dest.writeString(webUrl);
        dest.writeString(apiUrl);
        dest.writeByte((byte) (isHosted ? 1 : 0));
        dest.writeByte((byte) (isRead ? 1 : 0));
    }
}
