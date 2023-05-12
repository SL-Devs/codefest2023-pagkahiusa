package com.example.codefest_cdo.data;

public class EFHDetails {

    public String post_id;


    public String user_id;
    public String fullname;
    public String typeOfUser;
    public String problem_title;
    public String problem_details;
    public String problem_type;
    public String mobile_number;
    public String date;
    public String votes;
    public String imageLink;
    public String postImageLink;

    public EFHDetails(){

    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPostImageLink() {
        return postImageLink;
    }

    public void setPostImageLink(String postImageLink) {
        this.postImageLink = postImageLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(String typeOfUser) {
        this.typeOfUser = typeOfUser;
    }

    public String getProblem_title() {
        return problem_title;
    }

    public void setProblem_title(String problem_title) {
        this.problem_title = problem_title;
    }

    public String getProblem_details() {
        return problem_details;
    }

    public void setProblem_details(String problem_details) {
        this.problem_details = problem_details;
    }

    public String getProblem_type() {
        return problem_type;
    }

    public void setProblem_type(String problem_type) {
        this.problem_type = problem_type;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

//    public String getOthers() {
//        return others;
//    }
//
//    public void setOthers(String others) {
//        this.others = others;
//    }

    public EFHDetails(String user_id,String post_id, String fullname, String typeOfUser, String problem_title,
                      String problem_details, String problem_type, String mobile_number,String date,String imageLink,String postImageLink,String votes){
        this.user_id = user_id;
        this.post_id = post_id;
        this.fullname = fullname;
        this.typeOfUser = typeOfUser;
        this.problem_title = problem_title;
        this.problem_details = problem_details;
        this.problem_type = problem_type;
        this.mobile_number = mobile_number;
        this.date = date;
        this.imageLink = imageLink;
        this.postImageLink = postImageLink;
        this.votes = votes;
    }

}
