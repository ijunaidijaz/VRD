package com.vrd.gsaf.api.responses.FairsHalls;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FairHallsResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private HallsData data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HallsData getData() {
        return data;
    }

    public void setData(HallsData data) {
        this.data = data;
    }

}
