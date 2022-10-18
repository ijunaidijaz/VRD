package com.vrd.gsaf.api.responses.resetpassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("fair_id")
    @Expose
    private String fairId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("plan_password")
    @Expose
    private String planPassword;
    @SerializedName("login_requests")
    @Expose
    private String loginRequests;
    @SerializedName("block_time")
    @Expose
    private String blockTime;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("user_designation")
    @Expose
    private String userDesignation;
    @SerializedName("user_role")
    @Expose
    private String userRole;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("questionnaires_template_id")
    @Expose
    private String questionnairesTemplateId;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFairId() {
        return fairId;
    }

    public void setFairId(String fairId) {
        this.fairId = fairId;
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

    public String getPlanPassword() {
        return planPassword;
    }

    public void setPlanPassword(String planPassword) {
        this.planPassword = planPassword;
    }

    public String getLoginRequests() {
        return loginRequests;
    }

    public void setLoginRequests(String loginRequests) {
        this.loginRequests = loginRequests;
    }

    public String getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(String blockTime) {
        this.blockTime = blockTime;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserDesignation() {
        return userDesignation;
    }

    public void setUserDesignation(String userDesignation) {
        this.userDesignation = userDesignation;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuestionnairesTemplateId() {
        return questionnairesTemplateId;
    }

    public void setQuestionnairesTemplateId(String questionnairesTemplateId) {
        this.questionnairesTemplateId = questionnairesTemplateId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
