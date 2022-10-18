package com.vrd.gsaf.api.responses.stands;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stand {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("stand_top")
    @Expose
    private String standTop;
    @SerializedName("stand_left")
    @Expose
    private String standLeft;
    @SerializedName("stand_width")
    @Expose
    private String standWidth;
    @SerializedName("stand_height")
    @Expose
    private String standHeight;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStandTop() {
        return standTop;
    }

    public void setStandTop(String standTop) {
        this.standTop = standTop;
    }

    public String getStandLeft() {
        return standLeft;
    }

    public void setStandLeft(String standLeft) {
        this.standLeft = standLeft;
    }

    public String getStandWidth() {
        return standWidth;
    }

    public void setStandWidth(String standWidth) {
        this.standWidth = standWidth;
    }

    public String getStandHeight() {
        return standHeight;
    }

    public void setStandHeight(String standHeight) {
        this.standHeight = standHeight;
    }

}
