package com.vrd.gsaf.api.responses.schedules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("interviewSlots")
    @Expose
    private List<InterviewSlot> interviewSlots = null;

    public List<InterviewSlot> getInterviewSlots() {
        return interviewSlots;
    }

    public void setInterviewSlots(List<InterviewSlot> interviewSlots) {
        this.interviewSlots = interviewSlots;
    }

}
