package com.vrd.gsaf.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NetworkingTable {
    @SerializedName("auto_id")
    @Expose
    private Integer autoId;
    @SerializedName("fair_id")
    @Expose
    private Integer fairId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("table_logo")
    @Expose
    private String tableLogo;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("meeting_id")
    @Expose
    private String meetingId;
    @SerializedName("joining_link")
    @Expose
    private String joiningLink;
    @SerializedName("host_link")
    @Expose
    private String hostLink;
    @SerializedName("members_count")
    @Expose
    private String membersCount;

    public Integer getAutoId() {
        return autoId;
    }

    public void setAutoId(Integer autoId) {
        this.autoId = autoId;
    }

    public Integer getFairId() {
        return fairId;
    }

    public void setFairId(Integer fairId) {
        this.fairId = fairId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTableLogo() {
        return tableLogo;
    }

    public void setTableLogo(String tableLogo) {
        this.tableLogo = tableLogo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getJoiningLink() {
        return joiningLink;
    }

    public void setJoiningLink(String joiningLink) {
        this.joiningLink = joiningLink;
    }

    public String getHostLink() {
        return hostLink;
    }

    public void setHostLink(String hostLink) {
        this.hostLink = hostLink;
    }

    public String getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(String membersCount) {
        this.membersCount = membersCount;
    }

}
