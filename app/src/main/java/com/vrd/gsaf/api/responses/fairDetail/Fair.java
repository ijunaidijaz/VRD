package com.vrd.gsaf.api.responses.fairDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Fair {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("reseller_id")
    @Expose
    private String resellerId;
    @SerializedName("organiser_id")
    @Expose
    private String organiserId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("short_name")
    @Expose
    private String shortName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("fair_image")
    @Expose
    private String fairImage;
    @SerializedName("fair_video")
    @Expose
    private String fairVideo;
    @SerializedName("fair_mobile_image")
    @Expose
    private String fairMobileImage;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("register_time")
    @Expose
    private String registerTime;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("fair_type")
    @Expose
    private String fairType;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("youtube")
    @Expose
    private String youtube;
    @SerializedName("twitter")
    @Expose
    private String twitter;
    @SerializedName("linkedin")
    @Expose
    private String linkedin;
    @SerializedName("instagram")
    @Expose
    private String instagram;
    @SerializedName("xing")
    @Expose
    private String xing;
    @SerializedName("fair_status")
    @Expose
    private String fairStatus;
    @SerializedName("live")
    @Expose
    private String live;
    @SerializedName("chat_status")
    @Expose
    private String chatStatus;
    @SerializedName("layout")
    @Expose
    private String layout;
    @SerializedName("presenter")
    @Expose
    private String presenter;
    @SerializedName("stand_receptionist")
    @Expose
    private String standReceptionist;
    @SerializedName("total_hall")
    @Expose
    private String totalHall;

    @SerializedName("survey_result_popup")
    @Expose
    private String surveyResultPopUp;


    @SerializedName("back_scheduling")
    @Expose
    private String backScheduling;
    @SerializedName("scheduling_plugin")
    @Expose
    private String schedulingPlugin;
    @SerializedName("front_scheduling")
    @Expose
    private String frontScheduling;
    @SerializedName("event_stage")
    @Expose
    private String eventStage;
    @SerializedName("webinar_plugin")
    @Expose
    private String webinarPlugin;
    @SerializedName("fair_smtp")
    @Expose
    private String fairSmtp;
    @SerializedName("sso_enabled")
    @Expose
    private String ssoEnabled;
    @SerializedName("goodies")
    @Expose
    private String goodies;
    @SerializedName("match_algorithm")
    @Expose
    private String matchAlgorithm;
    @SerializedName("reception_vid_link")
    @Expose
    private String receptionVidLink;
    @SerializedName("vanity_url")
    @Expose
    private String vanityUrl;
    @SerializedName("closing_info")
    @Expose
    private String closingInfo;
    @SerializedName("encriched_text_editor")
    @Expose
    private String encrichedTextEditor;
    @SerializedName("deleted_by")
    @Expose
    private String deletedBy;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("credit_status")
    @Expose
    private String creditStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("ms_app_id")
    @Expose
    private String msAppId;
    @SerializedName("ms_app_secret")
    @Expose
    private String msAppSecret;
    @SerializedName("ms_tenant_id")
    @Expose
    private String msTenantId;
    @SerializedName("fair_duration")
    @Expose
    private String fairDuration;
    @SerializedName("live_start_time")
    @Expose
    private String liveStartTime;
    @SerializedName("live_end_time")
    @Expose
    private String liveEndTime;
    @SerializedName("yearly_credit")
    @Expose
    private String yearlyCredit;
    @SerializedName("live_credit")
    @Expose
    private String liveCredit;
    @SerializedName("fair_languages")
    @Expose
    private List<FairLanguage> fairLanguages = null;
    @SerializedName("default_language")
    @Expose
    private DefaultLanguage defaultLanguage;
    @SerializedName("fair_translations")
    @Expose
    private List<FairTranslation> fairTranslations = null;
    @SerializedName("options")
    @Expose
    private Options options;
    @SerializedName("auto_arrange_stands")
    @Expose
    private Boolean autoArrangeStands;
    @SerializedName("show_halls_with_match_stands")
    @Expose
    private Boolean showHallsWithMatchStands;
    @SerializedName("fair_total_halls")
    @Expose
    private Integer fairTotalHalls;
    @SerializedName("fair_halls_data")
    @Expose
    private List<FairHallsDatum> fairHallsData = null;
    @SerializedName("organiser_name")
    @Expose
    private String organiserName;
    @SerializedName("organiser_image")
    @Expose
    private String organiserImage;
    @SerializedName("pre_fair_enter_content")
    @Expose
    private String preFairEnterContent;
    @SerializedName("w_c_a_registration")
    @Expose
    private String wCARegistration;
    @SerializedName("keywords")
    @Expose
    private String keywords;
    @SerializedName("fairTiming")
    @Expose
    private FairTiming fairTiming;
    @SerializedName("fair_setting")
    @Expose
    private FairSetting fairSetting;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResellerId() {
        return resellerId;
    }

    public void setResellerId(String resellerId) {
        this.resellerId = resellerId;
    }

    public String getOrganiserId() {
        return organiserId;
    }

    public void setOrganiserId(String organiserId) {
        this.organiserId = organiserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFairImage() {
        return fairImage;
    }

    public void setFairImage(String fairImage) {
        this.fairImage = fairImage;
    }

    public String getFairVideo() {
        return fairVideo;
    }

    public void setFairVideo(String fairVideo) {
        this.fairVideo = fairVideo;
    }

    public String getFairMobileImage() {
        return fairMobileImage;
    }

    public void setFairMobileImage(String fairMobileImage) {
        this.fairMobileImage = fairMobileImage;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
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

    public String getFairType() {
        return fairType;
    }

    public void setFairType(String fairType) {
        this.fairType = fairType;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getXing() {
        return xing;
    }

    public void setXing(String xing) {
        this.xing = xing;
    }

    public String getFairStatus() {
        return fairStatus;
    }

    public void setFairStatus(String fairStatus) {
        this.fairStatus = fairStatus;
    }

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public String getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(String chatStatus) {
        this.chatStatus = chatStatus;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getPresenter() {
        return presenter;
    }

    public void setPresenter(String presenter) {
        this.presenter = presenter;
    }

    public String getStandReceptionist() {
        return standReceptionist;
    }

    public void setStandReceptionist(String standReceptionist) {
        this.standReceptionist = standReceptionist;
    }

    public String getTotalHall() {
        return totalHall;
    }

    public void setTotalHall(String totalHall) {
        this.totalHall = totalHall;
    }

    public String getBackScheduling() {
        return backScheduling;
    }

    public void setBackScheduling(String backScheduling) {
        this.backScheduling = backScheduling;
    }

    public String getSchedulingPlugin() {
        return schedulingPlugin;
    }

    public void setSchedulingPlugin(String schedulingPlugin) {
        this.schedulingPlugin = schedulingPlugin;
    }

    public String getFrontScheduling() {
        return frontScheduling;
    }

    public void setFrontScheduling(String frontScheduling) {
        this.frontScheduling = frontScheduling;
    }

    public String getEventStage() {
        return eventStage;
    }

    public void setEventStage(String eventStage) {
        this.eventStage = eventStage;
    }

    public String getWebinarPlugin() {
        return webinarPlugin;
    }

    public void setWebinarPlugin(String webinarPlugin) {
        this.webinarPlugin = webinarPlugin;
    }

    public String getFairSmtp() {
        return fairSmtp;
    }

    public void setFairSmtp(String fairSmtp) {
        this.fairSmtp = fairSmtp;
    }

    public String getSsoEnabled() {
        return ssoEnabled;
    }

    public void setSsoEnabled(String ssoEnabled) {
        this.ssoEnabled = ssoEnabled;
    }

    public String getGoodies() {
        return goodies;
    }

    public void setGoodies(String goodies) {
        this.goodies = goodies;
    }

    public String getMatchAlgorithm() {
        return matchAlgorithm;
    }

    public void setMatchAlgorithm(String matchAlgorithm) {
        this.matchAlgorithm = matchAlgorithm;
    }

    public String getReceptionVidLink() {
        return receptionVidLink;
    }

    public void setReceptionVidLink(String receptionVidLink) {
        this.receptionVidLink = receptionVidLink;
    }

    public String getVanityUrl() {
        return vanityUrl;
    }

    public void setVanityUrl(String vanityUrl) {
        this.vanityUrl = vanityUrl;
    }

    public String getClosingInfo() {
        return closingInfo;
    }

    public void setClosingInfo(String closingInfo) {
        this.closingInfo = closingInfo;
    }

    public String getEncrichedTextEditor() {
        return encrichedTextEditor;
    }

    public void setEncrichedTextEditor(String encrichedTextEditor) {
        this.encrichedTextEditor = encrichedTextEditor;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
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

    public String getFairDuration() {
        return fairDuration;
    }

    public void setFairDuration(String fairDuration) {
        this.fairDuration = fairDuration;
    }

    public String getLiveStartTime() {
        return liveStartTime;
    }

    public void setLiveStartTime(String liveStartTime) {
        this.liveStartTime = liveStartTime;
    }

    public String getLiveEndTime() {
        return liveEndTime;
    }

    public void setLiveEndTime(String liveEndTime) {
        this.liveEndTime = liveEndTime;
    }

    public String getYearlyCredit() {
        return yearlyCredit;
    }

    public void setYearlyCredit(String yearlyCredit) {
        this.yearlyCredit = yearlyCredit;
    }

    public String getLiveCredit() {
        return liveCredit;
    }

    public void setLiveCredit(String liveCredit) {
        this.liveCredit = liveCredit;
    }

    public List<FairLanguage> getFairLanguages() {
        return fairLanguages;
    }

    public void setFairLanguages(List<FairLanguage> fairLanguages) {
        this.fairLanguages = fairLanguages;
    }

    public DefaultLanguage getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(DefaultLanguage defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public List<FairTranslation> getFairTranslations() {
        return fairTranslations;
    }

    public void setFairTranslations(List<FairTranslation> fairTranslations) {
        this.fairTranslations = fairTranslations;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public Boolean getAutoArrangeStands() {
        return autoArrangeStands;
    }

    public void setAutoArrangeStands(Boolean autoArrangeStands) {
        this.autoArrangeStands = autoArrangeStands;
    }

    public Boolean getShowHallsWithMatchStands() {
        return showHallsWithMatchStands;
    }

    public void setShowHallsWithMatchStands(Boolean showHallsWithMatchStands) {
        this.showHallsWithMatchStands = showHallsWithMatchStands;
    }

    public Integer getFairTotalHalls() {
        return fairTotalHalls;
    }

    public void setFairTotalHalls(Integer fairTotalHalls) {
        this.fairTotalHalls = fairTotalHalls;
    }

    public List<FairHallsDatum> getFairHallsData() {
        return fairHallsData;
    }

    public void setFairHallsData(List<FairHallsDatum> fairHallsData) {
        this.fairHallsData = fairHallsData;
    }

    public String getOrganiserName() {
        return organiserName;
    }

    public void setOrganiserName(String organiserName) {
        this.organiserName = organiserName;
    }

    public String getOrganiserImage() {
        return organiserImage;
    }

    public void setOrganiserImage(String organiserImage) {
        this.organiserImage = organiserImage;
    }

    public String getPreFairEnterContent() {
        return preFairEnterContent;
    }

    public void setPreFairEnterContent(String preFairEnterContent) {
        this.preFairEnterContent = preFairEnterContent;
    }

    public String getwCARegistration() {
        return wCARegistration;
    }

    public void setwCARegistration(String wCARegistration) {
        this.wCARegistration = wCARegistration;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public FairTiming getFairTiming() {
        return fairTiming;
    }

    public void setFairTiming(FairTiming fairTiming) {
        this.fairTiming = fairTiming;
    }

    public FairSetting getFairSetting() {
        return fairSetting;
    }

    public void setFairSetting(FairSetting fairSetting) {
        this.fairSetting = fairSetting;
    }

    public String getSurveyResultPopUp() {
        return surveyResultPopUp;
    }

    public void setSurveyResultPopUp(String surveyResultPopUp) {
        this.surveyResultPopUp = surveyResultPopUp;
    }
}
