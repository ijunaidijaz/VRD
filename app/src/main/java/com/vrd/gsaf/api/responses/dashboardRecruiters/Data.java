package com.vrd.gsaf.api.responses.dashboardRecruiters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("recruiterList")
    @Expose
    private List<Recruiter> recruiterList = null;

    public List<Recruiter> getRecruiterList() {
        return recruiterList;
    }

    public void setRecruiterList(List<Recruiter> recruiterList) {
        this.recruiterList = recruiterList;
    }

}
