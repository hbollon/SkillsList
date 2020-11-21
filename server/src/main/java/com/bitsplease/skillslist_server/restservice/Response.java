package com.bitsplease.skillslist_server.restservice;

public class Response {
    private final long id;
    private final String content;

    public Response(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
