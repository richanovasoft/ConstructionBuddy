package com.consturctionbuddy.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TimeLineImage implements Parcelable {

    @SerializedName("imge_url")
    private String msTrImageUrl;


    @SerializedName("title")
    private String mTitle;

    @SerializedName("description")
    private String mDesc;

    @SerializedName("Date")
    private String mDate;


    public TimeLineImage(Parcel in) {
        msTrImageUrl = in.readString();
        mTitle = in.readString();
        mDesc = in.readString();
        mDate = in.readString();
    }

    public TimeLineImage() {

    }

    public TimeLineImage(String s, String s1, String s2, String s3) {
        this.msTrImageUrl = s;
        this.mTitle = s1;
        this.mDate = s2;
        this.mDesc = s3;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msTrImageUrl);
        dest.writeString(mTitle);
        dest.writeString(mDesc);
        dest.writeString(mDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TimeLineImage> CREATOR = new Creator<TimeLineImage>() {
        @Override
        public TimeLineImage createFromParcel(Parcel in) {
            return new TimeLineImage(in);
        }

        @Override
        public TimeLineImage[] newArray(int size) {
            return new TimeLineImage[size];
        }
    };

    public String getMsTrImageUrl() {
        return msTrImageUrl;
    }

    public void setMsTrImageUrl(String msTrImageUrl) {
        this.msTrImageUrl = msTrImageUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
