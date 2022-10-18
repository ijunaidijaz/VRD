package com.vrd.gsaf.api.responses.fairExtraFields;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FairData {
    @SerializedName("extra_fields")
    @Expose
    private List<ExtraField> extraFields = null;

    public List<ExtraField> getExtraFields() {
        return extraFields;
    }

    public void setExtraFields(List<ExtraField> extraFields) {
        this.extraFields = extraFields;
    }

}
