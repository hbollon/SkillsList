package com.bitsplease.skillslist_server.data;

import java.util.Objects;

public class Skill {
    private int dbId;
    private String skillName;
    private String skillDesc;
    private int skillblockId;
    private boolean autoValidate;
    private int validate = -1;

    public Skill() {
    }

    public Skill(String skillName, String skillDesc, boolean autoValidate) {
        this.skillName = skillName;
        this.skillDesc = skillDesc;
        this.autoValidate = autoValidate;
    }

    public Skill(int skillblockId, String skillName, String skillDesc, boolean autoValidate) {
        this.skillName = skillName;
        this.skillDesc = skillDesc;
        this.skillblockId = skillblockId;
        this.autoValidate = autoValidate;
    }

    public Skill(int dbId, int skillblockId, String skillName, String skillDesc, boolean autoValidate) {
        this.skillName = skillName;
        this.skillDesc = skillDesc;
        this.skillblockId = skillblockId;
        this.dbId = dbId;
        this.autoValidate = autoValidate;
    }

    public Skill(int dbId, int skillblockId, String skillName, String skillDesc, boolean autoValidate, int validate) {
        this.skillName = skillName;
        this.skillDesc = skillDesc;
        this.skillblockId = skillblockId;
        this.dbId = dbId;
        this.autoValidate = autoValidate;
        this.validate = validate;
    }

    public int getDbId() {
        return this.dbId;
    }

    public void setBlockName(int dbId) {
        this.dbId = dbId;
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

    public int getSkillblockId() {
        return this.skillblockId;
    }

    public void setSkillblockId(int skillblockId) {
        this.skillblockId = skillblockId;
    }

    public boolean getAutoValidate() {
        return this.autoValidate;
    }

    public void setAutoValidate(boolean autoValidate) {
        this.autoValidate = autoValidate;
    }

    public int getValidate() {
        return this.validate;
    }

    public void setValidate(int validate) {
        this.validate = validate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Skill)) {
            return false;
        }
        Skill skill = (Skill) o;
        return dbId == skill.dbId && Objects.equals(skillName, skill.skillName) && Objects.equals(skillDesc, skill.skillDesc) && skillblockId == skill.skillblockId && autoValidate == skill.autoValidate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbId, skillName, skillDesc, skillblockId, autoValidate, validate);
    }

    @Override
    public String toString() {
        return "{" +
            " dbId='" + getDbId() + "'" +
            ", skillName='" + getSkillName() + "'" +
            ", skillDesc='" + getSkillDesc() + "'" +
            ", skillblockId='" + getSkillblockId() + "'" +
            ", autoValidate='" + getAutoValidate() + "'" +
            ", validate='" + getValidate() + "'" +
            "}";
    }

}
