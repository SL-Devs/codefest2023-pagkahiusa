package com.example.codefest_cdo.data;

public class AccountDetails {


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String user_id;
    public String fullname;
    public String type;
    public String address;
    public String mobile_number;
    public String email;
    public String points;
    public String totalPost;

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(String totalPost) {
        this.totalPost = totalPost;
    }

    public String profileLink;

    public AccountDetails(){

    }
    public String getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public AccountDetails(String user_id,String fullname, String type, String address, String mobile_number, String email, String points,String profileLink,String totalPost) {
        this.user_id = user_id;
        this.fullname = fullname;
        this.type = type;
        this.address = address;
        this.mobile_number = mobile_number;
        this.email = email;
        this.points = points;
        this.profileLink = profileLink;
        this.totalPost = totalPost;
    }

}
