package com.vrd.gsaf.api.responses.fairDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SystemUrl {

    @SerializedName("admin_url")
    @Expose
    private String adminUrl;
    @SerializedName("api_url")
    @Expose
    private String apiUrl;
    @SerializedName("front_url")
    @Expose
    private String frontUrl;
    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;
    @SerializedName("assets_url")
    @Expose
    private String assetsUrl;
    @SerializedName("image_route")
    @Expose
    private String imageRoute;
    @SerializedName("video_route")
    @Expose
    private String videoRoute;
    @SerializedName("document_route")
    @Expose
    private String documentRoute;
    @SerializedName("resume_url")
    @Expose
    private String resumeUrl;
    @SerializedName("resume_bucket")
    @Expose
    private String resumeBucket;
    @SerializedName("resume_aws_route")
    @Expose
    private String resumeAwsRoute;
    @SerializedName("resume_route")
    @Expose
    private String resumeRoute;
    @SerializedName("resume_region")
    @Expose
    private String resumeRegion;

    public String getAdminUrl() {
        return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getFrontUrl() {
        return frontUrl;
    }

    public void setFrontUrl(String frontUrl) {
        this.frontUrl = frontUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAssetsUrl() {
        return assetsUrl;
    }

    public void setAssetsUrl(String assetsUrl) {
        this.assetsUrl = assetsUrl;
    }

    public String getImageRoute() {
        return imageRoute;
    }

    public void setImageRoute(String imageRoute) {
        this.imageRoute = imageRoute;
    }

    public String getVideoRoute() {
        return videoRoute;
    }

    public void setVideoRoute(String videoRoute) {
        this.videoRoute = videoRoute;
    }

    public String getDocumentRoute() {
        return documentRoute;
    }

    public void setDocumentRoute(String documentRoute) {
        this.documentRoute = documentRoute;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getResumeBucket() {
        return resumeBucket;
    }

    public void setResumeBucket(String resumeBucket) {
        this.resumeBucket = resumeBucket;
    }

    public String getResumeAwsRoute() {
        return resumeAwsRoute;
    }

    public void setResumeAwsRoute(String resumeAwsRoute) {
        this.resumeAwsRoute = resumeAwsRoute;
    }

    public String getResumeRoute() {
        return resumeRoute;
    }

    public void setResumeRoute(String resumeRoute) {
        this.resumeRoute = resumeRoute;
    }

    public String getResumeRegion() {
        return resumeRegion;
    }

    public void setResumeRegion(String resumeRegion) {
        this.resumeRegion = resumeRegion;
    }

}
