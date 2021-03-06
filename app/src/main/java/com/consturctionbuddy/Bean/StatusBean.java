
package com.consturctionbuddy.Bean;

import com.google.gson.annotations.SerializedName;

public class StatusBean {

    @SerializedName("message")
    private String mMessage;

    @SerializedName("status")
    private int mStatus;

    @SerializedName("New Password")
    private String mNewPassword;


    @SerializedName("path")
    private String mImagePath;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }


    public String getmNewPassword() {
        return mNewPassword;
    }

    public void setmNewPassword(String mNewPassword) {
        this.mNewPassword = mNewPassword;
    }


    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }
}
