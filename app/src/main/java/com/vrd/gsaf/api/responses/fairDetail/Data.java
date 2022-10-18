package com.vrd.gsaf.api.responses.fairDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("fair")
    @Expose
    private Fair fair;
    @SerializedName("fairFields")
    @Expose
    private List<FairField> fairFields = null;
    @SerializedName("dateAndTime")
    @Expose
    private DateAndTime dateAndTime;
    @SerializedName("isTakeTest")
    @Expose
    private Boolean isTakeTest;
    @SerializedName("addedWebinars")
    @Expose
    private List<AddedWebinar> addedWebinars = null;
    @SerializedName("fairStages")
    @Expose
    private List<FairStage> fairStages = null;
    @SerializedName("isCandidateExist")
    @Expose
    private Boolean isCandidateExist;
    @SerializedName("fairCandidateGoodiesCount")
    @Expose
    private Integer fairCandidateGoodiesCount;
    //    @SerializedName("orReceptionists")
//    @Expose
//    private List<String> orReceptionists = null;
    @SerializedName("announcements")
    @Expose
    private List<String> announcements = null;
    @SerializedName("isTakeSurvey")
    @Expose
    private Boolean isTakeSurvey;

    @SerializedName("system_url")
    @Expose
    private SystemUrl systemUrl;
    @SerializedName("chatApiDetail")
    @Expose
    private ChatApiDetails chatApiDetails;

    public SystemUrl getSystemUrl() {
        return systemUrl;
    }

    public void setSystemUrl(SystemUrl systemUrl) {
        this.systemUrl = systemUrl;
    }

    public ChatApiDetails getChatApiDetails() {
        return chatApiDetails;
    }

    public void setChatApiDetails(ChatApiDetails chatApiDetails) {
        this.chatApiDetails = chatApiDetails;
    }

    public Fair getFair() {
        return fair;
    }

    public void setFair(Fair fair) {
        this.fair = fair;
    }

    public List<FairField> getFairFields() {
        return fairFields;
    }

    public void setFairFields(List<FairField> fairFields) {
        this.fairFields = fairFields;
    }

    public DateAndTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(DateAndTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Boolean getIsTakeTest() {
        return isTakeTest;
    }

    public void setIsTakeTest(Boolean isTakeTest) {
        this.isTakeTest = isTakeTest;
    }

    public List<AddedWebinar> getAddedWebinars() {
        return addedWebinars;
    }

    public void setAddedWebinars(List<AddedWebinar> addedWebinars) {
        this.addedWebinars = addedWebinars;
    }

    public List<FairStage> getFairStages() {
        return fairStages;
    }

    public void setFairStages(List<FairStage> fairStages) {
        this.fairStages = fairStages;
    }

    public Boolean getIsCandidateExist() {
        return isCandidateExist;
    }

    public void setIsCandidateExist(Boolean isCandidateExist) {
        this.isCandidateExist = isCandidateExist;
    }

    public Integer getFairCandidateGoodiesCount() {
        return fairCandidateGoodiesCount;
    }

    public void setFairCandidateGoodiesCount(Integer fairCandidateGoodiesCount) {
        this.fairCandidateGoodiesCount = fairCandidateGoodiesCount;
    }

//    public List<String> getOrReceptionists() {
//        return orReceptionists;
//    }
//
//    public void setOrReceptionists(List<String> orReceptionists) {
//        this.orReceptionists = orReceptionists;
//    }

    public List<String> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<String> announcements) {
        this.announcements = announcements;
    }

    public Boolean getIsTakeSurvey() {
        return isTakeSurvey;
    }

    public void setIsTakeSurvey(Boolean isTakeSurvey) {
        this.isTakeSurvey = isTakeSurvey;
    }

}
