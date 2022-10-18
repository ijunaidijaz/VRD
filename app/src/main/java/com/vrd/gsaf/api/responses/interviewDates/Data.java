package com.vrd.gsaf.api.responses.interviewDates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("matchDate")
    @Expose
    private List<MatchDate> matchDate = null;
    @SerializedName("highlitedDates")
    @Expose
    private List<String> highlitedDates = null;

    public List<MatchDate> getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(List<MatchDate> matchDate) {
        this.matchDate = matchDate;
    }

    public List<String> getHighlitedDates() {
        return highlitedDates;
    }

    public void setHighlitedDates(List<String> highlitedDates) {
        this.highlitedDates = highlitedDates;
    }

}
