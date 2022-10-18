package com.vrd.gsaf.api.responses.schedules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InterviewSlot {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("teams_meeting")
    @Expose
    private String teamsMeeting;
    @SerializedName("teams_url")
    @Expose
    private String teamsUrl;
    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("slot_id")
    @Expose
    private String slotId;
    @SerializedName("notes")
    @Expose
    private List<Object> notes = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("recruiter_name")
    @Expose
    private String recruiterName;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("slot")
    @Expose
    private String slot;
    @SerializedName("simpleDate")
    @Expose
    private String simpleDate;
    @SerializedName("sortDate")
    @Expose
    private Integer sortDate;
    @SerializedName("invited_by")
    @Expose
    private String invitedBy;
    @SerializedName("candidate_id")
    @Expose
    private String candidateId;
    @SerializedName("recruiter_id")
    @Expose
    private String recruiterId;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("action_by")
    @Expose
    private String actionBy;
    @SerializedName("is_shortlisted")
    @Expose
    private Integer isShortlisted;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("user_cv")
    @Expose
    private String userCv;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamsMeeting() {
        return teamsMeeting;
    }

    public void setTeamsMeeting(String teamsMeeting) {
        this.teamsMeeting = teamsMeeting;
    }

    public String getTeamsUrl() {
        return teamsUrl;
    }

    public void setTeamsUrl(String teamsUrl) {
        this.teamsUrl = teamsUrl;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public List<Object> getNotes() {
        return notes;
    }

    public void setNotes(List<Object> notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getSimpleDate() {
        return simpleDate;
    }

    public void setSimpleDate(String simpleDate) {
        this.simpleDate = simpleDate;
    }

    public Integer getSortDate() {
        return sortDate;
    }

    public void setSortDate(Integer sortDate) {
        this.sortDate = sortDate;
    }

    public String getInvitedBy() {
        return invitedBy;
    }

    public void setInvitedBy(String invitedBy) {
        this.invitedBy = invitedBy;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public Integer getIsShortlisted() {
        return isShortlisted;
    }

    public void setIsShortlisted(Integer isShortlisted) {
        this.isShortlisted = isShortlisted;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserCv() {
        return userCv;
    }

    public void setUserCv(String userCv) {
        this.userCv = userCv;
    }

}
