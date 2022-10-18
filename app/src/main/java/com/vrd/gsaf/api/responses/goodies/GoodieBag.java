package com.vrd.gsaf.api.responses.goodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoodieBag {

    @SerializedName("goodiebag_id")
    @Expose
    private Integer goodiebagId;
    @SerializedName("fair_id")
    @Expose
    private Integer fairId;
    @SerializedName("company_id")
    @Expose
    private Integer companyId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("company_logo")
    @Expose
    private String companyLogo;
    @SerializedName("goodiebag_name")
    @Expose
    private String goodiebagName;
    @SerializedName("goodiebag_type")
    @Expose
    private String goodiebagType;
    @SerializedName("goodiebag_link")
    @Expose
    private String goodiebagLink;
    @SerializedName("goodiebag_image")
    @Expose
    private String goodiebagImage;
    @SerializedName("goodiebag_video")
    @Expose
    private String goodiebagVideo;
    @SerializedName("goodiebag_document")
    @Expose
    private String goodiebagDocument;
    @SerializedName("is_added")
    @Expose
    private Integer isAdded;
    @SerializedName("goodiebag_description")
    @Expose
    private String goodiebagDescription;

    public String getGoodiebagDescription() {
        return goodiebagDescription;
    }

    public void setGoodiebagDescription(String goodiebagDescription) {
        this.goodiebagDescription = goodiebagDescription;
    }

    public Integer getGoodiebagId() {
        return goodiebagId;
    }

    public void setGoodiebagId(Integer goodiebagId) {
        this.goodiebagId = goodiebagId;
    }

    public Integer getFairId() {
        return fairId;
    }

    public void setFairId(Integer fairId) {
        this.fairId = fairId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getGoodiebagName() {
        return goodiebagName;
    }

    public void setGoodiebagName(String goodiebagName) {
        this.goodiebagName = goodiebagName;
    }

    public String getGoodiebagType() {
        return goodiebagType;
    }

    public void setGoodiebagType(String goodiebagType) {
        this.goodiebagType = goodiebagType;
    }

    public String getGoodiebagLink() {
        return goodiebagLink;
    }

    public void setGoodiebagLink(String goodiebagLink) {
        this.goodiebagLink = goodiebagLink;
    }

    public String getGoodiebagImage() {
        return goodiebagImage;
    }

    public void setGoodiebagImage(String goodiebagImage) {
        this.goodiebagImage = goodiebagImage;
    }

    public String getGoodiebagVideo() {
        return goodiebagVideo;
    }

    public void setGoodiebagVideo(String goodiebagVideo) {
        this.goodiebagVideo = goodiebagVideo;
    }

    public String getGoodiebagDocument() {
        return goodiebagDocument;
    }

    public void setGoodiebagDocument(String goodiebagDocument) {
        this.goodiebagDocument = goodiebagDocument;
    }

    public Integer getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(Integer isAdded) {
        this.isAdded = isAdded;
    }

}
