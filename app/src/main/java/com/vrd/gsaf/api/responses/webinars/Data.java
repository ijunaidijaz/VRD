package com.vrd.gsaf.api.responses.webinars;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("webinarList")
    @Expose
    private List<Webinar> webinarList = null;

    public List<Webinar> getWebinarList() {
        return webinarList;
    }

    public void setWebinarList(List<Webinar> webinarList) {
        this.webinarList = webinarList;
    }

}
