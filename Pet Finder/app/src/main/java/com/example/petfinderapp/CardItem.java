package com.example.petfinderapp;

public class CardItem {
    private String imageUrl;
    private String sector;
    private String description;

    // Constructor
    public CardItem(String imageUrl, String sector, String description) {
        this.imageUrl = imageUrl;
        this.sector = sector;
        this.description = description;
    }

    // Getter methods
    public String getImageUrl() {
        return imageUrl;
    }

    public String getSector() {
        return sector;
    }

    public String getDescription() {
        return description;
    }
}
