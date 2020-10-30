package com.bitsplease.skillslist_server.data;

import java.util.Objects;

public class Role {
    private String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Role)) {
            return false;
        }
        Role role = (Role) o;
        return Objects.equals(roleName, role.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roleName);
    }

    @Override
    public String toString() {
        return "{" +
            " roleName='" + getRoleName() + "'" +
            "}";
    }

}
