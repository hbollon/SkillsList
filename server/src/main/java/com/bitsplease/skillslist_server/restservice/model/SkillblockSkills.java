package com.bitsplease.skillslist_server.restservice.model;

import com.bitsplease.skillslist_server.data.Skill;
import com.bitsplease.skillslist_server.data.SkillBlock;

public class SkillblockSkills {
    public final SkillBlock skillblock;
    public final Skill[] skills;

    public SkillblockSkills() {
        this.skillblock = null;
        this.skills = null;
    }

    public SkillblockSkills(SkillBlock skillblock, Skill[] skills)
    {
        this.skillblock = skillblock;
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "{" +
            " skillblockName='" + skillblock.toString() + "'" +
            ", skill='" + skills.toString() + "'" +
            "}";
    }

}
