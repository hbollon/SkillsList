package data;

import java.util.ArrayList;
import java.util.Objects;

import utils.Pair;

public class SkillBlock {
    private String blockName;
    private String blockDesc;
    private ArrayList<Pair<Skill, Boolean>> skills;


    public SkillBlock(String blockName, String blockDesc, ArrayList<Pair<Skill,Boolean>> skills) {
        this.blockName = blockName;
        this.blockDesc = blockDesc;
        this.skills = skills;
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SkillBlock)) {
            return false;
        }
        SkillBlock skillBlock = (SkillBlock) o;
        return Objects.equals(blockName, skillBlock.blockName) && Objects.equals(blockDesc, skillBlock.blockDesc) && Objects.equals(skills, skillBlock.skills);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockName, blockDesc, skills);
    }


    @Override
    public String toString() {
        return "{" +
            " blockName='" + getBlockName() + "'" +
            ", blockDesc='" + getBlockDesc() + "'" +
            ", skills='" + getSkills() + "'" +
            "}";
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
}
