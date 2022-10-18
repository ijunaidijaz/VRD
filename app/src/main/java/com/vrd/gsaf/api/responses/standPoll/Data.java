package com.vrd.gsaf.api.responses.standPoll;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("attended")
    @Expose
    private Boolean attended;
    @SerializedName("pollList")
    @Expose
    private List<Poll> pollList = null;

    @SerializedName("Request")
    @Expose
    private Request request = null;

    public Boolean getAttended() {
        return attended;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }

    public List<Poll> getPollList() {
        return pollList;
    }

    public void setPollList(List<Poll> pollList) {
        this.pollList = pollList;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
