package com.vrd.gsaf.api.responses.faq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Faq {

    @SerializedName("faq_id")
    @Expose
    private int id;
    @SerializedName("fair_id")
    @Expose
    private int fairId;
    @SerializedName("faq_display_order")
    @Expose
    private String displayOrder;
    @SerializedName("faq_question")
    @Expose
    private String question;
    @SerializedName("faq_answer")
    @Expose
    private String answer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFairId() {
        return fairId;
    }

    public void setFairId(int fairId) {
        this.fairId = fairId;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
