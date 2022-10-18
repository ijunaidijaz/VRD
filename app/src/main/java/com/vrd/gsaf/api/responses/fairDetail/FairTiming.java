package com.vrd.gsaf.api.responses.fairDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FairTiming {

    @SerializedName("fair_id")
    @Expose
    private Integer fairId;
    @SerializedName("fair_start")
    @Expose
    private String fairStart;
    @SerializedName("fair_end")
    @Expose
    private String fairEnd;
    @SerializedName("fair_start_date")
    @Expose
    private String fairStartDate;
    @SerializedName("fair_end_date")
    @Expose
    private String fairEndDate;
    @SerializedName("fair_start_time")
    @Expose
    private String fairStartTime;
    @SerializedName("fair_end_time")
    @Expose
    private String fairEndTime;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("app_region")
    @Expose
    private String appRegion;

    public Integer getFairId() {
        return fairId;
    }

    public void setFairId(Integer fairId) {
        this.fairId = fairId;
    }

    public String getFairStart() {
        return fairStart;
    }

    public void setFairStart(String fairStart) {
        this.fairStart = fairStart;
    }

    public String getFairEnd() {
        return fairEnd;
    }

    public void setFairEnd(String fairEnd) {
        this.fairEnd = fairEnd;
    }

    public String getFairStartDate() {
        return fairStartDate;
    }

    public void setFairStartDate(String fairStartDate) {
        this.fairStartDate = fairStartDate;
    }

    public String getFairEndDate() {
        return fairEndDate;
    }

    public void setFairEndDate(String fairEndDate) {
        this.fairEndDate = fairEndDate;
    }

    public String getFairStartTime() {
        return fairStartTime;
    }

    public void setFairStartTime(String fairStartTime) {
        this.fairStartTime = fairStartTime;
    }

    public String getFairEndTime() {
        return fairEndTime;
    }

    public void setFairEndTime(String fairEndTime) {
        this.fairEndTime = fairEndTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAppRegion() {
        return appRegion;
    }

    public void setAppRegion(String appRegion) {
        this.appRegion = appRegion;
    }

}
