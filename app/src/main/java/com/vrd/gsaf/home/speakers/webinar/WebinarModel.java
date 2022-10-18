package com.vrd.gsaf.home.speakers.webinar;

import android.graphics.drawable.Drawable;

public class WebinarModel {

    private final String type;
    private final String title;
    private final String stage;
    private final String startAt;
    private final String endAt;
    private final Drawable speakerImage;


    public WebinarModel(String title, String type, String stage, String startAt, String endAt, Drawable speakerImage) {
        this.type = type;
        this.title = title;
        this.stage = stage;
        this.startAt = startAt;
        this.endAt = endAt;
        this.speakerImage = speakerImage;
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
}
