package com.vrd.gsaf.api.responses.compnayDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyMedium {

    @SerializedName("company_id")
    @Expose
    private String companyId;
    @SerializedName("company_media_name")
    @Expose
    private String companyMediaName;
    @SerializedName("company_media_type")
    @Expose
    private String companyMediaType;
    @SerializedName("company_media_description")
    @Expose
    private String companyMediaDescription;
    @SerializedName("company_media_link")
    @Expose
    private String companyMediaLink;
    @SerializedName("company_media_image")
    @Expose
    private String companyMediaImage;
    @SerializedName("company_media_video")
    @Expose
    private String companyMediaVideo;
    @SerializedName("company_media_document")
    @Expose
    private String companyMediaDocument;
    @SerializedName("company_media_order")
    @Expose
    private String companyMediaOrder;
    @SerializedName("is_show_front")
    @Expose
    private String isShowFront;
    @SerializedName("display_order")
    @Expose
    private String displayOrder;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyMediaName() {
        return companyMediaName;
    }

    public void setCompanyMediaName(String companyMediaName) {
        this.companyMediaName = companyMediaName;
    }

    public String getCompanyMediaType() {
        return companyMediaType;
    }

    public void setCompanyMediaType(String companyMediaType) {
        this.companyMediaType = companyMediaType;
    }

    public String getCompanyMediaDescription() {
        return companyMediaDescription;
    }

    public void setCompanyMediaDescription(String companyMediaDescription) {
        this.companyMediaDescription = companyMediaDescription;
    }

    public String getCompanyMediaLink() {
        return companyMediaLink;
    }

    public void setCompanyMediaLink(String companyMediaLink) {
        this.companyMediaLink = companyMediaLink;
    }

    public String getCompanyMediaImage() {
        return companyMediaImage;
    }

    public void setCompanyMediaImage(String companyMediaImage) {
        this.companyMediaImage = companyMediaImage;
    }

    public String getCompanyMediaVideo() {
        return companyMediaVideo;
    }

    public void setCompanyMediaVideo(String companyMediaVideo) {
        this.companyMediaVideo = companyMediaVideo;
    }

    public String getCompanyMediaDocument() {
        return companyMediaDocument;
    }

    public void setCompanyMediaDocument(String companyMediaDocument) {
        this.companyMediaDocument = companyMediaDocument;
    }

    public String getCompanyMediaOrder() {
        return companyMediaOrder;
    }

    public void setCompanyMediaOrder(String companyMediaOrder) {
        this.companyMediaOrder = companyMediaOrder;
    }

    public String getIsShowFront() {
        return isShowFront;
    }

    public void setIsShowFront(String isShowFront) {
        this.isShowFront = isShowFront;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

}
