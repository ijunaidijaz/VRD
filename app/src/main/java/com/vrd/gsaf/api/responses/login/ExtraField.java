package com.vrd.gsaf.api.responses.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtraField {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("field_id")
    @Expose
    private String fieldId;
    @SerializedName("field_title")
    @Expose
    private String fieldTitle;
    @SerializedName("option_id")
    @Expose
    private String optionId;
    @SerializedName("field_type")
    @Expose
    private String fieldType;
    @SerializedName("field_text")
    @Expose
    private String fieldText;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldText() {
        return fieldText;
    }

    public void setFieldText(String fieldText) {
        this.fieldText = fieldText;
    }

}
