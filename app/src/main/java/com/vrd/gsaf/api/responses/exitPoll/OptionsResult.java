package com.vrd.gsaf.api.responses.exitPoll;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OptionsResult {

    @SerializedName("survey_id")
    @Expose
    private Integer surveyId;
    @SerializedName("survey_type")
    @Expose
    private String surveyType;
    @SerializedName("option_id")
    @Expose
    private Integer optionId;
    @SerializedName("option_value")
    @Expose
    private String optionValue;
    @SerializedName("is_checked")
    @Expose
    private Boolean isChecked;
    @SerializedName("total_attempt")
    @Expose
    private Integer totalAttempt;
    @SerializedName("total_percentage")
    @Expose
    private Float totalPercentage;

    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public Integer getTotalAttempt() {
        return totalAttempt;
    }

    public void setTotalAttempt(Integer totalAttempt) {
        this.totalAttempt = totalAttempt;
    }

    public Float getTotalPercentage() {
        return totalPercentage;
    }

    public void setTotalPercentage(Float totalPercentage) {
        this.totalPercentage = totalPercentage;
    }

}
