package com.vrd.gsaf.api.responses.fairDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefaultLanguage {

    @SerializedName("lang_name")
    @Expose
    private String langName;
    @SerializedName("lang_code")
    @Expose
    private String langCode;
    @SerializedName("auto_id")
    @Expose
    private String autoId;
    @SerializedName("is_default")
    @Expose
    private String isDefault;

    public String getLangName() {
        return langName;
    }

    public void setLangName(String langName) {
        this.langName = langName;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

}
