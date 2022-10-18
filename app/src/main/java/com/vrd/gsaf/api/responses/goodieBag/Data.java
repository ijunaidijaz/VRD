package com.vrd.gsaf.api.responses.goodieBag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @SerializedName("goodieBagList")
    @Expose
    private List<GoodieBag> goodieBagList = null;

    public ArrayList<GoodieBag> getGoodieBagList() {
        return (ArrayList<GoodieBag>) goodieBagList;
    }

    public void setGoodieBagList(List<GoodieBag> goodieBagList) {
        this.goodieBagList = goodieBagList;
    }

}
