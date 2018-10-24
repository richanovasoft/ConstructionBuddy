
package com.consturctionbuddy.Bean.ProjectList;

import java.util.ArrayList;

import com.consturctionbuddy.Bean.Projects.StateInfo;
import com.google.gson.annotations.SerializedName;

public class ProjectListBean {

    @SerializedName("projectlist")
    private ArrayList<Projectlist> mProjectlist;

    public ArrayList<Projectlist> getProjectlist() {
        return mProjectlist;
    }

    public void setProjectlist(ArrayList<Projectlist> projectlist) {
        mProjectlist = projectlist;
    }


    public ArrayList<String> getStateList() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < mProjectlist.size(); i++) {
            Projectlist state = mProjectlist.get(i);
            stringArrayList.add(state.getProjectName());
        }
        return stringArrayList;
    }

}
