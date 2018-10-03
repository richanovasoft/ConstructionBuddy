
package com.consturctionbuddy.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SiteImageListBean {

    @SerializedName("site_image")
    private ArrayList<SiteImageBean> mSiteImageList;

    @SerializedName("status")
    private int mStatus;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }


    public ArrayList<SiteImageBean> getmSiteImageList() {
        return mSiteImageList;
    }

    public void setmSiteImageList(ArrayList<SiteImageBean> mSiteImageList) {
        this.mSiteImageList = mSiteImageList;
    }
}
