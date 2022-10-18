package com.vrd.gsaf.api.responses.fairDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddedWebinar {

    @SerializedName("fair_id")
    @Expose
    private Integer fairId;
    @SerializedName("speaker")
    @Expose
    private String speaker;
    @SerializedName("main_stage")
    @Expose
    private Integer mainStage;
    @SerializedName("stage_id")
    @Expose
    private Integer stageId;
    @SerializedName("company_id")
    @Expose
    private String companyId;
    @SerializedName("webinar_id")
    @Expose
    private String webinarId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("session_logo")
    @Expose
    private String sessionLogo;
    @SerializedName("percentage")
    @Expose
    private String percentage;
    @SerializedName("join_url")
    @Expose
    private String joinUrl;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("recruiter_img")
    @Expose
    private String recruiterImg;
    @SerializedName("company_logo")
    @Expose
    private String companyLogo;
    @SerializedName("stage_name")
    @Expose
    private String stageName;
    @SerializedName("speaker_name")
    @Expose
    private String speakerName;
    @SerializedName("join")
    @Expose
    private Boolean join;
    @SerializedName("video_type")
    @Expose
    private String videoType;

    public Integer getFairId() {
        return fairId;
    }

    public void setFairId(Integer fairId) {
        this.fairId = fairId;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public Integer getMainStage() {
        return mainStage;
    }

    public void setMainStage(Integer mainStage) {
        this.mainStage = mainStage;
    }

    public Integer getStageId() {
        return stageId;
    }

    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getWebinarId() {
        return webinarId;
    }

    public void setWebinarId(String webinarId) {
        this.webinarId = webinarId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSessionLogo() {
        return sessionLogo;
    }

    public void setSessionLogo(String sessionLogo) {
        this.sessionLogo = sessionLogo;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getJoinUrl() {
        return joinUrl;
    }

    public void setJoinUrl(String joinUrl) {
        this.joinUrl = joinUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRecruiterImg() {
        return recruiterImg;
    }

    public void setRecruiterImg(String recruiterImg) {
        this.recruiterImg = recruiterImg;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public Boolean getJoin() {
        return join;
    }

    public void setJoin(Boolean join) {
        this.join = join;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

}
