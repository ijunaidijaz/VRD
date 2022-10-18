package com.vrd.gsaf.api.responses.goodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("goodieBagList")
    @Expose
    private List<GoodieBag> goodieBagList = null;

    @SerializedName("candidateTotalGoodieBag")
    @Expose
    private Integer candidateTotalGoodieBag = null;

    public List<GoodieBag> getGoodieBagList() {
        return goodieBagList;
    }

    public void setGoodieBagList(List<GoodieBag> goodieBagList) {
        this.goodieBagList = goodieBagList;
    }

    public Integer getCandidateTotalGoodieBag() {
        return candidateTotalGoodieBag;
    }

    public void setCandidateTotalGoodieBag(Integer candidateTotalGoodieBag) {
        this.candidateTotalGoodieBag = candidateTotalGoodieBag;
    }
}
