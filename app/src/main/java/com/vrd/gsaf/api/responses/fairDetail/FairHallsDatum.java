package com.vrd.gsaf.api.responses.fairDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FairHallsDatum {

    @SerializedName("hall_id")
    @Expose
    private String hallId;
    @SerializedName("hall_name")
    @Expose
    private String hallName;
    @SerializedName("show_hall")
    @Expose
    private String showHall;

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getShowHall() {
        return showHall;
    }

    public void setShowHall(String showHall) {
        this.showHall = showHall;
    }

}
