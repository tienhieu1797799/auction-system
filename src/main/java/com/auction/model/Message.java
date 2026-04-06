package com.auction.model;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum MessageType {
        LOGIN, LOGOUT, GET_AUCTIONS, PLACE_BID, AUCTION_UPDATE, 
        ERROR, SUCCESS, USER_UPDATE, BID_RESULT
    }

    private MessageType type;
    private String content;
    private Object data;

    public Message(MessageType type, String content, Object data) {
        this.type = type;
        this.content = content;
        this.data = data;
    }

    public MessageType getType() { return type; }
    public String getContent() { return content; }
    public Object getData() { return data; }

    @Override
    public String toString() {
        return "Message{" + "type=" + type + ", content='" + content + '\'' + '}';
    }
}