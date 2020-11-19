package com.bitsplease.skillslist_server.data;

public class Role {
    private int dbId;
    private String roleName;
    private boolean canValidate;
    private boolean canAddSkill;

    public Role() {}

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public Role(String roleName, boolean canValidate, boolean canAddSkill) {
        this.roleName = roleName;
        this.canValidate = canValidate;
        this.canAddSkill = canAddSkill;
    }

    public Role(int dbId, String roleName, boolean canValidate, boolean canAddSkill) {
        this.dbId = dbId;
        this.roleName = roleName;
        this.canValidate = canValidate;
        this.canAddSkill = canAddSkill;
    }

    public int getDbId() {
        return this.dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean getCanValidate() {
        return this.canValidate;
    }

    public void setCanValidate(boolean canValidate) {
        this.canValidate = canValidate;
    }

    public boolean getCanAddSkill() {
        return this.canAddSkill;
    }

    public void setCanAddSkill(boolean canAddSkill) {
        this.canAddSkill = canAddSkill;
    }
    
    @Override
    public String toString() {
        return "{" +
            " dbId='" + getDbId() + "'" +
            ", roleName='" + getRoleName() + "'" +
            ", canValidate='" + getCanValidate() + "'" +
            ", canAddSkill='" + getCanAddSkill() + "'" +
            "}";
    }

}
