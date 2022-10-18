package com.vrd.gsaf.api.responses.careerTest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question {

    @SerializedName("test_id")
    @Expose
    private Integer id;
    @SerializedName("fair_id")
    @Expose
    private Integer fairId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("short_question")
    @Expose
    private String shortQuestion;
    @SerializedName("backoffice_question")
    @Expose
    private String backofficeQuestion;
    @SerializedName("question_type")
    @Expose
    private String questionType;
    @SerializedName("min_selection")
    @Expose
    private String minSelection;
    @SerializedName("max_selection")
    @Expose
    private String maxSelection;
    @SerializedName("display_order")
    @Expose
    private String displayOrder;
    @SerializedName("answers")
    @Expose
    private List<Answer> answers = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFairId() {
        return fairId;
    }

    public void setFairId(Integer fairId) {
        this.fairId = fairId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getShortQuestion() {
        return shortQuestion;
    }

    public void setShortQuestion(String shortQuestion) {
        this.shortQuestion = shortQuestion;
    }

    public String getBackofficeQuestion() {
        return backofficeQuestion;
    }

    public void setBackofficeQuestion(String backofficeQuestion) {
        this.backofficeQuestion = backofficeQuestion;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getMinSelection() {
        return minSelection;
    }

    public void setMinSelection(String minSelection) {
        this.minSelection = minSelection;
    }

    public String getMaxSelection() {
        return maxSelection;
    }

    public void setMaxSelection(String maxSelection) {
        this.maxSelection = maxSelection;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

}
