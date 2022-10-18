package com.vrd.gsaf.api.responses.fairDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FairSetting {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("fair_id")
    @Expose
    private String fairId;
    @SerializedName("information_text")
    @Expose
    private String informationText;
    @SerializedName("offline_text")
    @Expose
    private String offlineText;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("terms_conditions")
    @Expose
    private String termsConditions;
    @SerializedName("privacy_policy")
    @Expose
    private String privacyPolicy;
    @SerializedName("fair_news")
    @Expose
    private String fairNews;
    @SerializedName("webinar_enable")
    @Expose
    private String webinarEnable;
    @SerializedName("cv_required")
    @Expose
    private String cvRequired;
    @SerializedName("interview_room")
    @Expose
    private String interviewRoom;
    @SerializedName("seminar")
    @Expose
    private String seminar;
    @SerializedName("video_chat")
    @Expose
    private String videoChat;
    @SerializedName("user_vetting")
    @Expose
    private String userVetting;
    @SerializedName("limited_access")
    @Expose
    private String limitedAccess;
    @SerializedName("chat_status")
    @Expose
    private String chatStatus;
    @SerializedName("fair_meta")
    @Expose
    private String fairMeta;
    @SerializedName("front_keywords")
    @Expose
    private String frontKeywords;
    @SerializedName("options")
    @Expose
    private String options;
    @SerializedName("speakers_enabled")
    @Expose
    private String speakersEnabled;
    @SerializedName("zoom_api_key")
    @Expose
    private String zoomApiKey;
    @SerializedName("zoom_api_secret")
    @Expose
    private String zoomApiSecret;
    @SerializedName("zoom_auth")
    @Expose
    private String zoomAuth;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("pre_fair_enter_content")
    @Expose
    private String preFairEnterContent;
    @SerializedName("fair_close_content")
    @Expose
    private String fairCloseContent;
    @SerializedName("welcome_email_content")
    @Expose
    private String welcomeEmailContent;
    @SerializedName("welcome_content_after_registration")
    @Expose
    private String welcomeContentAfterRegistration;
    @SerializedName("extra_info1")
    @Expose
    private String extraInfo1;
    @SerializedName("extra_info2")
    @Expose
    private String extraInfo2;
    @SerializedName("extra_info3")
    @Expose
    private String extraInfo3;
    @SerializedName("smart_rec_info")
    @Expose
    private String smartRecInfo;
    @SerializedName("help")
    @Expose
    private String help;
    @SerializedName("live_webinar_id")
    @Expose
    private String liveWebinarId;
    @SerializedName("live_webinar_secret")
    @Expose
    private String liveWebinarSecret;
    @SerializedName("webinar_content")
    @Expose
    private String webinarContent;
    @SerializedName("email_settings")
    @Expose
    private String emailSettings;
    @SerializedName("registration_email_template")
    @Expose
    private String registrationEmailTemplate;
    @SerializedName("block_recruiter")
    @Expose
    private String blockRecruiter;
    @SerializedName("block_companyAdmin")
    @Expose
    private String blockCompanyAdmin;
    @SerializedName("survey_enable")
    @Expose
    private String surveyEnable;
    @SerializedName("transfer_chat")
    @Expose
    private String transferChat;
    @SerializedName("representative_schedules")
    @Expose
    private String representativeSchedules;
    @SerializedName("ms_app_id")
    @Expose
    private String msAppId;
    @SerializedName("ms_app_secret")
    @Expose
    private String msAppSecret;
    @SerializedName("ms_tenant_id")
    @Expose
    private String msTenantId;
    @SerializedName("ms_options")
    @Expose
    private String msOptions;

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

    public String getInformationText() {
        return informationText;
    }

    public void setInformationText(String informationText) {
        this.informationText = informationText;
    }

    public String getOfflineText() {
        return offlineText;
    }

    public void setOfflineText(String offlineText) {
        this.offlineText = offlineText;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTermsConditions() {
        return termsConditions;
    }

    public void setTermsConditions(String termsConditions) {
        this.termsConditions = termsConditions;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public String getFairNews() {
        return fairNews;
    }

    public void setFairNews(String fairNews) {
        this.fairNews = fairNews;
    }

    public String getWebinarEnable() {
        return webinarEnable;
    }

    public void setWebinarEnable(String webinarEnable) {
        this.webinarEnable = webinarEnable;
    }

    public String getCvRequired() {
        return cvRequired;
    }

    public void setCvRequired(String cvRequired) {
        this.cvRequired = cvRequired;
    }

    public String getInterviewRoom() {
        return interviewRoom;
    }

    public void setInterviewRoom(String interviewRoom) {
        this.interviewRoom = interviewRoom;
    }

    public String getSeminar() {
        return seminar;
    }

    public void setSeminar(String seminar) {
        this.seminar = seminar;
    }

    public String getVideoChat() {
        return videoChat;
    }

    public void setVideoChat(String videoChat) {
        this.videoChat = videoChat;
    }

    public String getUserVetting() {
        return userVetting;
    }

    public void setUserVetting(String userVetting) {
        this.userVetting = userVetting;
    }

    public String getLimitedAccess() {
        return limitedAccess;
    }

    public void setLimitedAccess(String limitedAccess) {
        this.limitedAccess = limitedAccess;
    }

    public String getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(String chatStatus) {
        this.chatStatus = chatStatus;
    }

    public String getFairMeta() {
        return fairMeta;
    }

    public void setFairMeta(String fairMeta) {
        this.fairMeta = fairMeta;
    }

    public String getFrontKeywords() {
        return frontKeywords;
    }

    public void setFrontKeywords(String frontKeywords) {
        this.frontKeywords = frontKeywords;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getSpeakersEnabled() {
        return speakersEnabled;
    }

    public void setSpeakersEnabled(String speakersEnabled) {
        this.speakersEnabled = speakersEnabled;
    }

    public String getZoomApiKey() {
        return zoomApiKey;
    }

    public void setZoomApiKey(String zoomApiKey) {
        this.zoomApiKey = zoomApiKey;
    }

    public String getZoomApiSecret() {
        return zoomApiSecret;
    }

    public void setZoomApiSecret(String zoomApiSecret) {
        this.zoomApiSecret = zoomApiSecret;
    }

    public String getZoomAuth() {
        return zoomAuth;
    }

    public void setZoomAuth(String zoomAuth) {
        this.zoomAuth = zoomAuth;
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

    public String getPreFairEnterContent() {
        return preFairEnterContent;
    }

    public void setPreFairEnterContent(String preFairEnterContent) {
        this.preFairEnterContent = preFairEnterContent;
    }

    public String getFairCloseContent() {
        return fairCloseContent;
    }

    public void setFairCloseContent(String fairCloseContent) {
        this.fairCloseContent = fairCloseContent;
    }

    public String getWelcomeEmailContent() {
        return welcomeEmailContent;
    }

    public void setWelcomeEmailContent(String welcomeEmailContent) {
        this.welcomeEmailContent = welcomeEmailContent;
    }

    public String getWelcomeContentAfterRegistration() {
        return welcomeContentAfterRegistration;
    }

    public void setWelcomeContentAfterRegistration(String welcomeContentAfterRegistration) {
        this.welcomeContentAfterRegistration = welcomeContentAfterRegistration;
    }

    public String getExtraInfo1() {
        return extraInfo1;
    }

    public void setExtraInfo1(String extraInfo1) {
        this.extraInfo1 = extraInfo1;
    }

    public String getExtraInfo2() {
        return extraInfo2;
    }

    public void setExtraInfo2(String extraInfo2) {
        this.extraInfo2 = extraInfo2;
    }

    public String getExtraInfo3() {
        return extraInfo3;
    }

    public void setExtraInfo3(String extraInfo3) {
        this.extraInfo3 = extraInfo3;
    }

    public String getSmartRecInfo() {
        return smartRecInfo;
    }

    public void setSmartRecInfo(String smartRecInfo) {
        this.smartRecInfo = smartRecInfo;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getLiveWebinarId() {
        return liveWebinarId;
    }

    public void setLiveWebinarId(String liveWebinarId) {
        this.liveWebinarId = liveWebinarId;
    }

    public String getLiveWebinarSecret() {
        return liveWebinarSecret;
    }

    public void setLiveWebinarSecret(String liveWebinarSecret) {
        this.liveWebinarSecret = liveWebinarSecret;
    }

    public String getWebinarContent() {
        return webinarContent;
    }

    public void setWebinarContent(String webinarContent) {
        this.webinarContent = webinarContent;
    }

    public String getEmailSettings() {
        return emailSettings;
    }

    public void setEmailSettings(String emailSettings) {
        this.emailSettings = emailSettings;
    }

    public String getRegistrationEmailTemplate() {
        return registrationEmailTemplate;
    }

    public void setRegistrationEmailTemplate(String registrationEmailTemplate) {
        this.registrationEmailTemplate = registrationEmailTemplate;
    }

    public String getBlockRecruiter() {
        return blockRecruiter;
    }

    public void setBlockRecruiter(String blockRecruiter) {
        this.blockRecruiter = blockRecruiter;
    }

    public String getBlockCompanyAdmin() {
        return blockCompanyAdmin;
    }

    public void setBlockCompanyAdmin(String blockCompanyAdmin) {
        this.blockCompanyAdmin = blockCompanyAdmin;
    }

    public String getSurveyEnable() {
        return surveyEnable;
    }

    public void setSurveyEnable(String surveyEnable) {
        this.surveyEnable = surveyEnable;
    }

    public String getTransferChat() {
        return transferChat;
    }

    public void setTransferChat(String transferChat) {
        this.transferChat = transferChat;
    }

    public String getRepresentativeSchedules() {
        return representativeSchedules;
    }

    public void setRepresentativeSchedules(String representativeSchedules) {
        this.representativeSchedules = representativeSchedules;
    }

    public String getMsAppId() {
        return msAppId;
    }

    public void setMsAppId(String msAppId) {
        this.msAppId = msAppId;
    }

    public String getMsAppSecret() {
        return msAppSecret;
    }

    public void setMsAppSecret(String msAppSecret) {
        this.msAppSecret = msAppSecret;
    }

    public String getMsTenantId() {
        return msTenantId;
    }

    public void setMsTenantId(String msTenantId) {
        this.msTenantId = msTenantId;
    }

    public String getMsOptions() {
        return msOptions;
    }

    public void setMsOptions(String msOptions) {
        this.msOptions = msOptions;
    }

}
