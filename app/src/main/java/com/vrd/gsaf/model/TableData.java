package com.vrd.gsaf.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TableData {
    @SerializedName("networking_tables")
    @Expose
    private List<NetworkingTable> networkingTables = null;
    @SerializedName("networking_halls")
    @Expose
    private Integer networkingHalls;

    public List<NetworkingTable> getNetworkingTables() {
        return networkingTables;
    }

    public void setNetworkingTables(List<NetworkingTable> networkingTables) {
        this.networkingTables = networkingTables;
    }

    public Integer getNetworkingHalls() {
        return networkingHalls;
    }

    public void setNetworkingHalls(Integer networkingHalls) {
        this.networkingHalls = networkingHalls;
    }

}
