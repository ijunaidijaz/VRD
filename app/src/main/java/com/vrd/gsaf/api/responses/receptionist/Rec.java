package com.vrd.gsaf.api.responses.receptionist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rec {

    @SerializedName("fair_id")
    @Expose
    private Integer fairId;
    @SerializedName("reception_id")
    @Expose
    private Integer receptionId;
    @SerializedName("reception_name")
    @Expose
    private String receptionName;
    @SerializedName("reception_email")
    @Expose
    private String reception_email;
    @SerializedName("reception_image")
    @Expose
    private String receptionImage;
    @SerializedName("reception_status")
    @Expose
    private String receptionStatus;

    public Integer getFairId() {
        return fairId;
    }

    public void setFairId(Integer fairId) {
        this.fairId = fairId;
    }

    public Integer getReceptionId() {
        return receptionId;
    }

    public void setReceptionId(Integer receptionId) {
        this.receptionId = receptionId;
    }

    public String getReceptionName() {
        return receptionName;
    }

    public void setReceptionName(String receptionName) {
        this.receptionName = receptionName;
    }

    public String getReceptionImage() {
        return receptionImage;
    }

    public void setReceptionImage(String receptionImage) {
        this.receptionImage = receptionImage;
    }

    public String getReceptionStatus() {
        return receptionStatus;
    }

    public void setReceptionStatus(String receptionStatus) {
        this.receptionStatus = receptionStatus;
    }

    public String getReception_email() {
        return reception_email;
    }

    public void setReception_email(String reception_email) {
        this.reception_email = reception_email;
    }
}
