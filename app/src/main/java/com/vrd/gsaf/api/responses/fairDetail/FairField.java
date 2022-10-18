package com.vrd.gsaf.api.responses.fairDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FairField {

    @SerializedName("auto_id")
    @Expose
    private String autoId;
    @SerializedName("fair_id")
    @Expose
    private String fairId;
    @SerializedName("field_title")
    @Expose
    private String fieldTitle;
    @SerializedName("field_type")
    @Expose
    private String fieldType;
    @SerializedName("field_place")
    @Expose
    private String fieldPlace;
    @SerializedName("is_required")
    @Expose
    private String isRequired;
    @SerializedName("hide_field")
    @Expose
    private String hideField;
    @SerializedName("is_multiselect")
    @Expose
    private String isMultiselect;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("options")
    @Expose
    private List<Option> options = null;

    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }

    public String getFairId() {
        return fairId;
    }

    public void setFairId(String fairId) {
        this.fairId = fairId;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldPlace() {
        return fieldPlace;
    }

    public void setFieldPlace(String fieldPlace) {
        this.fieldPlace = fieldPlace;
    }

    public String getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
    }

    public String getHideField() {
        return hideField;
    }

    public void setHideField(String hideField) {
        this.hideField = hideField;
    }

    public String getIsMultiselect() {
        return isMultiselect;
    }

    public void setIsMultiselect(String isMultiselect) {
        this.isMultiselect = isMultiselect;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

}
