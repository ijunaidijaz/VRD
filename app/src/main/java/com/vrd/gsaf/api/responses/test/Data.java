package com.vrd.gsaf.api.responses.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("withArray")
    @Expose
    private List<WithArray> withArray = null;
    @SerializedName("notArray")
    @Expose
    private List<NotArray> notArray = null;

    public List<WithArray> getWithArray() {
        return withArray;
    }

    public void setWithArray(List<WithArray> withArray) {
        this.withArray = withArray;
    }

    public List<NotArray> getNotArray() {
        return notArray;
    }

    public void setNotArray(List<NotArray> notArray) {
        this.notArray = notArray;
    }

}
