package com.example.anko.newconnect.Model;

/**
 * Created by ANKO on 22/07/2020.
 */

public class Message {
    private String content,username,imageurl;

    public Message(){}
    public Message(String content, String username, String image){
        this.content = content;
        this.username = username;
        this.imageurl = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String image) {
        this.imageurl = image;
    }
}
