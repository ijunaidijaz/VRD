package com.vrd.gsaf.api.responses.standPoll;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PollResult {

    @SerializedName("options_result")
    @Expose
    private List<OptionsResult> optionsResult = null;
    @SerializedName("total_percentage")
    @Expose
    private Float totalPercentage;
    @SerializedName("total_attendees")
    @Expose
    private Integer totalAttendees;

    public List<OptionsResult> getOptionsResult() {
        return optionsResult;
    }

    public void setOptionsResult(List<OptionsResult> optionsResult) {
        this.optionsResult = optionsResult;
    }

    public Float getTotalPercentage() {
        return totalPercentage;
    }

    public void setTotalPercentage(Float totalPercentage) {
        this.totalPercentage = totalPercentage;
    }

    public Integer getTotalAttendees() {
        return totalAttendees;
    }

    public void setTotalAttendees(Integer totalAttendees) {
        this.totalAttendees = totalAttendees;
    }

}
