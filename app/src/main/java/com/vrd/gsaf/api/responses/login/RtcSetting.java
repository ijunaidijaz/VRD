package com.vrd.gsaf.api.responses.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RtcSetting {

    @SerializedName("auto_id")
    @Expose
    private String autoId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("fair_id")
    @Expose
    private String fairId;
    @SerializedName("sendbird_avatar")
    @Expose
    private String sendbirdAvatar;
    @SerializedName("sendbird_user_id")
    @Expose
    private String sendbirdUserId;
    @SerializedName("sendbird_access_token")
    @Expose
    private String sendbirdAccessToken;
    @SerializedName("sendbird_session_token")
    @Expose
    private String sendbirdSessionToken;
    @SerializedName("sendbird_session_expiry")
    @Expose
    private String sendbirdSessionExpiry;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFairId() {
        return fairId;
    }

    public void setFairId(String fairId) {
        this.fairId = fairId;
    }

    public String getSendbirdAvatar() {
        return sendbirdAvatar;
    }

    public void setSendbirdAvatar(String sendbirdAvatar) {
        this.sendbirdAvatar = sendbirdAvatar;
    }

    public String getSendbirdUserId() {
        return sendbirdUserId;
    }

    public void setSendbirdUserId(String sendbirdUserId) {
        this.sendbirdUserId = sendbirdUserId;
    }

    public String getSendbirdAccessToken() {
        return sendbirdAccessToken;
    }

    public void setSendbirdAccessToken(String sendbirdAccessToken) {
        this.sendbirdAccessToken = sendbirdAccessToken;
    }

    public String getSendbirdSessionToken() {
        return sendbirdSessionToken;
    }

    public void setSendbirdSessionToken(String sendbirdSessionToken) {
        this.sendbirdSessionToken = sendbirdSessionToken;
    }

    public String getSendbirdSessionExpiry() {
        return sendbirdSessionExpiry;
    }

    public void setSendbirdSessionExpiry(String sendbirdSessionExpiry) {
        this.sendbirdSessionExpiry = sendbirdSessionExpiry;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
