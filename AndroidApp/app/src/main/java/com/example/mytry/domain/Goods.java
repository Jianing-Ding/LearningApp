package com.example.mytry.domain;

public class Goods {
    private String name;
    private String description;
    private String imagePath;
    private int price;

    public Goods(String name, String description, String imagePath, int price) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.price = price;
    }

    public Goods() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
