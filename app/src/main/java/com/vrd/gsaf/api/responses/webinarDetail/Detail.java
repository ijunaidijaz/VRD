package com.vrd.gsaf.api.responses.webinarDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vrd.gsaf.api.responses.webinars.CompanyInfo;
import com.vrd.gsaf.api.responses.webinars.UserInfo;

public class Detail {

    @SerializedName("fair_id")
    @Expose
    private Integer fairId;
    @SerializedName("company_id")
    @Expose
    private Integer companyId;
    @SerializedName("recruiter_id")
    @Expose
    private Integer recruiterId;
    @SerializedName("webinar_id")
    @Expose
    private Integer webinarId;
    @SerializedName("webinar_title")
    @Expose
    private String webinarTitle;
    @SerializedName("webinar_type")
    @Expose
    private String webinarType;
    @SerializedName("webinar_detail")
    @Expose
    private String webinarDetail;

    @SerializedName("webinar_link")
    @Expose
    private String webinarLink;

    @SerializedName("enable_chat")
    @Expose
    private String webinarChat;

    @SerializedName("user_info")
    @Expose
    private UserInfo userInfo;
    @SerializedName("company_info")
    @Expose
    private CompanyInfo companyInfo;

    public String getWebinarChat() {
        return webinarChat;
    }

    public void setWebinarChat(String webinarChat) {
        this.webinarChat = webinarChat;
    }

    public String getWebinarLink() {
        return webinarLink;
    }

    public void setWebinarLink(String webinarLink) {
        this.webinarLink = webinarLink;
    }

    public Integer getFairId() {
        return fairId;
    }

    public void setFairId(Integer fairId) {
        this.fairId = fairId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Integer recruiterId) {
        this.recruiterId = recruiterId;
    }

    public Integer getWebinarId() {
        return webinarId;
    }

    public void setWebinarId(Integer webinarId) {
        this.webinarId = webinarId;
    }

    public String getWebinarTitle() {
        return webinarTitle;
    }

    public void setWebinarTitle(String webinarTitle) {
        this.webinarTitle = webinarTitle;
    }

    public String getWebinarType() {
        return webinarType;
    }

    public void setWebinarType(String webinarType) {
        this.webinarType = webinarType;
    }

    public String getWebinarDetail() {
        return webinarDetail;
    }

    public void setWebinarDetail(String webinarDetail) {
        this.webinarDetail = webinarDetail;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }
}
