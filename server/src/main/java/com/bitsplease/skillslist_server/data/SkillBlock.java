package com.bitsplease.skillslist_server.data;

import java.util.ArrayList;
import java.util.Objects;

import com.bitsplease.skillslist_server.utils.Pair;

public class SkillBlock {
    private int dbId;
    private String blockName;
    private String blockDesc;
    private ArrayList<Pair<Skill, Boolean>> skills;

    public SkillBlock() {}
    
    public SkillBlock(String blockName, String blockDesc) {
        this.blockName = blockName;
        this.blockDesc = blockDesc;
        this.skills = new ArrayList<Pair<Skill, Boolean>>();
    }

    public SkillBlock(int dbId, String blockName) {
        this.dbId = dbId;
        this.blockName = blockName;
    }

    public SkillBlock(int dbId, String blockName, String blockDesc) {
        this.dbId = dbId;
        this.blockName = blockName;
        this.blockDesc = blockDesc;
        this.skills = new ArrayList<Pair<Skill, Boolean>>();
    }

    public SkillBlock(String blockName, String blockDesc, ArrayList<Pair<Skill,Boolean>> skills) {
        this.blockName = blockName;
        this.blockDesc = blockDesc;
        this.skills = skills;
    }

    public int getDbId() {
        return this.dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getBlockName() {
        return this.blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getBlockDesc() {
        return this.blockDesc;
    }

    public void setBlockDesc(String blockDesc) {
        this.blockDesc = blockDesc;
    }

    public ArrayList<Pair<Skill,Boolean>> getSkills() {
        return this.skills;
    }

    public void setSkills(ArrayList<Pair<Skill,Boolean>> skills) {
        this.skills = skills;
    }

    public int getNumberOfSkills() {
        return this.skills.size();
    }

    public float getCompletionProgress() {
        int completed = 0;
        for (Pair<Skill, Boolean> p : skills) {
            if (p.second) ++completed;
        }
        return ((float) completed) / ((float) this.getNumberOfSkills());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SkillBlock)) {
            return false;
        }
        SkillBlock skillBlock = (SkillBlock) o;
        return dbId == skillBlock.dbId && Objects.equals(blockName, skillBlock.blockName) && Objects.equals(blockDesc, skillBlock.blockDesc) && Objects.equals(skills, skillBlock.skills);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbId, blockName, blockDesc, skills);
    }

    @Override
    public String toString() {
        return "{" +
            " dbId='" + dbId + "'" +
            ", blockName='" + blockName + "'" +
            ", blockDesc='" + blockDesc + "'" +
            ", skills='" + skills + "'" +
            "}";
    }


}
