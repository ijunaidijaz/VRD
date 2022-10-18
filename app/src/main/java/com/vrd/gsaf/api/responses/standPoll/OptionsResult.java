package com.vrd.gsaf.api.responses.standPoll;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OptionsResult {

    @SerializedName("poll_id")
    @Expose
    private Integer pollId;
    @SerializedName("option_id")
    @Expose
    private Integer optionId;
    @SerializedName("option_value")
    @Expose
    private String optionValue;
    @SerializedName("total_attempt")
    @Expose
    private Integer totalAttempt;
    @SerializedName("total_percentage")
    @Expose
    private Float totalPercentage;

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public Integer getTotalAttempt() {
        return totalAttempt;
    }

    public void setTotalAttempt(Integer totalAttempt) {
        this.totalAttempt = totalAttempt;
    }

    public Float getTotalPercentage() {
        return totalPercentage;
    }

    public void setTotalPercentage(Float totalPercentage) {
        this.totalPercentage = totalPercentage;
    }

}
