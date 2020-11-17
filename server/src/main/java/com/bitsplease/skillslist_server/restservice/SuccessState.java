package com.bitsplease.skillslist_server.restservice;

public class SuccessState {
    private final long id;
    private final boolean success;

    public SuccessState(long id, Boolean success) {
        this.id = id;
        this.success = success;
    }

    public long getId() {
        return id;
    }

    public boolean getSuccess() {
        return success;
    }
}
