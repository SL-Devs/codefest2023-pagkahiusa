package com.example.codefest_cdo.data;

public class RewardsDetails {


    public String id;
    public String name;
    public String details;
    public String value;
    public String quantity;
    public String imageLink;

    public RewardsDetails(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
