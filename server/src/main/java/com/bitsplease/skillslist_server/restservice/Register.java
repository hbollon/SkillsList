package com.bitsplease.skillslist_server.restservice;

public class Register {
    private final long id;
    private final String content;
    private final boolean success;

    public Register(long id, String content, boolean success) {
        this.id = id;
        this.content = content;
        this.success = success;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public boolean getSucess() {
        return success;
    }
}
