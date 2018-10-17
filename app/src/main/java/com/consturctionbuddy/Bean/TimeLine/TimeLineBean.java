
package com.consturctionbuddy.Bean.TimeLine;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TimeLineBean {

    @SerializedName("data")
    private ArrayList<Datum> mData;

    @SerializedName("status")
    private Long mStatus;

    public ArrayList<Datum> getData() {
        return mData;
    }

    public void setData(ArrayList<Datum> data) {
        mData = data;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}
