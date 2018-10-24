package com.consturctionbuddy.Bean.Projects;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class StateResponse {

    @SerializedName("status")
    private String mStatus;

    @SerializedName("projects")
    ArrayList<StateInfo> mProjectsList;


    public String getStatus() {
        return mStatus;
    }

    public ArrayList<StateInfo> getStateInfo() {
        return mProjectsList;
    }

    public ArrayList<String> getStateList() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < mProjectsList.size(); i++) {
            StateInfo state = mProjectsList.get(i);
            stringArrayList.add(state.getmProjectCategory());
        }
        return stringArrayList;
    }
}
