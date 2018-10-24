
package com.consturctionbuddy.Bean.UserResponse;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("address")
    private String mAddress;

    @SerializedName("img")
    private Img mImg;

    @SerializedName("local")
    private Local mLocal;

    @SerializedName("staff_img")
    private StaffImg mStaffImg;

    @SerializedName("__v")
    private Long m_V;

    @SerializedName("_id")
    private String m_id;


    @SerializedName("firstName")
    private String mStrName;

    @SerializedName("phone")
    private String mstrPhone;

    @SerializedName("companyName")
    private String mStrCompanyName;

    @SerializedName("companyEmail")
    private String mStrCompanyEmail;

    @SerializedName("companyPhone")
    private String mStrCompanyPhone;

    @SerializedName("companyAddress")
    private String mStrCompanyAdd;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public Img getImg() {
        return mImg;
    }

    public void setImg(Img img) {
        mImg = img;
    }

    public Local getLocal() {
        return mLocal;
    }

    public void setLocal(Local local) {
        mLocal = local;
    }

    public StaffImg getStaffImg() {
        return mStaffImg;
    }

    public void setStaffImg(StaffImg staffImg) {
        mStaffImg = staffImg;
    }

    public Long get_V() {
        return m_V;
    }

    public void set_V(Long _V) {
        m_V = _V;
    }

    public String get_id() {
        return m_id;
    }

    public void set_id(String _id) {
        m_id = _id;
    }


    public String getmStrName() {
        return mStrName;
    }

    public void setmStrName(String mStrName) {
        this.mStrName = mStrName;
    }


    public String getMstrPhone() {
        return mstrPhone;
    }

    public void setMstrPhone(String mstrPhone) {
        this.mstrPhone = mstrPhone;
    }

    public String getmStrCompanyName() {
        return mStrCompanyName;
    }

    public void setmStrCompanyName(String mStrCompanyName) {
        this.mStrCompanyName = mStrCompanyName;
    }

    public String getmStrCompanyEmail() {
        return mStrCompanyEmail;
    }

    public void setmStrCompanyEmail(String mStrCompanyEmail) {
        this.mStrCompanyEmail = mStrCompanyEmail;
    }

    public String getmStrCompanyPhone() {
        return mStrCompanyPhone;
    }

    public void setmStrCompanyPhone(String mStrCompanyPhone) {
        this.mStrCompanyPhone = mStrCompanyPhone;
    }

    public String getmStrCompanyAdd() {
        return mStrCompanyAdd;
    }

    public void setmStrCompanyAdd(String mStrCompanyAdd) {
        this.mStrCompanyAdd = mStrCompanyAdd;
    }
}
