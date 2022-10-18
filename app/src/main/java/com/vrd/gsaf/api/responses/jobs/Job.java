package com.vrd.gsaf.api.responses.jobs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Job {

    @SerializedName("fair_id")
    @Expose
    private Integer fairId;
    @SerializedName("recruiter_id")
    @Expose
    private Integer recruiterId;
    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("job_title")
    @Expose
    private String jobTitle;
    @SerializedName("job_type")
    @Expose
    private String jobType;
    @SerializedName("list_type")
    @Expose
    private String listType;
    @SerializedName("job_language")
    @Expose
    private String jobLanguage;
    @SerializedName("job_location")
    @Expose
    private String jobLocation;
    @SerializedName("job_contact_name")
    @Expose
    private Object jobContactName;
    @SerializedName("job_phone")
    @Expose
    private String jobPhone;
    @SerializedName("job_email")
    @Expose
    private String jobEmail;
    @SerializedName("job_url")
    @Expose
    private String jobUrl;
    @SerializedName("job_salary")
    @Expose
    private String jobSalary;
    @SerializedName("job_match")
    @Expose
    private Integer jobMatch;
    @SerializedName("job_status")
    @Expose
    private String jobStatus;
    @SerializedName("match_percentage")
    @Expose
    private String matchPercentage;
    @SerializedName("user_applied")
    @Expose
    private Boolean userApplied = false;

    @SerializedName("company_info")
    @Expose
    private CompanyInfo companyInfo;

    public String getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(String matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public Boolean getUserApplied() {
        return userApplied;
    }

    public void setUserApplied(Boolean userApplied) {
        this.userApplied = userApplied;
    }

    public Integer getFairId() {
        return fairId;
    }

    public void setFairId(Integer fairId) {
        this.fairId = fairId;
    }

    public Integer getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Integer recruiterId) {
        this.recruiterId = recruiterId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getJobLanguage() {
        return jobLanguage;
    }

    public void setJobLanguage(String jobLanguage) {
        this.jobLanguage = jobLanguage;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public Object getJobContactName() {
        return jobContactName;
    }

    public void setJobContactName(Object jobContactName) {
        this.jobContactName = jobContactName;
    }

    public String getJobPhone() {
        return jobPhone;
    }

    public void setJobPhone(String jobPhone) {
        this.jobPhone = jobPhone;
    }

    public String getJobEmail() {
        return jobEmail;
    }

    public void setJobEmail(String jobEmail) {
        this.jobEmail = jobEmail;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public String getJobSalary() {
        return jobSalary;
    }

    public void setJobSalary(String jobSalary) {
        this.jobSalary = jobSalary;
    }

    public Integer getJobMatch() {
        return jobMatch;
    }

    public void setJobMatch(Integer jobMatch) {
        this.jobMatch = jobMatch;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

}
