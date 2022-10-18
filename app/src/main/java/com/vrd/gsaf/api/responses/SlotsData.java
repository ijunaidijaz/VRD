package com.vrd.gsaf.api.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vrd.gsaf.api.responses.interviewSlots.Slot;

import java.util.List;

public class SlotsData {

    @SerializedName("slots")
    @Expose
    private List<Slot> slotList = null;

    public List<Slot> getSlotList() {
        return slotList;
    }

    public void setSlotList(List<Slot> slotList) {
        this.slotList = slotList;
    }

}
