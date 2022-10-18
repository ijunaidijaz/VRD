package com.vrd.gsaf.api.responses.recruiterDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailList {

    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("user_web")
    @Expose
    private String userWeb;
    @SerializedName("user_facebook")
    @Expose
    private String userFacebook;
    @SerializedName("user_twitter")
    @Expose
    private String userTwitter;
    @SerializedName("user_linkedin")
    @Expose
    private String userLinkedin;
    @SerializedName("user_instagram")
    @Expose
    private String userInstagram;
    @SerializedName("user_details")
    @Expose
    private String userDetails;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserWeb() {
        return userWeb;
    }

    public void setUserWeb(String userWeb) {
        this.userWeb = userWeb;
    }

    public String getUserFacebook() {
        return userFacebook;
    }

    public void setUserFacebook(String userFacebook) {
        this.userFacebook = userFacebook;
    }

    public String getUserTwitter() {
        return userTwitter;
    }

    public void setUserTwitter(String userTwitter) {
        this.userTwitter = userTwitter;
    }

    public String getUserLinkedin() {
        return userLinkedin;
    }

    public void setUserLinkedin(String userLinkedin) {
        this.userLinkedin = userLinkedin;
    }

    public String getUserInstagram() {
        return userInstagram;
    }

    public void setUserInstagram(String userInstagram) {
        this.userInstagram = userInstagram;
    }

    public String getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(String userDetails) {
        this.userDetails = userDetails;
    }

}
