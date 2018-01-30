package com.example.akash.myhighway1;

/**
 * Created by vishal on 24/12/17.
 */

public class Message {
    private String content,username,time;

    public Message(){
    }
    public Message(String content){
        this.content=content;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
