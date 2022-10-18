package com.vrd.gsaf.api.responses.webinars;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Webinar {

    @SerializedName("fair_id")
    @Expose
    private Integer fairId;
    @SerializedName("recruiter_id")
    @Expose
    private Integer recruiterId;
    @SerializedName("recruiter_img")
    @Expose
    private String recruiterImg;
    @SerializedName("stage_id")
    @Expose
    private Integer stageId;
    @SerializedName("main_stage")
    @Expose
    private Integer mainStage;
    @SerializedName("webinar_id")
    @Expose
    private Integer webinarId;
    @SerializedName("webinar_title")
    @Expose
    private String webinarTitle;
    @SerializedName("final_join_link")
    @Expose
    private String finalJoinLink;
    @SerializedName("webinar_type")
    @Expose
    private String webinarType;
    @SerializedName("stage_name")
    @Expose
    private String stageName;
    @SerializedName("webinar_status")
    @Expose
    private String webinar_status;
    @SerializedName("webinar_speaker")
    @Expose
    private Integer webinarSpeaker;
    @SerializedName("webinar_link")
    @Expose
    private String webinarLink;
    @SerializedName("webinar_percentage")
    @Expose
    private String webinarPercentage;
    @SerializedName("webinar_join_url")
    @Expose
    private String webinarJoinUrl;
    @SerializedName("webinar_start_time")
    @Expose
    private String webinarStartTime;
    @SerializedName("webinar_end_time")
    @Expose
    private String webinarEndTime;
    @SerializedName("webinar_join")
    @Expose
    private Boolean webinarJoin;
    @SerializedName("webinar_video_type")
    @Expose
    private String webinarVideoType;
    @SerializedName("speaker_name")
    @Expose
    private String speakerName;
    @SerializedName("multiple_speakers_count")
    @Expose
    private Integer multipleSpeakersCount;
    @SerializedName("multiple_speakers")
    @Expose
    private Boolean multipleSpeakers;
    @SerializedName("user_info")
    @Expose
    private UserInfo userInfo;
    @SerializedName("company_info")
    @Expose
    private CompanyInfo companyInfo;

    public String getFinalJoinLink() {
        return finalJoinLink;
    }

    public void setFinalJoinLink(String finalJoinLink) {
        this.finalJoinLink = finalJoinLink;
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

    public String getRecruiterImg() {
        return recruiterImg;
    }

    public void setRecruiterImg(String recruiterImg) {
        this.recruiterImg = recruiterImg;
    }

    public Integer getStageId() {
        return stageId;
    }

    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    public Integer getMainStage() {
        return mainStage;
    }

    public void setMainStage(Integer mainStage) {
        this.mainStage = mainStage;
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

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Integer getWebinarSpeaker() {
        return webinarSpeaker;
    }

    public void setWebinarSpeaker(Integer webinarSpeaker) {
        this.webinarSpeaker = webinarSpeaker;
    }

    public String getWebinarLink() {
        return webinarLink;
    }

    public void setWebinarLink(String webinarLink) {
        this.webinarLink = webinarLink;
    }

    public String getWebinarPercentage() {
        return webinarPercentage;
    }

    public void setWebinarPercentage(String webinarPercentage) {
        this.webinarPercentage = webinarPercentage;
    }

    public String getWebinarJoinUrl() {
        return webinarJoinUrl;
    }

    public void setWebinarJoinUrl(String webinarJoinUrl) {
        this.webinarJoinUrl = webinarJoinUrl;
    }

    public String getWebinarStartTime() {
        return webinarStartTime;
    }

    public void setWebinarStartTime(String webinarStartTime) {
        this.webinarStartTime = webinarStartTime;
    }

    public String getWebinar_status() {
        return webinar_status;
    }

    public void setWebinar_status(String webinar_status) {
        this.webinar_status = webinar_status;
    }

    public String getWebinarEndTime() {
        return webinarEndTime;
    }

    public void setWebinarEndTime(String webinarEndTime) {
        this.webinarEndTime = webinarEndTime;
    }

    public Boolean getWebinarJoin() {
        return webinarJoin;
    }

    public void setWebinarJoin(Boolean webinarJoin) {
        this.webinarJoin = webinarJoin;
    }

    public String getWebinarVideoType() {
        return webinarVideoType;
    }

    public void setWebinarVideoType(String webinarVideoType) {
        this.webinarVideoType = webinarVideoType;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public Integer getMultipleSpeakersCount() {
        return multipleSpeakersCount;
    }

    public void setMultipleSpeakersCount(Integer multipleSpeakersCount) {
        this.multipleSpeakersCount = multipleSpeakersCount;
    }

    public Boolean getMultipleSpeakers() {
        return multipleSpeakers;
    }

    public void setMultipleSpeakers(Boolean multipleSpeakers) {
        this.multipleSpeakers = multipleSpeakers;
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
