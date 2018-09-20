
package com.consturctionbuddy.Bean;

import com.google.gson.annotations.SerializedName;

public class StatusBean {

    @SerializedName("message")
    private String mMessage;

    @SerializedName("status")
    private String mStatus;

    @SerializedName("New Password")
    private String mNewPassword;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }


    public String getmNewPassword() {
        return mNewPassword;
    }

    public void setmNewPassword(String mNewPassword) {
        this.mNewPassword = mNewPassword;
    }
}
