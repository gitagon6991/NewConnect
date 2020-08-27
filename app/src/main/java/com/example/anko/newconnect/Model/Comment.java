package com.example.anko.newconnect.Model;

/**
 * Created by ANKO on 31/07/2020.
 */

public class Comment {

    private String comment;
    private String publisher;


    public Comment(String comment, String publisher) {
        this.comment = comment;
        this.publisher = publisher;
    }

    public Comment (){

    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
