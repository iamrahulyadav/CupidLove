package com.example.cupidlove.model;

/**
 * Created by Bhumi Shah on 4/28/2017.
 */

public class ChatMessage {


    public int messagetype;
    public String receiver, sender;
    public int msgid;
    public String body;
    public String mdate;
    public String mtime;
    public String dateid;
    public String userId;
    public String placeIdstr;
    public boolean isUpdate = false;
    public boolean isRead = false;


    public ChatMessage() {

    }

    public ChatMessage(int msgid, boolean isRead) {
        this.msgid = msgid;
        this.isRead = isRead;
    }

    public ChatMessage(String fromUser, String toUser, String message, int messagetype, String date, String time, String dateid, String userId, boolean isRead) {

        sender = fromUser;
        receiver = toUser;
        body = message;
        this.messagetype = messagetype;
        mdate = date;
        mtime = time;
        this.dateid = dateid;
        this.userId = userId;
        this.isRead = isRead;

    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
