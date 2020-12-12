package com.bitsplease.skillslist_server.restservice.model;

import com.bitsplease.skillslist_server.data.Skill;

public class SkillUser {
    public final String username;
    public final Skill skill;

    public SkillUser() {
        this.username = null;
        this.skill = null;
    }

    public SkillUser(String username, Skill skill)
    {
        this.username = username;
        this.skill = skill;
    }

    @Override
    public String toString() {
        return "{" +
            " skillblockName='" + username + "'" +
            ", skill='" + skill + "'" +
            "}";
    }

}
