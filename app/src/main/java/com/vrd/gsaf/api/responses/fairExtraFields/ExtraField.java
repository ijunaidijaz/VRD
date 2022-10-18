package com.vrd.gsaf.api.responses.fairExtraFields;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExtraField {
    @SerializedName("auto_id")
    @Expose
    private Integer autoId;
    @SerializedName("fair_id")
    @Expose
    private Integer fairId;
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
    @SerializedName("user_answer")
    @Expose
    private String userAnswer;
    @SerializedName("options")
    @Expose
    private List<FairOption> options = null;

    public String getAutoId() {
        return String.valueOf(autoId);
    }

    public void setAutoId(Integer autoId) {
        this.autoId = autoId;
    }

    public Integer getFairId() {
        return fairId;
    }

    public void setFairId(Integer fairId) {
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

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public List<FairOption> getOptions() {
        return options;
    }

    public void setOptions(List<FairOption> options) {
        this.options = options;
    }

}
