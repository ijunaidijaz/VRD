package com.vrd.gsaf.api.responses.fairMedia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Media {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("fair_id")
    @Expose
    private String fairId;
    @SerializedName("fair_media_name")
    @Expose
    private String fairMediaName;
    @SerializedName("fair_media_type")
    @Expose
    private String fairMediaType;
    @SerializedName("fair_media_description")
    @Expose
    private String fairMediaDescription;
    @SerializedName("fair_media_link")
    @Expose
    private String fairMediaLink;
    @SerializedName("fair_media_image")
    @Expose
    private String fairMediaImage;
    @SerializedName("fair_media_video")
    @Expose
    private String fairMediaVideo;
    @SerializedName("fair_media_document")
    @Expose
    private String fairMediaDocument;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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

    public String getFairMediaName() {
        return fairMediaName;
    }

    public void setFairMediaName(String fairMediaName) {
        this.fairMediaName = fairMediaName;
    }

    public String getFairMediaType() {
        return fairMediaType;
    }

    public void setFairMediaType(String fairMediaType) {
        this.fairMediaType = fairMediaType;
    }

    public String getFairMediaDescription() {
        return fairMediaDescription;
    }

    public void setFairMediaDescription(String fairMediaDescription) {
        this.fairMediaDescription = fairMediaDescription;
    }

    public String getFairMediaLink() {
        return fairMediaLink;
    }

    public void setFairMediaLink(String fairMediaLink) {
        this.fairMediaLink = fairMediaLink;
    }

    public String getFairMediaImage() {
        return fairMediaImage;
    }

    public void setFairMediaImage(String fairMediaImage) {
        this.fairMediaImage = fairMediaImage;
    }

    public String getFairMediaVideo() {
        return fairMediaVideo;
    }

    public void setFairMediaVideo(String fairMediaVideo) {
        this.fairMediaVideo = fairMediaVideo;
    }

    public String getFairMediaDocument() {
        return fairMediaDocument;
    }

    public void setFairMediaDocument(String fairMediaDocument) {
        this.fairMediaDocument = fairMediaDocument;
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
