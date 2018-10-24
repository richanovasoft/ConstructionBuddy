
package com.consturctionbuddy.Bean.UserResponse;

import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("userInfo")
    private User mUser;

    @SerializedName("status")
    private int mStatus;

    @SerializedName("meesage")
    private String mStrMsg;

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }


    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getStrMsg() {
        return mStrMsg;
    }

    public void setStrMsg(String mStrMsg) {
        this.mStrMsg = mStrMsg;
    }
}
