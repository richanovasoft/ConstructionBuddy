
package com.consturctionbuddy.Bean;

import com.google.gson.annotations.SerializedName;

public class Leafe {

    @SerializedName("approved_status")
    private String mApprovedStatus;

    @SerializedName("color_code")
    private String mColorCode;

    @SerializedName("datetime")
    private String mDatetime;

    @SerializedName("end_date")
    private String mEndDate;

    @SerializedName("reasonleave")
    private String mReasonleave;

    @SerializedName("start_date")
    private String mStartDate;

    @SerializedName("userid")
    private String mUserid;

    @SerializedName("__v")
    private int m_V;

    @SerializedName("_id")
    private String m_id;

    public String getApprovedStatus() {
        return mApprovedStatus;
    }

    public void setApprovedStatus(String approvedStatus) {
        mApprovedStatus = approvedStatus;
    }

    public String getColorCode() {
        return mColorCode;
    }

    public void setColorCode(String colorCode) {
        mColorCode = colorCode;
    }

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

    public String getReasonleave() {
        return mReasonleave;
    }

    public void setReasonleave(String reasonleave) {
        mReasonleave = reasonleave;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    public String getUserid() {
        return mUserid;
    }

    public void setUserid(String userid) {
        mUserid = userid;
    }

    public int get_V() {
        return m_V;
    }

    public void set_V(int _V) {
        m_V = _V;
    }

    public String get_id() {
        return m_id;
    }

    public void set_id(String _id) {
        m_id = _id;
    }

}
