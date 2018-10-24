
package com.consturctionbuddy.Bean.ProjectList;

import com.google.gson.annotations.SerializedName;

public class Projectlist {

    @SerializedName("datetime")
    private String mDatetime;

    @SerializedName("end_date")
    private String mEndDate;

    @SerializedName("img")
    private Img mImg;

    @SerializedName("project_category")
    private String mProjectCategory;

    @SerializedName("project_description")
    private String mProjectDescription;

    @SerializedName("project_name")
    private String mProjectName;

    @SerializedName("staffid")
    private String mStaffid;

    @SerializedName("start_date")
    private String mStartDate;


    @SerializedName("_id")
    private String m_id;

    public String getDatetime() {
        return mDatetime;
    }

    public void setDatetime(String datetime) {
        mDatetime = datetime;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }

    public Img getImg() {
        return mImg;
    }

    public void setImg(Img img) {
        mImg = img;
    }

    public String getProjectCategory() {
        return mProjectCategory;
    }

    public void setProjectCategory(String projectCategory) {
        mProjectCategory = projectCategory;
    }

    public String getProjectDescription() {
        return mProjectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        mProjectDescription = projectDescription;
    }

    public String getProjectName() {
        return mProjectName;
    }

    public void setProjectName(String projectName) {
        mProjectName = projectName;
    }

    public String getStaffid() {
        return mStaffid;
    }

    public void setStaffid(String staffid) {
        mStaffid = staffid;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }


    public String get_id() {
        return m_id;
    }

    public void set_id(String _id) {
        m_id = _id;
    }

}
