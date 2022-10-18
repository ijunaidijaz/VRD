package com.vrd.gsaf.home.homeTab.media;

public class MediaModel {

    private final String images;
    private String videoUrl;
    private String youtubeId;

    public MediaModel(String videoUrl, String youtubeId, String images) {
        this.videoUrl = videoUrl;
        this.youtubeId = youtubeId;
        this.images = images;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        images = images;
    }


}
