package com.bitsplease.skillslist_server.restservice.model;

public class SkillValidation {
    public final String connectedUsername;
    public final String username;
    public final String skillName;
    public final String skillblockName;

    public SkillValidation() {
        this.connectedUsername = null;
        this.username = null;
        this.skillName = null;
        this.skillblockName = null;
    }

    public SkillValidation(String connectedUsername, String username, String skillName, String skillblockName)
    {
        this.connectedUsername = connectedUsername;
        this.username = username;
        this.skillblockName = skillblockName;
        this.skillName = skillName;
    }

    @Override
    public String toString() {
        return "{" +
            " connectedUsername='" + connectedUsername + "'" +
            ", username='" + username + "'" +
            ", skillName='" + skillName + "'" +
            ", skillblockName='" + skillblockName + "'" +
            "}";
    }

}
