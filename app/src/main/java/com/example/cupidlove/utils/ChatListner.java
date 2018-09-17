package com.example.cupidlove.utils;

import com.example.cupidlove.model.ChatMessage;

/**
 * Created by Kaushal on 19-01-2018.
 */

public interface ChatListner {

    public void onChat(ChatMessage chatMessage,boolean isInsert);

    public void setEventInCalender(ChatMessage chatMessage);
}
