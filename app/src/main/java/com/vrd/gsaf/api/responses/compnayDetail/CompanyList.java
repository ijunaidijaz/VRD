package com.vrd.gsaf.api.responses.compnayDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("admin_id")
    @Expose
    private String adminId;
    @SerializedName("fair_id")
    @Expose
    private String fairId;
    @SerializedName("recruiter_id")
    @Expose
    private String recruiterId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("company_email")
    @Expose
    private String companyEmail;
    @SerializedName("company_post_code")
    @Expose
    private String companyPostCode;
    @SerializedName("company_state")
    @Expose
    private String companyState;
    @SerializedName("company_country")
    @Expose
    private String companyCountry;
    @SerializedName("company_match")
    @Expose
    private String companyMatch;
    @SerializedName("company_web_url")
    @Expose
    private String companyWebUrl;
    @SerializedName("company_facebook_url")
    @Expose
    private String companyFacebookUrl;
    @SerializedName("company_youtube_url")
    @Expose
    private String companyYoutubeUrl;
    @SerializedName("company_twitter_url")
    @Expose
    private String companyTwitterUrl;
    @SerializedName("company_in_url")
    @Expose
    private String companyInUrl;
    @SerializedName("company_instagram_url")
    @Expose
    private String companyInstagramUrl;
    @SerializedName("company_stand_type")
    @Expose
    private String companyStandType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("description2")
    @Expose
    private Object description2;
    @SerializedName("description3")
    @Expose
    private Object description3;
    @SerializedName("description4")
    @Expose
    private Object description4;
    @SerializedName("description5")
    @Expose
    private Object description5;
    @SerializedName("description6")
    @Expose
    private Object description6;
    @SerializedName("company_logo")
    @Expose
    private String companyLogo;
    @SerializedName("company_stand_image")
    @Expose
    private String companyStandImage;
    @SerializedName("company_stand_banner")
    @Expose
    private String companyStandBanner;
    @SerializedName("dashboard_thumbnail")
    @Expose
    private Object dashboardThumbnail;
    @SerializedName("company_hall")
    @Expose
    private String companyHall;
    @SerializedName("questionnaire_selected")
    @Expose
    private String questionnaireSelected;
    @SerializedName("enable_recruiters")
    @Expose
    private String enableRecruiters;
    @SerializedName("enable_webinars")
    @Expose
    private String enableWebinars;
    @SerializedName("enable_goodies")
    @Expose
    private String enableGoodies;
    @SerializedName("enable_media")
    @Expose
    private String enableMedia;
    @SerializedName("enable_jobs")
    @Expose
    private String enableJobs;
    @SerializedName("enable_courses")
    @Expose
    private String enableCourses;
    @SerializedName("enable_documents")
    @Expose
    private String enableDocuments;
    @SerializedName("enable_about")
    @Expose
    private String enableAbout;
    @SerializedName("enable_auto_enrolled")
    @Expose
    private String enableAutoEnrolled;
    @SerializedName("enable_poll")
    @Expose
    private String enablePoll;
    @SerializedName("enable_matching_slots")
    @Expose
    private String enableMatchingSlots;
    @SerializedName("enable_virtual_tour")
    @Expose
    private String enableVirtualTour;
    @SerializedName("enable_description2")
    @Expose
    private String enableDescription2;
    @SerializedName("enable_description3")
    @Expose
    private String enableDescription3;
    @SerializedName("enable_description4")
    @Expose
    private String enableDescription4;
    @SerializedName("enable_description5")
    @Expose
    private String enableDescription5;
    @SerializedName("enable_description6")
    @Expose
    private String enableDescription6;
    @SerializedName("display_order")
    @Expose
    private String displayOrder;
    @SerializedName("stand_options")
    @Expose
    private String standOptions;
    @SerializedName("stand_background")
    @Expose
    private Object standBackground;
    @SerializedName("show_on_front")
    @Expose
    private String showOnFront;
    @SerializedName("standBackground")
    @Expose
    private String standBackgroundImage;
    @SerializedName("recruiters_limit")
    @Expose
    private Object recruitersLimit;
    @SerializedName("company_admins_limit")
    @Expose
    private Object companyAdminsLimit;
    @SerializedName("company_media_limit")
    @Expose
    private Object companyMediaLimit;
    @SerializedName("company_documents_limit")
    @Expose
    private Object companyDocumentsLimit;
    @SerializedName("company_webinars_limit")
    @Expose
    private Object companyWebinarsLimit;
    @SerializedName("disable_company_chat")
    @Expose
    private String disableCompanyChat;
    @SerializedName("disable_company_salary")
    @Expose
    private String disableCompanySalary;
    @SerializedName("latitude")
    @Expose
    private Object latitude;
    @SerializedName("longitude")
    @Expose
    private Object longitude;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("companyMedia")
    @Expose
    private List<CompanyMedium> companyMedia = null;
    @SerializedName("standBackgroundUrl")
    @Expose
    private String standBackgroundUrl = null;

    public String getStandBackgroundUrl() {
        return standBackgroundUrl;
    }

    public void setStandBackgroundUrl(String standBackgroundUrl) {
        this.standBackgroundUrl = standBackgroundUrl;
    }

    public String getStandBackgroundImage() {
        return standBackgroundImage;
    }

    public void setStandBackgroundImage(String standBackgroundImage) {
        this.standBackgroundImage = standBackgroundImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getFairId() {
        return fairId;
    }

    public void setFairId(String fairId) {
        this.fairId = fairId;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyPostCode() {
        return companyPostCode;
    }

    public void setCompanyPostCode(String companyPostCode) {
        this.companyPostCode = companyPostCode;
    }

    public String getCompanyState() {
        return companyState;
    }

    public void setCompanyState(String companyState) {
        this.companyState = companyState;
    }

    public String getCompanyCountry() {
        return companyCountry;
    }

    public void setCompanyCountry(String companyCountry) {
        this.companyCountry = companyCountry;
    }

    public String getCompanyMatch() {
        return companyMatch;
    }

    public void setCompanyMatch(String companyMatch) {
        this.companyMatch = companyMatch;
    }

    public String getCompanyWebUrl() {
        return companyWebUrl;
    }

    public void setCompanyWebUrl(String companyWebUrl) {
        this.companyWebUrl = companyWebUrl;
    }

    public String getCompanyFacebookUrl() {
        return companyFacebookUrl;
    }

    public void setCompanyFacebookUrl(String companyFacebookUrl) {
        this.companyFacebookUrl = companyFacebookUrl;
    }

    public String getCompanyYoutubeUrl() {
        return companyYoutubeUrl;
    }

    public void setCompanyYoutubeUrl(String companyYoutubeUrl) {
        this.companyYoutubeUrl = companyYoutubeUrl;
    }

    public String getCompanyTwitterUrl() {
        return companyTwitterUrl;
    }

    public void setCompanyTwitterUrl(String companyTwitterUrl) {
        this.companyTwitterUrl = companyTwitterUrl;
    }

    public String getCompanyInUrl() {
        return companyInUrl;
    }

    public void setCompanyInUrl(String companyInUrl) {
        this.companyInUrl = companyInUrl;
    }

    public String getCompanyInstagramUrl() {
        return companyInstagramUrl;
    }

    public void setCompanyInstagramUrl(String companyInstagramUrl) {
        this.companyInstagramUrl = companyInstagramUrl;
    }

    public String getCompanyStandType() {
        return companyStandType;
    }

    public void setCompanyStandType(String companyStandType) {
        this.companyStandType = companyStandType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getDescription2() {
        return description2;
    }

    public void setDescription2(Object description2) {
        this.description2 = description2;
    }

    public Object getDescription3() {
        return description3;
    }

    public void setDescription3(Object description3) {
        this.description3 = description3;
    }

    public Object getDescription4() {
        return description4;
    }

    public void setDescription4(Object description4) {
        this.description4 = description4;
    }

    public Object getDescription5() {
        return description5;
    }

    public void setDescription5(Object description5) {
        this.description5 = description5;
    }

    public Object getDescription6() {
        return description6;
    }

    public void setDescription6(Object description6) {
        this.description6 = description6;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyStandImage() {
        return companyStandImage;
    }

    public void setCompanyStandImage(String companyStandImage) {
        this.companyStandImage = companyStandImage;
    }

    public String getCompanyStandBanner() {
        return companyStandBanner;
    }

    public void setCompanyStandBanner(String companyStandBanner) {
        this.companyStandBanner = companyStandBanner;
    }

    public Object getDashboardThumbnail() {
        return dashboardThumbnail;
    }

    public void setDashboardThumbnail(Object dashboardThumbnail) {
        this.dashboardThumbnail = dashboardThumbnail;
    }

    public String getCompanyHall() {
        return companyHall;
    }

    public void setCompanyHall(String companyHall) {
        this.companyHall = companyHall;
    }

    public String getQuestionnaireSelected() {
        return questionnaireSelected;
    }

    public void setQuestionnaireSelected(String questionnaireSelected) {
        this.questionnaireSelected = questionnaireSelected;
    }

    public String getEnableRecruiters() {
        return enableRecruiters;
    }

    public void setEnableRecruiters(String enableRecruiters) {
        this.enableRecruiters = enableRecruiters;
    }

    public String getEnableWebinars() {
        return enableWebinars;
    }

    public void setEnableWebinars(String enableWebinars) {
        this.enableWebinars = enableWebinars;
    }

    public String getEnableGoodies() {
        return enableGoodies;
    }

    public void setEnableGoodies(String enableGoodies) {
        this.enableGoodies = enableGoodies;
    }

    public String getEnableMedia() {
        return enableMedia;
    }

    public void setEnableMedia(String enableMedia) {
        this.enableMedia = enableMedia;
    }

    public String getEnableJobs() {
        return enableJobs;
    }

    public void setEnableJobs(String enableJobs) {
        this.enableJobs = enableJobs;
    }

    public String getEnableCourses() {
        return enableCourses;
    }

    public void setEnableCourses(String enableCourses) {
        this.enableCourses = enableCourses;
    }

    public String getEnableDocuments() {
        return enableDocuments;
    }

    public void setEnableDocuments(String enableDocuments) {
        this.enableDocuments = enableDocuments;
    }

    public String getEnableAbout() {
        return enableAbout;
    }

    public void setEnableAbout(String enableAbout) {
        this.enableAbout = enableAbout;
    }

    public String getEnableAutoEnrolled() {
        return enableAutoEnrolled;
    }

    public void setEnableAutoEnrolled(String enableAutoEnrolled) {
        this.enableAutoEnrolled = enableAutoEnrolled;
    }

    public String getEnablePoll() {
        return enablePoll;
    }

    public void setEnablePoll(String enablePoll) {
        this.enablePoll = enablePoll;
    }

    public String getEnableMatchingSlots() {
        return enableMatchingSlots;
    }

    public void setEnableMatchingSlots(String enableMatchingSlots) {
        this.enableMatchingSlots = enableMatchingSlots;
    }

    public String getEnableVirtualTour() {
        return enableVirtualTour;
    }

    public void setEnableVirtualTour(String enableVirtualTour) {
        this.enableVirtualTour = enableVirtualTour;
    }

    public String getEnableDescription2() {
        return enableDescription2;
    }

    public void setEnableDescription2(String enableDescription2) {
        this.enableDescription2 = enableDescription2;
    }

    public String getEnableDescription3() {
        return enableDescription3;
    }

    public void setEnableDescription3(String enableDescription3) {
        this.enableDescription3 = enableDescription3;
    }

    public String getEnableDescription4() {
        return enableDescription4;
    }

    public void setEnableDescription4(String enableDescription4) {
        this.enableDescription4 = enableDescription4;
    }

    public String getEnableDescription5() {
        return enableDescription5;
    }

    public void setEnableDescription5(String enableDescription5) {
        this.enableDescription5 = enableDescription5;
    }

    public String getEnableDescription6() {
        return enableDescription6;
    }

    public void setEnableDescription6(String enableDescription6) {
        this.enableDescription6 = enableDescription6;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getStandOptions() {
        return standOptions;
    }

    public void setStandOptions(String standOptions) {
        this.standOptions = standOptions;
    }

    public Object getStandBackground() {
        return standBackground;
    }

    public void setStandBackground(Object standBackground) {
        this.standBackground = standBackground;
    }

    public String getShowOnFront() {
        return showOnFront;
    }

    public void setShowOnFront(String showOnFront) {
        this.showOnFront = showOnFront;
    }

    public Object getRecruitersLimit() {
        return recruitersLimit;
    }

    public void setRecruitersLimit(Object recruitersLimit) {
        this.recruitersLimit = recruitersLimit;
    }

    public Object getCompanyAdminsLimit() {
        return companyAdminsLimit;
    }

    public void setCompanyAdminsLimit(Object companyAdminsLimit) {
        this.companyAdminsLimit = companyAdminsLimit;
    }

    public Object getCompanyMediaLimit() {
        return companyMediaLimit;
    }

    public void setCompanyMediaLimit(Object companyMediaLimit) {
        this.companyMediaLimit = companyMediaLimit;
    }

    public Object getCompanyDocumentsLimit() {
        return companyDocumentsLimit;
    }

    public void setCompanyDocumentsLimit(Object companyDocumentsLimit) {
        this.companyDocumentsLimit = companyDocumentsLimit;
    }

    public Object getCompanyWebinarsLimit() {
        return companyWebinarsLimit;
    }

    public void setCompanyWebinarsLimit(Object companyWebinarsLimit) {
        this.companyWebinarsLimit = companyWebinarsLimit;
    }

    public String getDisableCompanyChat() {
        return disableCompanyChat;
    }

    public void setDisableCompanyChat(String disableCompanyChat) {
        this.disableCompanyChat = disableCompanyChat;
    }

    public String getDisableCompanySalary() {
        return disableCompanySalary;
    }

    public void setDisableCompanySalary(String disableCompanySalary) {
        this.disableCompanySalary = disableCompanySalary;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
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

    public List<CompanyMedium> getCompanyMedia() {
        return companyMedia;
    }

    public void setCompanyMedia(List<CompanyMedium> companyMedia) {
        this.companyMedia = companyMedia;
    }

}
