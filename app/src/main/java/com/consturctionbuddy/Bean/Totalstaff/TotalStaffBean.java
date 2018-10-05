package com.consturctionbuddy.Bean.Totalstaff;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TotalStaffBean {


    @SerializedName("site_image")
    private ArrayList<TotalStaffListBean> mTotalStaffList;

    @SerializedName("status")
    private int mStatus;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public ArrayList<TotalStaffListBean> getmTotalStaffList() {
        return mTotalStaffList;
    }

    public void setmTotalStaffList(ArrayList<TotalStaffListBean> mTotalStaffList) {
        this.mTotalStaffList = mTotalStaffList;
    }
}
