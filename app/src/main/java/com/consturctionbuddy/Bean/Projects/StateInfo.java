package com.consturctionbuddy.Bean.Projects;

import com.google.gson.annotations.SerializedName;


public class StateInfo {
    @SerializedName("_id")
    private int mProjectsId;

    @SerializedName("project_category")
    private String mProjectCategory;


    public int getmProjectsId() {
        return mProjectsId;
    }

    public void setmProjectsId(int mProjectsId) {
        this.mProjectsId = mProjectsId;
    }

    public String getmProjectCategory() {
        return mProjectCategory;
    }

    public void setmProjectCategory(String mProjectCategory) {
        this.mProjectCategory = mProjectCategory;
    }
}
