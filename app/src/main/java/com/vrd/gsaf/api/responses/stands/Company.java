package com.vrd.gsaf.api.responses.stands;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Company {

    @SerializedName("company_id")
    @Expose
    private Integer companyId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("company_logo")
    @Expose
    private String companyLogo;
    @SerializedName("company_email")
    @Expose
    private String companyEmail;
    @SerializedName("stand")
    @Expose
    private Stand stand;
    @SerializedName("company_stand_image")
    @Expose
    private String companyStandImage;
    @SerializedName("company_stand_type")
    @Expose
    private String companyStandType;

    @SerializedName("standBackgroundUrl")
    @Expose
    private String standBackgroundUrl;

    public String getStandBackgroundUrl() {
        return standBackgroundUrl;
    }

    public void setStandBackgroundUrl(String standBackgroundUrl) {
        this.standBackgroundUrl = standBackgroundUrl;
    }

    public String getCompanyStandImage() {
        return companyStandImage;
    }

    public void setCompanyStandImage(String companyStandImage) {
        this.companyStandImage = companyStandImage;
    }

    public String getCompanyStandType() {
        return companyStandType;
    }

    public void setCompanyStandType(String companyStandType) {
        this.companyStandType = companyStandType;
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

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public Stand getStand() {
        return stand;
    }

    public void setStand(Stand stand) {
        this.stand = stand;
    }

}
