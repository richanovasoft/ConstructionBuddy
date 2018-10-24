package com.consturctionbuddy.Bean.SiteImage;

import com.google.gson.annotations.SerializedName;

public class siteImage {

    @SerializedName("originalname")
    private String originalname;

    public String getOriginalname() {
        return originalname;
    }

    public void setOriginalname(String originalname) {
        this.originalname = originalname;
    }
}
