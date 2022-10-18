package com.vrd.gsaf.api.responses.appliedJobs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("appliedJobs")
    @Expose
    private List<AppliedJob> appliedJobs = null;

    public List<AppliedJob> getAppliedJobs() {
        return appliedJobs;
    }

    public void setAppliedJobs(List<AppliedJob> appliedJobs) {
        this.appliedJobs = appliedJobs;
    }

}
