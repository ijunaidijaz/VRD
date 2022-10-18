package com.vrd.gsaf.api.responses.companies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Company {

    @SerializedName("company_id")
    @Expose
    private Integer companyId;
    @SerializedName("company_logo")
    @Expose
    private String companyLogo;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("company_email")
    @Expose
    private String companyEmail;
    @SerializedName("distance")
    @Expose
    private Distance distance;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
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

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

}
