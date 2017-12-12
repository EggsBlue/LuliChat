package com.dd.entity;

public class SocketMsg  {
    private byte action;
    private Object body;


    public byte getAction() {
        return action;
    }

    public void setAction(byte action) {
        this.action = action;
    }

    public SocketMsg() {

    }

    public SocketMsg(byte action, Object body) {
        this.action = action;
        this.body = body;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
