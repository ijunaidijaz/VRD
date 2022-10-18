package com.vrd.gsaf.api.responses.speakers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Speaker {

    @SerializedName("speaker_id")
    @Expose
    private Integer speakerID;
    @SerializedName("fair_id")
    @Expose
    private Integer fairId;
    @SerializedName("speaker_name")
    @Expose
    private String name;
    @SerializedName("speaker_email")
    @Expose
    private String email;
    @SerializedName("speaker_title")
    @Expose
    private String title;
    @SerializedName("speaker_description")
    @Expose
    private String description;
    @SerializedName("speaker_website")
    @Expose
    private String website;
    @SerializedName("speaker_facebook")
    @Expose
    private String facebook;
    @SerializedName("speaker_type")
    @Expose
    private String type;
    @SerializedName("speaker_youtube")
    @Expose
    private String youtube;
    @SerializedName("speaker_twitter")
    @Expose
    private String twitter;
    @SerializedName("speaker_linkedin")
    @Expose
    private String linkedin;
    @SerializedName("speaker_instagram")
    @Expose
    private String instagram;
    @SerializedName("speaker_image")
    @Expose
    private String image;

    public Integer getId() {
        return speakerID;
    }

    public void setId(Integer id) {
        this.speakerID = id;
    }

    public Integer getFairId() {
        return fairId;
    }

    public void setFairId(Integer fairId) {
        this.fairId = fairId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
