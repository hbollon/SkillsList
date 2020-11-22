package com.bitsplease.skillslist_server.restservice.model;

import com.bitsplease.skillslist_server.data.Skill;

public class SkillCrud {
    public final String skillblockName;
    public final Skill skill;

    public SkillCrud() {
        this.skillblockName = null;
        this.skill = null;
    }

    public SkillCrud(String skillblockName, Skill skill)
    {
        this.skillblockName = skillblockName;
        this.skill = skill;
    }

    @Override
    public String toString() {
        return "{" +
            " skillblockName='" + skillblockName + "'" +
            ", skill='" + skill + "'" +
            "}";
    }

}
