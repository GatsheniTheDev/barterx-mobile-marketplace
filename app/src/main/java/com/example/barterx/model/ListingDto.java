package com.example.barterx.model;

import java.util.List;

public class ListingDto {
    private String merchantId;
    private String title;
    private String category;
    private String condition;
    private String description;
    private double latitude;
    private double longitude;

    public ListingDto() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private List<String> listingImages;

    public void setListingImages(List<String> listingImages) {
        this.listingImages = listingImages;
    }

    public ListingDto(String title, String category, String condition, String description) {
        this.title = title;
        this.category = category;
        this.condition = condition;
        this.description = description;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getListingImages() {
        return listingImages;
    }
}
