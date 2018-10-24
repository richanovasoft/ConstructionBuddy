
package com.consturctionbuddy.Bean.SiteImage;


import com.google.gson.annotations.SerializedName;

public class SiteImageBean {


    @SerializedName("_id")
    private String mSiteId;


    @SerializedName("site_title")
    private String mSiteName;

    @SerializedName("img")
    private siteImage mSiteImage;

    public String getmSiteName() {
        return mSiteName;
    }

    public void setmSiteName(String mSiteName) {
        this.mSiteName = mSiteName;
    }


    public String getmSiteId() {
        return mSiteId;
    }

    public void setmSiteId(String mSiteId) {
        this.mSiteId = mSiteId;
    }

    public siteImage getmSiteImage() {
        return mSiteImage;
    }

    public void setmSiteImage(siteImage mSiteImage) {
        this.mSiteImage = mSiteImage;
    }
}
