package com.ly.lrpc.netty.handler.param;

public class ServerRequest {

    private long id;
    private Object content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
