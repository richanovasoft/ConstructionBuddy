
package com.consturctionbuddy.Bean.TimeLine;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ProjectImg implements Parcelable{

    @SerializedName("originalname")
    private String mOriginalname;

    @SerializedName("path")
    private String mPath;


    protected ProjectImg(Parcel in) {
        mOriginalname = in.readString();
        mPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOriginalname);
        dest.writeString(mPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProjectImg> CREATOR = new Creator<ProjectImg>() {
        @Override
        public ProjectImg createFromParcel(Parcel in) {
            return new ProjectImg(in);
        }

        @Override
        public ProjectImg[] newArray(int size) {
            return new ProjectImg[size];
        }
    };

    public String getOriginalname() {
        return mOriginalname;
    }

    public void setOriginalname(String originalname) {
        mOriginalname = originalname;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }


}
