package com.vrd.gsaf.api.responses.halls;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

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
