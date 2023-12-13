package com.example.gwswaporshop;

public class Order {
    private String itemName;
    private String itemPrice;
    private String itemDescription;
    private String itemCondition;
    private String paymentMethod;
    private String pickupLocation;
    private String itemImageUrl;

    // Default constructor is needed for Firebase
    public Order() {
    }

    // Parametrized constructor
    public Order(String itemName, String itemPrice, String itemDescription, String itemCondition, String paymentMethod, String pickupLocation) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
        this.itemCondition = itemCondition;
        this.paymentMethod = paymentMethod;
        this.pickupLocation = pickupLocation;
        this.itemImageUrl = itemImageUrl;
    }

    // Getters and setters
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }
    public String getItemImageUrl() {
        return itemImageUrl;
    }
    public void setItemImageUrl(String itemImageUrl) { this.itemImageUrl = itemImageUrl; }
}
