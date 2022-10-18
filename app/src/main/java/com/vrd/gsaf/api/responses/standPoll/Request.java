package com.vrd.gsaf.api.responses.standPoll;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Request {

    @SerializedName("fair_id")
    @Expose
    private String fairId;
    @SerializedName("company_id")
    @Expose
    private String companyId;
    @SerializedName("candidate_id")
    @Expose
    private String candidateId;
    @SerializedName("poll_data")
    @Expose
    private List<PollDatum> pollData = null;
    @SerializedName("test_response")
    @Expose
    private String testResponse;

    public String getFairId() {
        return fairId;
    }

    public void setFairId(String fairId) {
        this.fairId = fairId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public List<PollDatum> getPollData() {
        return pollData;
    }

    public void setPollData(List<PollDatum> pollData) {
        this.pollData = pollData;
    }

    public String getTestResponse() {
        return testResponse;
    }

    public void setTestResponse(String testResponse) {
        this.testResponse = testResponse;
    }

}
