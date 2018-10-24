
package com.consturctionbuddy.Bean.DailyWork;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("datetime")
    private String mDatetime;

    //@SerializedName("project_img")
   // private List<Object> mProjectImg;

    @SerializedName("projectid")
    private String mProjectid;

    @SerializedName("today_work_descr")
    private String mTodayWorkDescr;

    @SerializedName("today_work_subject")
    private String mTodayWorkSubject;

    @SerializedName("userid")
    private String mUserid;

    @SerializedName("working_date")
    private String mWorkingDate;


    @SerializedName("_id")
    private String m_id;

    public String getDatetime() {
        return mDatetime;
    }

    public void setDatetime(String datetime) {
        mDatetime = datetime;
    }

    public String getProjectid() {
        return mProjectid;
    }

    public void setProjectid(String projectid) {
        mProjectid = projectid;
    }

    public String getTodayWorkDescr() {
        return mTodayWorkDescr;
    }

    public void setTodayWorkDescr(String todayWorkDescr) {
        mTodayWorkDescr = todayWorkDescr;
    }

    public String getTodayWorkSubject() {
        return mTodayWorkSubject;
    }

    public void setTodayWorkSubject(String todayWorkSubject) {
        mTodayWorkSubject = todayWorkSubject;
    }

    public String getUserid() {
        return mUserid;
    }

    public void setUserid(String userid) {
        mUserid = userid;
    }

    public String getWorkingDate() {
        return mWorkingDate;
    }

    public void setWorkingDate(String workingDate) {
        mWorkingDate = workingDate;
    }


    public String get_id() {
        return m_id;
    }

    public void set_id(String _id) {
        m_id = _id;
    }

}
