
package com.consturctionbuddy.Bean.UserResponse;

import com.google.gson.annotations.SerializedName;

public class Local {

    @SerializedName("email")
    private String mEmail;

    @SerializedName("password")
    private String mPassword;

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

}
