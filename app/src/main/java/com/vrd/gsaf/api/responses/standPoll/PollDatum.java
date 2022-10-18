package com.vrd.gsaf.api.responses.standPoll;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PollDatum {

    @SerializedName("poll_id")
    @Expose
    private String pollId;
    @SerializedName("poll_type")
    @Expose
    private String pollType;
    @SerializedName("option_id")
    @Expose
    private String optionId;

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getPollType() {
        return pollType;
    }

    public void setPollType(String pollType) {
        this.pollType = pollType;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

}
