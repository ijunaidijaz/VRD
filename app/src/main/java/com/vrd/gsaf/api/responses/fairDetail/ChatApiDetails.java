package com.vrd.gsaf.api.responses.fairDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatApiDetails {
    @SerializedName("app_id")
    @Expose
    private String appId;
    @SerializedName("api_key")
    @Expose
    private String apiAuthKey;
    @SerializedName("rest_api_key")
    @Expose
    private String restApiKey;
    @SerializedName("region")
    @Expose
    private String region;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getApiAuthKey() {
        return apiAuthKey;
    }

    public void setApiAuthKey(String apiAuthKey) {
        this.apiAuthKey = apiAuthKey;
    }

    public String getRestApiKey() {
        return restApiKey;
    }

    public void setRestApiKey(String restApiKey) {
        this.restApiKey = restApiKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
