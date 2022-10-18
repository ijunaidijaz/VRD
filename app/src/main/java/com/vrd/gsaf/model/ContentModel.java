package com.vrd.gsaf.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentModel {
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
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("fair_help")
        @Expose
        private String fairHelp;
        @SerializedName("pre_fair_enter_content")
        @Expose
        private String pre_fair_enter_content;

        public String getFairHelp() {
            return fairHelp;
        }

        public void setFairHelp(String fairHelp) {
            this.fairHelp = fairHelp;
        }

        public String getPre_fair_enter_content() {
            return pre_fair_enter_content;
        }

        public void setPre_fair_enter_content(String pre_fair_enter_content) {
            this.pre_fair_enter_content = pre_fair_enter_content;
        }

    }
}

