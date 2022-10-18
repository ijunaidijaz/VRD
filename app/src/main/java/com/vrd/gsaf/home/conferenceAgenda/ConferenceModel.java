package com.vrd.gsaf.home.conferenceAgenda;

import android.graphics.drawable.Drawable;

public class ConferenceModel {

    private final String type;
    private final String title;
    private final String stage;
    private final String startAt;
    private final String endAt;
    private final Drawable speakerImage;
    private final String profile;
    private final String details;


    public ConferenceModel(String type, String title, String stage, String startAt, String endAt, Drawable speakerImage, String profile, String details) {
        this.type = type;
        this.title = title;
        this.stage = stage;
        this.startAt = startAt;
        this.endAt = endAt;
        this.speakerImage = speakerImage;
        this.profile = profile;
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getStage() {
        return stage;
    }

    public String getStartAt() {
        return startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public Drawable getSpeakerImage() {
        return speakerImage;
    }

    public String getProfile() {
        return profile;
    }

    public String getDetails() {
        return details;
    }
}
