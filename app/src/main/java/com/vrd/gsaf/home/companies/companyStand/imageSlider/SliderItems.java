package com.vrd.gsaf.home.companies.companyStand.imageSlider;

public class SliderItems {

    private String imageUrl;
    private String description;
    private int staticImage;

    public SliderItems(String imageUrl, String description) {
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public SliderItems(int staicIMage, String description) {
        this.staticImage = staicIMage;
        this.description = description;

    }

    public SliderItems(int staticImage) {
        this.staticImage = staticImage;
    }

    public int getStaticImage() {
        return staticImage;
    }

    public void setStaticImage(int staticImage) {
        this.staticImage = staticImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
