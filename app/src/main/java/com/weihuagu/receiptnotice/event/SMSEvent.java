package com.weihuagu.receiptnotice.event;

public class SMSEvent {

    private String sendMobile;

    private String content;

    public String getSendMobile() {
        return sendMobile;
    }

    public void setSendMobile(String sendMobile) {
        this.sendMobile = sendMobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
