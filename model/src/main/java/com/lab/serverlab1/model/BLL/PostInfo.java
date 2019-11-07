package com.lab.serverlab1.model.BLL;

import java.util.Date;

public class PostInfo {
    private String content;
    private Date date;
    private String senderUsername;
    private String receiverUsername;
    private boolean isPrivate;

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public PostInfo(String senderUsername, String receiverUsername, String content, Date date, boolean isPrivate){
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.content = content;
        this.date = date;
        this.isPrivate = isPrivate;
    }

    public PostInfo(){

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    @Override
    public String toString() {
        return "PostInfo{" +
                "content='" + content + '\'' +
                ", date=" + date +
                ", senderUsername='" + senderUsername + '\'' +
                ", receiverUsername='" + receiverUsername + '\'' +
                '}';
    }
}
