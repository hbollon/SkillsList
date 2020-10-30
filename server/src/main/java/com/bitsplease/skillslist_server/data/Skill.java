package com.bitsplease.skillslist_server.data;

import java.util.Objects;

public class Skill {
    private String skillName;
    private String skillDesc;

    public Skill() {
    }

    public Skill(String skillName, String skillDesc) {
        this.skillName = skillName;
        this.skillDesc = skillDesc;
    }

    public String getSkillName() {
        return this.skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillDesc() {
        return this.skillDesc;
    }

    public void setSkillDesc(String skillDesc) {
        this.skillDesc = skillDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Skill)) {
            return false;
        }
        Skill skill = (Skill) o;
        return Objects.equals(skillName, skill.skillName) && Objects.equals(skillDesc, skill.skillDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skillName, skillDesc);
    }

    @Override
    public String toString() {
        return "{" +
            " skillName='" + getSkillName() + "'" +
            ", skillDesc='" + getSkillDesc() + "'" +
            "}";
    }

}
