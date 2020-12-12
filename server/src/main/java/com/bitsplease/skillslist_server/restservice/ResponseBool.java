package com.bitsplease.skillslist_server.restservice;

public class ResponseBool {
    private final long id;
    private final boolean content;
    private final boolean error;

    public ResponseBool(long id, boolean content, boolean error) {
        this.id = id;
        this.content = content;
        this.error = error;
    }

    public long getId() {
        return id;
    }

    public boolean getContent() {
        return content;
    }

    public boolean getError() {
        return error;
    }
}
