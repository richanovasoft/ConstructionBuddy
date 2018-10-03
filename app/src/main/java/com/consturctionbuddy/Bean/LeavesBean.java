
package com.consturctionbuddy.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LeavesBean {

    @SerializedName("leaves")
    private ArrayList<Leafe> mLeaves;

    @SerializedName("status")
    private int mStatus;

    public ArrayList<Leafe> getLeaves() {
        return mLeaves;
    }

    public void setLeaves(ArrayList<Leafe> leaves) {
        mLeaves = leaves;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

}
