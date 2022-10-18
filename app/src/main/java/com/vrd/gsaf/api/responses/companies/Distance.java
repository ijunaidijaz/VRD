package com.vrd.gsaf.api.responses.companies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Distance {

    @SerializedName("feet")
    @Expose
    private Integer feet;
    @SerializedName("miles")
    @Expose
    private Integer miles;
    @SerializedName("yards")
    @Expose
    private Integer yards;
    @SerializedName("meters")
    @Expose
    private Integer meters;
    @SerializedName("kilometers")
    @Expose
    private Integer kilometers;

    public Integer getFeet() {
        return feet;
    }

    public void setFeet(Integer feet) {
        this.feet = feet;
    }

    public Integer getMiles() {
        return miles;
    }

    public void setMiles(Integer miles) {
        this.miles = miles;
    }

    public Integer getYards() {
        return yards;
    }

    public void setYards(Integer yards) {
        this.yards = yards;
    }

    public Integer getMeters() {
        return meters;
    }

    public void setMeters(Integer meters) {
        this.meters = meters;
    }

    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer kilometers) {
        this.kilometers = kilometers;
    }

}
