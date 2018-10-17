
package com.consturctionbuddy.Bean.UserResponse;

import com.google.gson.annotations.SerializedName;

public class Img {

    @SerializedName("originalname")
    private String mOriginalname;

    @SerializedName("path")
    private String mPath;

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
