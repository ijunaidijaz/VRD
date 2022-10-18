package com.vrd.gsaf.api.responses.fairExtraFields;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FairOption {
    @SerializedName("auto_id")
    @Expose
    private Integer autoId;
    @SerializedName("field_id")
    @Expose
    private String fieldId;
    @SerializedName("option_text")
    @Expose
    private String optionText;
    @SerializedName("is_selected")
    @Expose
    private Boolean isSelected;
    @SerializedName("order_by")
    @Expose
    private Integer orderBy;

    public Integer getAutoId() {
        return autoId;
    }

    public void setAutoId(Integer autoId) {
        this.autoId = autoId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }
}
