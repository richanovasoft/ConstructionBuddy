
package com.consturctionbuddy.Bean;

import com.google.gson.annotations.SerializedName;

public class LoginResponce {

    @SerializedName("address")
    private String mAddress;

    @SerializedName("age")
    private String mAge;

    @SerializedName("date")
    private String mDate;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("fullName")
    private String mFullName;

    @SerializedName("pass_status")
    private String mPassStatus;

    @SerializedName("password")
    private String mPassword;

    @SerializedName("payment_status")
    private String mPaymentStatus;

    @SerializedName("phone")
    private String mPhone;

    @SerializedName("relationship")
    private String mRelationship;

    @SerializedName("relative_email")
    private String mRelativeEmail;

    @SerializedName("relative_name")
    private String mRelativeName;

    @SerializedName("relative_phone")
    private String mRelativePhone;

    @SerializedName("user_id")
    private String mUserId;

    @SerializedName("user_status")
    private String mUserStatus;

    @SerializedName("message")
    private String mMessage;

    @SerializedName("status")
    private String mStatus;


    @SerializedName("firebaseRegistrationId")
    private String firebaseRegistrationId;

    @SerializedName("deviceId")
    private String deviceId;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getAge() {
        return mAge;
    }

    public void setAge(String age) {
        mAge = age;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public String getPassStatus() {
        return mPassStatus;
    }

    public void setPassStatus(String passStatus) {
        mPassStatus = passStatus;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getPaymentStatus() {
        return mPaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        mPaymentStatus = paymentStatus;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getRelationship() {
        return mRelationship;
    }

    public void setRelationship(String relationship) {
        mRelationship = relationship;
    }

    public String getRelativeEmail() {
        return mRelativeEmail;
    }

    public void setRelativeEmail(String relativeEmail) {
        mRelativeEmail = relativeEmail;
    }

    public String getRelativeName() {
        return mRelativeName;
    }

    public void setRelativeName(String relativeName) {
        mRelativeName = relativeName;
    }

    public String getRelativePhone() {
        return mRelativePhone;
    }

    public void setRelativePhone(String relativePhone) {
        mRelativePhone = relativePhone;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getUserStatus() {
        return mUserStatus;
    }

    public void setUserStatus(String userStatus) {
        mUserStatus = userStatus;
    }


    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getFirebaseRegistrationId() {
        return firebaseRegistrationId;
    }

    public void setFirebaseRegistrationId(String firebaseRegistrationId) {
        this.firebaseRegistrationId = firebaseRegistrationId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
