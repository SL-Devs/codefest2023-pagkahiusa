package com.example.codefest_cdo.data;

public class CommentDetails {

    public String user_id;
    public String comment_id;
    public String problem_title;
    public String problem_details;
    public String name;
    public String post_id;
    public String message;
    public String points;

    public CommentDetails(){

    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public CommentDetails(String user_id, String comment_id, String problem_title, String problem_details, String name, String post_id, String message) {
        this.user_id = user_id;
        this.comment_id = comment_id;
        this.problem_title = problem_title;
        this.problem_details = problem_details;
        this.name = name;
        this.post_id = post_id;
        this.message = message;
    }
}
