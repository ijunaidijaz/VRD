package com.vrd.gsaf.api.responses.standPoll;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestRequest {

    @SerializedName("Request")
    @Expose
    private Request request;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

}
