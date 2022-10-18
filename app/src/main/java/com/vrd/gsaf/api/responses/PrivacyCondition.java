package com.vrd.gsaf.api.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrivacyCondition {
    @SerializedName("privacy_policy")
    @Expose
    private String privacyPolicy;
    @SerializedName("terms_conditions")
    @Expose
    private String termsConditions;
    @SerializedName("fair_close_content")
    @Expose
    private String fairCloseContent;
    @SerializedName("welcome_content_after_registration")
    @Expose
    private String welcomeNote;

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public String getFairCloseContent() {
        return fairCloseContent;
    }

    public void setFairCloseContent(String fairCloseContent) {
        this.fairCloseContent = fairCloseContent;
    }

    public String getTermsConditions() {
        return termsConditions;
    }

    public void setTermsConditions(String termsConditions) {
        this.termsConditions = termsConditions;
    }

    public String getWelcomeNote() {
        return welcomeNote;
    }

    public void setWelcomeNote(String welcomeNote) {
        this.welcomeNote = welcomeNote;
    }
}
