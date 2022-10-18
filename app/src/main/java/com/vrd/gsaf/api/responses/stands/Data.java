package com.vrd.gsaf.api.responses.stands;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @SerializedName("companyList")
    @Expose
    private List<Company> companyList = null;

    public ArrayList<Company> getCompanyList() {
        return (ArrayList<Company>) companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }

}
