package com.vrd.gsaf.api.responses.receptionist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("recList")
    @Expose
    private List<Rec> recList = null;

    public List<Rec> getRecList() {
        return recList;
    }

    public void setRecList(List<Rec> recList) {
        this.recList = recList;
    }

}
