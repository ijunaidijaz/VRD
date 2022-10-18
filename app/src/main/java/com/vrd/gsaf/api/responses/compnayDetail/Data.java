package com.vrd.gsaf.api.responses.compnayDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("companyList")
    @Expose
    private List<CompanyList> companyList;
    @SerializedName("standBackground")
    @Expose
    private String standBackground;
    @SerializedName("standBackgroundUrl")
    @Expose
    private String standBackgroundUrl;

    public String getStandBackgroundUrl() {
        return standBackgroundUrl;
    }

    public void setStandBackgroundUrl(String standBackgroundUrl) {
        this.standBackgroundUrl = standBackgroundUrl;
    }

    public List<CompanyList> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<CompanyList> companyList) {
        this.companyList = companyList;
    }

    public String getStandBackground() {
        return standBackground;
    }

    public void setStandBackground(String standBackground) {
        this.standBackground = standBackground;
    }

}
