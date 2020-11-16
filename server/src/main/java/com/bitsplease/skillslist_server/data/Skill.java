package com.bitsplease.skillslist_server.data;

import java.util.Objects;

public class Skill {
    private int dbId;
    private String skillName;
    private String skillDesc;
    private int skillblockId;

    public Skill() {
    }

    public Skill(String skillName, String skillDesc) {
        this.skillName = skillName;
        this.skillDesc = skillDesc;
    }

    public Skill(int id, String skillName, String skillDesc) {
        this.skillName = skillName;
        this.skillDesc = skillDesc;
        this.skillblockId = id;
    }

    public Skill(int dbId, int id, String skillName, String skillDesc) {
        this.skillName = skillName;
        this.skillDesc = skillDesc;
        this.skillblockId = id;
        this.dbId = dbId;
    }

    public int getDbId() {
        return this.dbId;
    }

    public void setBlockName(int dbId) {
        this.dbId = dbId;
    }

    public int getSkillBlockId() {
        return this.skillblockId;
    }

    public void setSkillBlockId(int id) {
        this.skillblockId = id;
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
