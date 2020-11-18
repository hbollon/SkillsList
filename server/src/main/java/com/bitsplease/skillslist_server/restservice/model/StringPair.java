package com.bitsplease.skillslist_server.restservice.model;

public class StringPair {
    public final String first;
    public final String second;

    public StringPair() {
        this.first = null;
        this.second = null;
    }

    public StringPair(String first, String second)
    {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "StringPair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
