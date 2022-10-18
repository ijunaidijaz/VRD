package com.vrd.gsaf.api.responses.halls;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HallsDatum {

    @SerializedName("unique_id")
    @Expose
    private Integer uniqueId;
    @SerializedName("fair_id")
    @Expose
    private Integer fairId;
    @SerializedName("hall_id")
    @Expose
    private Integer hallId;
    @SerializedName("hall_name")
    @Expose
    private String hallName;
    @SerializedName("hall_background_front")
    @Expose
    private String hallBackgroundFront;
    @SerializedName("poster_background")
    @Expose
    private String posterBackground;
    @SerializedName("hall_grid_view")
    @Expose
    private String hallGridView;

    public Integer getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Integer uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Integer getFairId() {
        return fairId;
    }

    public void setFairId(Integer fairId) {
        this.fairId = fairId;
    }

    public Integer getHallId() {
        return hallId;
    }

    public void setHallId(Integer hallId) {
        this.hallId = hallId;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getHallBackgroundFront() {
        return hallBackgroundFront;
    }

    public void setHallBackgroundFront(String hallBackgroundFront) {
        this.hallBackgroundFront = hallBackgroundFront;
    }

    public String getPosterBackground() {
        return posterBackground;
    }

    public void setPosterBackground(String posterBackground) {
        this.posterBackground = posterBackground;
    }

    public String getHallGridView() {
        return hallGridView;
    }

    public void setHallGridView(String hallGridView) {
        this.hallGridView = hallGridView;
    }

}
