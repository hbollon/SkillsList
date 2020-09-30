package data;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    private int dbId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Role userRole;
    private ArrayList<SkillBlock> skillsBlocks;
    
    public User(int dbId, String username, String password, String firstName, String lastName) {
        this.dbId = dbId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(int dbId, String username, String password, String firstName, String lastName, Role userRole, ArrayList<SkillBlock> skillsBlocks) {
        this.dbId = dbId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
        this.skillsBlocks = skillsBlocks;
    }
    

    public int getDbId() {
        return this.dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getUserRole() {
        return this.userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public ArrayList<SkillBlock> getSkillsBlocks() {
        return this.skillsBlocks;
    }

    public void setSkillsBlocks(ArrayList<SkillBlock> skillsBlocks) {
        this.skillsBlocks = skillsBlocks;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return dbId == user.dbId && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(userRole, user.userRole) && Objects.equals(skillsBlocks, user.skillsBlocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbId, username, password, firstName, lastName, userRole, skillsBlocks);
    }

    @Override
    public String toString() {
        return "{" +
            " dbId='" + getDbId() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", userRole='" + getUserRole() + "'" +
            ", skillsBlocks='" + getSkillsBlocks() + "'" +
            "}";
    }

}
