package com.bitsplease.skillslist_server.restservice.model;

public class SkillRequest {
    public final String username;
    public final String skillblockName;
    public final String skillName;

    public SkillRequest() {
        this.username = null;
        this.skillblockName = null;
        this.skillName = null;
    }

    public SkillRequest(String username, String skillblockName, String skillName)
    {
        this.username = username;
        this.skillblockName = skillblockName;
        this.skillName = skillName;
    }

    @Override
    public String toString() {
        return "{" +
            " username='" + username + "'" +
            ", skillblockName='" + skillblockName + "'" +
            ", skillName='" + skillName + "'" +
            "}";
    }

}
