package com.vrd.gsaf.api.responses.aboutUs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("fair_news")
    @Expose
    private String fairNews;
    @SerializedName("address")
    @Expose
    private String address;

    public String getFairNews() {
        return fairNews;
    }

    public void setFairNews(String fairNews) {
        this.fairNews = fairNews;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
