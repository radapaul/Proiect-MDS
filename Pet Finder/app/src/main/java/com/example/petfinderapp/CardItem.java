package com.example.petfinderapp;

public class CardItem {
    private String imageUrl;
    private String sector;
    private String description;

    public CardItem(String imageUrl, String sector, String description) {
        this.imageUrl = imageUrl;
        this.sector = sector;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSector() {
        return sector;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
