package com.example.gwswaporshop;

public class Item {

    private String id;
    private String name;
    private String description;
    private String price;
    private String condition;
    private String category;
    private String imageUrl;

    // Default constructor required for Firebase
    public Item() {
    }

    // Parametrized constructor
    public Item(String id, String name, String description, String price, String condition, String category, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    // Getter and setter methods
    public String getId() { return id; // Getter for the item's ID
    }

    public void setId(String id) {
        this.id = id; // Setter for the item's ID
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getCondition() {
        return condition;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
