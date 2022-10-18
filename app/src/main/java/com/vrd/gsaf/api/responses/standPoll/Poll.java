package com.vrd.gsaf.api.responses.standPoll;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Poll {

    @SerializedName("poll_id")
    @Expose
    private Integer pollId;
    @SerializedName("created_by")
    @Expose
    private Integer createdBy;
    @SerializedName("poll_fair_id")
    @Expose
    private Integer pollFairId;
    @SerializedName("poll_graph_type")
    @Expose
    private String pollGraphType;
    @SerializedName("poll_company_id")
    @Expose
    private Integer pollCompanyId;
    @SerializedName("poll_type")
    @Expose
    private String pollType;
    @SerializedName("poll_question")
    @Expose
    private String pollQuestion;
    @SerializedName("poll_total_option")
    @Expose
    private Integer pollTotalOption;
    @SerializedName("poll_status")
    @Expose
    private String pollStatus;
    @SerializedName("poll_options")
    @Expose
    private List<PollOption> pollOptions = null;
    @SerializedName("poll_result")
    @Expose
    private List<PollResult> pollResult = null;

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getPollFairId() {
        return pollFairId;
    }

    public void setPollFairId(Integer pollFairId) {
        this.pollFairId = pollFairId;
    }

    public Integer getPollCompanyId() {
        return pollCompanyId;
    }

    public void setPollCompanyId(Integer pollCompanyId) {
        this.pollCompanyId = pollCompanyId;
    }

    public String getPollType() {
        return pollType;
    }

    public void setPollType(String pollType) {
        this.pollType = pollType;
    }

    public String getPollQuestion() {
        return pollQuestion;
    }

    public void setPollQuestion(String pollQuestion) {
        this.pollQuestion = pollQuestion;
    }

    public Integer getPollTotalOption() {
        return pollTotalOption;
    }

    public void setPollTotalOption(Integer pollTotalOption) {
        this.pollTotalOption = pollTotalOption;
    }

    public String getPollStatus() {
        return pollStatus;
    }

    public void setPollStatus(String pollStatus) {
        this.pollStatus = pollStatus;
    }

    public List<PollOption> getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(List<PollOption> pollOptions) {
        this.pollOptions = pollOptions;
    }

    public List<PollResult> getPollResult() {
        return pollResult;
    }

    public void setPollResult(List<PollResult> pollResult) {
        this.pollResult = pollResult;
    }


    public String getPollGraphType() {
        return pollGraphType;
    }

    public void setPollGraphType(String pollGraphType) {
        this.pollGraphType = pollGraphType;
    }
}
