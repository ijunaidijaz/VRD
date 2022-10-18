package com.vrd.gsaf.api.responses.general;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("candidateTotalGoodieBag")
    @Expose
    private int totalGoodies;

    public int getTotalGoodies() {
        return totalGoodies;
    }

    public void setTotalGoodies(int totalGoodies) {
        this.totalGoodies = totalGoodies;
    }
}
