package com.consturctionbuddy.Bean.TotalUsers;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TotalUsersBean {


    @SerializedName("site_image")
    private ArrayList<TotalUsersListBean> mTotalUserList;

    @SerializedName("status")
    private int mStatus;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public ArrayList<TotalUsersListBean> getmTotalUserList() {
        return mTotalUserList;
    }

    public void setmTotalUserList(ArrayList<TotalUsersListBean> mTotalUserList) {
        this.mTotalUserList = mTotalUserList;
    }
}
