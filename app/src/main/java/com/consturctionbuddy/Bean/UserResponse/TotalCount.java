package com.consturctionbuddy.Bean.UserResponse;

import com.google.gson.annotations.SerializedName;

public class TotalCount {



    @SerializedName("status")
    private int mStatus;

    @SerializedName("totalusers")
    private String mTotalUsers;

    @SerializedName("totalmaterials")
    private String mTotalMaterials;


    @SerializedName("totalstaffs")
    private String mTotalStaffs;

    @SerializedName("totalproject")
    private String mTotalProjects;


    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getmTotalUsers() {
        return mTotalUsers;
    }

    public void setmTotalUsers(String mTotalUsers) {
        this.mTotalUsers = mTotalUsers;
    }

    public String getmTotalMaterials() {
        return mTotalMaterials;
    }

    public void setmTotalMaterials(String mTotalMaterials) {
        this.mTotalMaterials = mTotalMaterials;
    }

    public String getmTotalStaffs() {
        return mTotalStaffs;
    }

    public void setmTotalStaffs(String mTotalStaffs) {
        this.mTotalStaffs = mTotalStaffs;
    }

    public String getmTotalProjects() {
        return mTotalProjects;
    }

    public void setmTotalProjects(String mTotalProjects) {
        this.mTotalProjects = mTotalProjects;
    }
}
