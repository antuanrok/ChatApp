package com.example.chatapp.pojo;

public class Message {
    private String author;
    private String message;
    private long date;

    private String img_url;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Message() {
    }

    public Message(String author, String message, long date, String img_url) {
        this.author = author;
        this.message = message;
        this.date = date;
        this.img_url = img_url;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
