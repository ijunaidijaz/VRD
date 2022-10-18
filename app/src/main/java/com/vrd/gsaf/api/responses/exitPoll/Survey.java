package com.vrd.gsaf.api.responses.exitPoll;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Survey {

    @SerializedName("survey_id")
    @Expose
    private Integer surveyId;
    @SerializedName("created_by")
    @Expose
    private Integer createdBy;
    @SerializedName("survey_fair_id")
    @Expose
    private Integer surveyFairId;
    @SerializedName("survey_type")
    @Expose
    private String surveyType;
    @SerializedName("survey_graph_type")
    @Expose
    private String surveyGraphType;
    @SerializedName("survey_question")
    @Expose
    private String surveyQuestion;
    @SerializedName("survey_total_option")
    @Expose
    private Integer surveyTotalOption;
    @SerializedName("survey_status")
    @Expose
    private String surveyStatus;
    @SerializedName("user_answer")
    @Expose
    private String userAnswer;
    @SerializedName("survey_options")
    @Expose
    private List<SurveyOption> surveyOptions = null;
    @SerializedName("survey_result")
    @Expose
    private List<SurveyResult> surveyResult = null;

    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getSurveyFairId() {
        return surveyFairId;
    }

    public void setSurveyFairId(Integer surveyFairId) {
        this.surveyFairId = surveyFairId;
    }

    public String getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public String getSurveyGraphType() {
        return surveyGraphType;
    }

    public void setSurveyGraphType(String surveyGraphType) {
        this.surveyGraphType = surveyGraphType;
    }

    public String getSurveyQuestion() {
        return surveyQuestion;
    }

    public void setSurveyQuestion(String surveyQuestion) {
        this.surveyQuestion = surveyQuestion;
    }

    public Integer getSurveyTotalOption() {
        return surveyTotalOption;
    }

    public void setSurveyTotalOption(Integer surveyTotalOption) {
        this.surveyTotalOption = surveyTotalOption;
    }

    public String getSurveyStatus() {
        return surveyStatus;
    }

    public void setSurveyStatus(String surveyStatus) {
        this.surveyStatus = surveyStatus;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public List<SurveyOption> getSurveyOptions() {
        return surveyOptions;
    }

    public void setSurveyOptions(List<SurveyOption> surveyOptions) {
        this.surveyOptions = surveyOptions;
    }

    public List<SurveyResult> getSurveyResult() {
        return surveyResult;
    }

    public void setSurveyResult(List<SurveyResult> surveyResult) {
        this.surveyResult = surveyResult;
    }

}
