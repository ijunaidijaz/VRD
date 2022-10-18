package com.vrd.gsaf.api.responses.FairsHalls;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vrd.gsaf.api.responses.halls.HallsDatum;

import java.util.List;

public class HallsData {
    @SerializedName("hallsData")
    @Expose
    private List<HallsDatum> hallsData = null;
    @SerializedName("enableGridView")
    @Expose
    private Boolean enableGridView;

    public List<HallsDatum> getHallsData() {
        return hallsData;
    }

    public void setHallsData(List<HallsDatum> hallsData) {
        this.hallsData = hallsData;
    }

    public Boolean getEnableGridView() {
        return enableGridView;
    }

    public void setEnableGridView(Boolean enableGridView) {
        this.enableGridView = enableGridView;
    }

}
