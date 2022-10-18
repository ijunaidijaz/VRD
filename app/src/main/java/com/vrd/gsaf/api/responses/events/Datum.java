package com.vrd.gsaf.api.responses.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("fair_id")
    @Expose
    private int id;
    @SerializedName("fair_name")
    @Expose
    private String name;
    @SerializedName("fair_short_name")
    @Expose
    private String shortName;
    @SerializedName("fair_email")
    @Expose
    private String email;
    @SerializedName("fair_image")
    @Expose
    private String fairImage;
    @SerializedName("fair_register_time")
    @Expose
    private String registerTime;
    @SerializedName("fair_start_time")
    @Expose
    private String startTime;
    @SerializedName("fair_end_time")
    @Expose
    private String endTime;
    @SerializedName("fair_type")
    @Expose
    private String fairType;
    @SerializedName("organiser_name")
    @Expose
    private String organiserName;
    @SerializedName("organiser_id")
    @Expose
    private String organiserId;
    @SerializedName("fair_status")
    @Expose
    private String fairStatus;
    @SerializedName("organiser_Bgimage")
    @Expose
    private String backgroundImage;

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFairImage() {
        return fairImage;
    }

    public void setFairImage(String fairImage) {
        this.fairImage = fairImage;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFairType() {
        return fairType;
    }

    public void setFairType(String fairType) {
        this.fairType = fairType;
    }

    public String getOrganiserName() {
        return organiserName;
    }

    public void setOrganiserName(String organiserName) {
        this.organiserName = organiserName;
    }

    public String getOrganiserId() {
        return organiserId;
    }

    public void setOrganiserId(String organiserId) {
        this.organiserId = organiserId;
    }

    public String getFairStatus() {
        return fairStatus;
    }

    public void setFairStatus(String fairStatus) {
        this.fairStatus = fairStatus;
    }

}
