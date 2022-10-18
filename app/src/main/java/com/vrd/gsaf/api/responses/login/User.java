package com.vrd.gsaf.api.responses.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vrd.gsaf.api.responses.fairExtraFields.ExtraField;

import java.util.List;

public class User {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name = " ";
    @SerializedName("email")
    @Expose
    private String email = "";
    @SerializedName("fair_id")
    @Expose
    private String fairId;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("postal_code")
    @Expose
    private String postalCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("cv")
    @Expose
    private String cv;
    @SerializedName("user_task_list")
    @Expose
    private String userTaskList;
    @SerializedName("profile_image")
    @Expose
    private String profileImage = " ";
    @SerializedName("user_job_title")
    @Expose
    private String userJobTitle;
    @SerializedName("user_company")
    @Expose
    private String userCompany;
    @SerializedName("rtcSetting")
    @Expose
    private RtcSetting rtcSetting = null;
    @SerializedName("extra_fields")
    @Expose
    private List<com.vrd.gsaf.api.responses.fairExtraFields.ExtraField> extraFields = null;
    @SerializedName("is_take_test")
    @Expose
    private int isTakeTest;

    public int getTakeTest() {
        return isTakeTest;
    }

    public void setTakeTest(int takeTest) {
        isTakeTest = takeTest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFairId() {
        return fairId;
    }

    public void setFairId(String fairId) {
        this.fairId = fairId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getUserTaskList() {
        return userTaskList;
    }

    public void setUserTaskList(String userTaskList) {
        this.userTaskList = userTaskList;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserJobTitle() {
        return userJobTitle;
    }

    public void setUserJobTitle(String userJobTitle) {
        this.userJobTitle = userJobTitle;
    }

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public RtcSetting getRtcSetting() {
        return rtcSetting;
    }

    public void setRtcSetting(RtcSetting rtcSetting) {
        this.rtcSetting = rtcSetting;
    }

    public List<com.vrd.gsaf.api.responses.fairExtraFields.ExtraField> getExtraFields() {
        return extraFields;
    }

    public void setExtraFields(List<ExtraField> extraFields) {
        this.extraFields = extraFields;
    }

}
