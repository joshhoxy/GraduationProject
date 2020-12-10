package com.example.graduationproject.ocr;

public class StoreData {
    private String store_name;
    private Float price_level;
    private Float rating;
    private String business_status;
    private String open_now;

    public StoreData(String store_name, Float price_level, Float rating, String business_status, String open_now) {
        this.store_name = store_name;
        this.price_level = price_level;
        this.rating = rating;
        this.business_status = business_status;
        this.open_now = open_now;

    }

    public String getStore_name() {
        return store_name;
    }

    public Float getPrice_level() {
        return price_level;
    }

    public Float getRating() {
        return rating;
    }

    public String getBusiness_status() {
        return business_status;
    }

    public String getOpen_now() {
        return open_now;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public void setPrice_level(Float price_level) {
        this.price_level = price_level;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setBusiness_status(String business_status) {
        this.business_status = business_status;
    }

    public void setOpen_now(String open_now) {
        this.open_now = open_now;
    }
}
