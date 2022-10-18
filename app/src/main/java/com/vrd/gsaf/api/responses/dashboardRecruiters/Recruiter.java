package com.vrd.gsaf.api.responses.dashboardRecruiters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vrd.gsaf.api.responses.companies.Company;

public class Recruiter {

    @SerializedName("fair_id")
    @Expose
    private Integer fairId;
    @SerializedName("recruiter_id")
    @Expose
    private String recruiterId;
    @SerializedName("recruiter_percentage")
    @Expose
    private String percentage;
    @SerializedName("recruiter_info")
    @Expose
    private String recruiter_info;
    @SerializedName("recruiter_name")
    @Expose
    private String name;
    @SerializedName("recruiter_title")
    @Expose
    private String title;
    @SerializedName("recruiter_public_email")
    @Expose
    private String publicEmail;
    @SerializedName("recruiter_linkedin")
    @Expose
    private String linkedin;
    @SerializedName("recruiter_image")
    @Expose
    private String recruiterImg;
    @SerializedName("recruiter_status")
    @Expose
    private String recruiterStatus;

    @SerializedName("recruiter_location")
    @Expose
    private String location;
    @SerializedName("allow_schedule")
    @Expose
    private String allowSchedule;
    @SerializedName("scheduling_percentage")
    @Expose
    private String schedulingPercentage;
    @SerializedName("is_chat_enable")
    @Expose
    private String isChatEnable;

    @SerializedName("company_info")
    @Expose
    private Company company;


    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Integer getFairId() {
        return fairId;
    }

    public void setFairId(Integer fairId) {
        this.fairId = fairId;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    public String getRecruiter_info() {
        return recruiter_info;
    }

    public void setRecruiter_info(String recruiter_info) {
        this.recruiter_info = recruiter_info;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicEmail() {
        return publicEmail;
    }

    public void setPublicEmail(String publicEmail) {
        this.publicEmail = publicEmail;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getRecruiterImg() {
        return recruiterImg;
    }

    public void setRecruiterImg(String recruiterImg) {
        this.recruiterImg = recruiterImg;
    }

    public String getRecruiterStatus() {
        return recruiterStatus;
    }

    public void setRecruiterStatus(String recruiterStatus) {
        this.recruiterStatus = recruiterStatus;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAllowSchedule() {
        return allowSchedule;
    }

    public void setAllowSchedule(String allowSchedule) {
        this.allowSchedule = allowSchedule;
    }

    public String getSchedulingPercentage() {
        return schedulingPercentage;
    }

    public void setSchedulingPercentage(String schedulingPercentage) {
        this.schedulingPercentage = schedulingPercentage;
    }

    public String getIsChatEnable() {
        return isChatEnable;
    }

    public void setIsChatEnable(String isChatEnable) {
        this.isChatEnable = isChatEnable;
    }

}
