package com.example.arjun.su_bca.Utils;

public class MessageChatGPT {
    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT = "bot";

    String message;
    String sendBy;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public MessageChatGPT(String message, String sendBy) {
        this.message = message;
        this.sendBy = sendBy;
    }
}
