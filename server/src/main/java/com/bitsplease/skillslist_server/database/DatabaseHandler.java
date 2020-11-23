package com.bitsplease.skillslist_server.database;

import com.bitsplease.skillslist_server.data.Role;
import com.bitsplease.skillslist_server.data.Skill;
import com.bitsplease.skillslist_server.data.SkillBlock;
import com.bitsplease.skillslist_server.data.User;
import com.bitsplease.skillslist_server.utils.HashUtils;

import java.sql.*;
import java.util.ArrayList;

/**
 * DatabaseHandler
 */
public class DatabaseHandler {
    private final String dbUrl = "jdbc:mysql://localhost:3306/skillslist";
    private final String user = "testuser";
    private final String password = "password";
    private final String urlOptions = "&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    
    private final String ROLE_TABLE_NAME = "role";
    private final String ROLE_DB_NAME = "name";
    private final String ROLE_DB_CAN_VALIDATE = "can_validate";
    private final String ROLE_DB_CAN_ADD_SKILL = "can_add_skill";
    private final String ROLE_DB_SQL = "CREATE TABLE IF NOT EXISTS " + ROLE_TABLE_NAME + "(id INT NOT NULL AUTO_INCREMENT, "
            + ROLE_DB_NAME + " VARCHAR(255), " + ROLE_DB_CAN_VALIDATE + " BOOLEAN, " + ROLE_DB_CAN_ADD_SKILL + " BOOLEAN,"
            + " PRIMARY KEY ( id ))";

    private final String USER_TABLE_NAME = "user";
    private final String USER_DB_USERNAME = "username";
    private final String USER_DB_PASSWORD = "password";
    private final String USER_DB_PASSWORD_SALT = "salt";
    private final String USER_DB_FIRSTNAME = "first_name";
    private final String USER_DB_LASTNAME = "last_name";
    private final String USER_DB_ROLE = "role_id";
    private final String USER_DB_SQL = "CREATE TABLE IF NOT EXISTS user " + "(id INT NOT NULL AUTO_INCREMENT, "
            + " username VARCHAR(255), " + " password VARCHAR(255), " + "salt VARCHAR(255),"
            + " first_name VARCHAR(255), " + " last_name VARCHAR(255), " + " role_id INT," + " PRIMARY KEY ( id ), "
            + "FOREIGN KEY(" + USER_DB_ROLE + ") REFERENCES " + ROLE_TABLE_NAME + "(id))";

    private final String SKILLBLOCK_TABLE_NAME = "skillblock";
    private final String SKILLBLOCK_DB_NAME = "name";
    private final String SKILLBLOCK_DB_DESC = "description";
    private final String SKILLBLOCK_DB_SQL = "CREATE TABLE IF NOT EXISTS skillblock "
            + "(id INT NOT NULL AUTO_INCREMENT, " + " name VARCHAR(255), " + " description VARCHAR(255), "
            + " PRIMARY KEY ( id ))";

    private final String SKILL_TABLE_NAME = "skill";
    private final String SKILL_DB_NAME = "name";
    private final String SKILL_DB_DESC = "description";
    private final String SKILL_SKILLBLOCK_ID = "skillblock_id";
    private final String SKILL_AUTO_VALIDATE = "auto_validation";
    private final String SKILL_DB_SQL = "CREATE TABLE IF NOT EXISTS " + SKILL_TABLE_NAME
            + " ( id INT NOT NULL AUTO_INCREMENT, " + "name VARCHAR(255), " + "description VARCHAR(255), "
            + "skillblock_id INT, " + SKILL_AUTO_VALIDATE + " BOOLEAN, PRIMARY KEY ( id ), FOREIGN KEY(" + SKILL_SKILLBLOCK_ID + ") REFERENCES "
            + SKILLBLOCK_TABLE_NAME + "(id))";

    private final String USER_SKILLBLOCKS_TABLE_NAME = "user_skillblock";
    private final String USER_SKILLBLOCKS_USER_ID = "user_id";
    private final String USER_SKILLBLOCKS_SKILLBLOCK_ID = "skillblock_id";
    private final String USER_SKILLBLOCKS_DB_SQL = "CREATE TABLE IF NOT EXISTS " + USER_SKILLBLOCKS_TABLE_NAME
            + " ( id INT NOT NULL AUTO_INCREMENT, " + "user_id INT, " + "skillblock_id INT, " + "PRIMARY KEY ( id ), "
            + "FOREIGN KEY(" + USER_SKILLBLOCKS_USER_ID + ") REFERENCES " + USER_TABLE_NAME + "(id), " + "FOREIGN KEY("
            + USER_SKILLBLOCKS_SKILLBLOCK_ID + ") REFERENCES " + SKILLBLOCK_TABLE_NAME + "(id))";

    private final String USER_SKILLS_TABLE_NAME = "user_skill";
    private final String USER_SKILLS_USER_ID = "user_id";
    private final String USER_SKILLS_SKILL_ID = "skill_id";
    private final String USER_SKILLS_SKILL_STATUS = "status";
    private final String USER_SKILLS_DB_SQL = "CREATE TABLE IF NOT EXISTS " + USER_SKILLS_TABLE_NAME
            + " ( id INT NOT NULL AUTO_INCREMENT, " + "user_id INT, " + "skill_id INT, " + "status INT, "
            + "PRIMARY KEY ( id ), " + "FOREIGN KEY(" + USER_SKILLS_USER_ID + ") REFERENCES " + USER_TABLE_NAME
            + "(id), " + "FOREIGN KEY(" + USER_SKILLS_SKILL_ID + ") REFERENCES " + SKILL_TABLE_NAME + "(id))";

    private final String[] TABLES = { ROLE_TABLE_NAME, USER_TABLE_NAME, SKILLBLOCK_TABLE_NAME, SKILL_TABLE_NAME,
            USER_SKILLBLOCKS_TABLE_NAME, USER_SKILLS_TABLE_NAME };
    private final String[] TABLES_SQL = { ROLE_DB_SQL, USER_DB_SQL, SKILLBLOCK_DB_SQL, SKILL_DB_SQL, USER_SKILLBLOCKS_DB_SQL,
            USER_SKILLS_DB_SQL };

    private Connection conn;
    private Statement statement;

    public DatabaseHandler() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(dbUrl + "?user=" + user + "&password=" + password + urlOptions);
            initDb();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initDb() throws SQLException {
        statement = conn.createStatement();
        for (String sql : TABLES_SQL) {
            statement.executeUpdate(sql);
        }
    }

    /**
     * Clear all tables of the db
     */
    private void clearAllTables() {
        try {
            statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
            for (String table : TABLES) {
                statement.executeUpdate("TRUNCATE " + table);
            }
            statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dropAllTables() {
        try {
            statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
            for (String table : TABLES) {
                statement.executeUpdate("DROP TABLE " + table);
            }
            statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
            initDb();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetAll(boolean debug) {
        if(debug)
            dropAllTables();
        else
            clearAllTables();
    }

    /**
     * Role Interactions
     */

    public boolean insertRole(Role r) {
        System.out.println("Trying to add new role...");
        try {
            String check_existing_request = "SELECT * FROM " + ROLE_TABLE_NAME + " WHERE `" + ROLE_DB_NAME + "`='" + r.getRoleName() + "'";
            try(ResultSet rs = statement.executeQuery(check_existing_request)){
                if(rs.next()) {
                    System.out.println("Error: Role already exists !");
                    return false;
                }
            }

            String sql = "INSERT INTO " + ROLE_TABLE_NAME + "(`" + ROLE_DB_NAME + "`, `" + ROLE_DB_CAN_VALIDATE + "`, `"
            + ROLE_DB_CAN_ADD_SKILL + "`) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, r.getRoleName());
            statement.setBoolean(2, r.getCanValidate());
            statement.setBoolean(3, r.getCanAddSkill());

            int result = statement.executeUpdate();
            if (result > 0) {
                System.out.println("A new role was inserted successfully!");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRole(Role r) {
        try {
            String sql = "UPDATE " + ROLE_TABLE_NAME + " SET " + ROLE_DB_NAME + "=?, " + ROLE_DB_CAN_VALIDATE + "=?, "
                    + ROLE_DB_CAN_ADD_SKILL + "=? WHERE " + ROLE_DB_NAME + "=?";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, r.getRoleName());
            statement.setBoolean(2, r.getCanValidate());
            statement.setBoolean(3, r.getCanAddSkill());
            statement.setString(4, r.getRoleName());

            int result = statement.executeUpdate();
            if (result > 0) {
                System.out.println("The role was updated successfully!");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Role getRole(String name) {
        String sql = "SELECT * FROM " + ROLE_TABLE_NAME + " WHERE `" + ROLE_DB_NAME + "`='" + name + "'";
        try (ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                return new Role(rs.getInt(1), rs.getString(2), rs.getBoolean(3), rs.getBoolean(4));
            }
            System.out.println("Role not recognized!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Role getRoleById(int id) {
        String sql = "SELECT * FROM " + ROLE_TABLE_NAME + " WHERE `id`='" + id + "'";
        try (ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                return new Role(rs.getInt(1), rs.getString(2), rs.getBoolean(3), rs.getBoolean(4));
            }
            System.out.println("Role not recognized!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Role[] getAllRoles() {
        String sql = "SELECT * FROM " + ROLE_TABLE_NAME;
        ArrayList<Role> roles = new ArrayList<Role>();
        try(ResultSet rs = statement.executeQuery(sql)) {
            while(rs.next()){
                roles.add(new Role(rs.getInt(1), rs.getString(2), rs.getBoolean(3), rs.getBoolean(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        Role[] output = roles.toArray(new Role[roles.size()]);
        return output;
    }

    /**
     * Users Interactions
     */

    public boolean insertUser(User u) {
        System.out.println("Trying to register new user...");
        try {
            String check_existing_request = "SELECT * FROM user WHERE `" + USER_DB_USERNAME + "`='" + u.getUsername() + "'";
            try(ResultSet rs = statement.executeQuery(check_existing_request)){
                if(rs.next()) {
                    System.out.println("Error: User already exists !");
                    return false;
                }
            }

            String sql = "INSERT INTO " + USER_TABLE_NAME + "(`" + USER_DB_USERNAME + "`, `" + USER_DB_PASSWORD + "`, `"
            + USER_DB_PASSWORD_SALT + "`, `" + USER_DB_FIRSTNAME + "`, `" + USER_DB_LASTNAME + "`, `" + USER_DB_ROLE + "`) VALUES (?, ?, ?, ?, ?, ?)";

            if(u.getDbId() == 0) {
                u.setUserRole(getRole(u.getUserRole().getRoleName()));
            }

            String salt = HashUtils.getSalt(30);

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, u.getUsername());
            statement.setString(2, HashUtils.generateSecurePassword(u.getPassword(), salt));
            statement.setString(3, salt);
            statement.setString(4, u.getFirstName());
            statement.setString(5, u.getLastName());
            statement.setInt(6, u.getUserRole().getDbId());

            int result = statement.executeUpdate();
            if (result > 0) {
                System.out.println("A new user was inserted successfully!");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(User u) {
        try {
            String sql = "UPDATE " + USER_TABLE_NAME + " SET " + USER_DB_USERNAME + "=?, " + USER_DB_FIRSTNAME + "=?, "
                    + USER_DB_LASTNAME + "=?, " + USER_DB_ROLE + "=? WHERE " + USER_DB_USERNAME + "=?";

            if(u.getDbId() == 0) {
                u.setUserRole(getRole(u.getUserRole().getRoleName()));
            }

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, u.getUsername());
            statement.setString(2, u.getFirstName());
            statement.setString(3, u.getLastName());
            statement.setInt(4, u.getUserRole().getDbId());
            statement.setString(5, u.getUsername());

            int result = statement.executeUpdate();
            if (result > 0) {
                System.out.println("The user was updated successfully!");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String username) {
        String sql = "DELETE FROM user WHERE `" + USER_DB_USERNAME + "`='" + username + "'";
        try {
            int result = statement.executeUpdate(sql);
            if (result > 0) {
                System.out.println("The user was deleted successfully!");
                return true;
            } else {
                System.out.println("User delete failed!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getUserId(String login) {
        String sql = "SELECT * FROM user WHERE `" + USER_DB_USERNAME + "`='" + login + "'";
        try (ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            System.out.println("User not find!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private User getUser(String login) {
        String sql = "SELECT * FROM user WHERE `" + USER_DB_USERNAME + "`='" + login + "'";
        try (ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                return new User(rs.getInt(1), rs.getString(2), rs.getString(5), rs.getString(6), getRoleById(rs.getInt(7)));
            }
            System.out.println("User not recognized!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Role getUserRole(String username) {
        User user = getUser(username);
        if(user == null)
            return null;
        return user.getUserRole();
    }

    public User connectUser(String login, String password) {
        String sql = "SELECT * FROM user WHERE `" + USER_DB_USERNAME + "`='" + login + "'";
        try (ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                if (HashUtils.verifyUserPassword(password, rs.getString(3), rs.getString(4))) {
                    System.out.println("User succesfully logged in!");
                    return new User(rs.getInt(1), rs.getString(2), rs.getString(5), rs.getString(6), getRoleById(rs.getInt(7)));
                }
            }
            System.out.println("User not recognized!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean subscribeSkillBlock(String u, String s) {
        System.out.println("Trying to subscribe to new skillblock...");
        try {
            int userId = getUserId(u);
            if (userId == -1) {
                System.out.println("Error: User don't exist !");
                return false;
            }

            SkillBlock skillblock = getSkillBlock(s);
            if (skillblock == null) {
                System.out.println("Error: Skillblock don't exist !");
                return false;
            }

            String check_existing_request = "SELECT * FROM " + USER_SKILLBLOCKS_TABLE_NAME + " WHERE `"
                    + USER_SKILLBLOCKS_USER_ID + "`='" + userId + "' AND `" + USER_SKILLBLOCKS_SKILLBLOCK_ID + "`='"
                    + skillblock.getDbId() + "'";
            try(ResultSet rs = statement.executeQuery(check_existing_request)) {
                if (rs.next()) {
                    System.out.println("Error: User already subscribed to this skillblock !");
                    return false;
                }
            }

            String sql = "INSERT INTO " + USER_SKILLBLOCKS_TABLE_NAME + "(`" + USER_SKILLBLOCKS_USER_ID + "`, `"
                    + USER_SKILLBLOCKS_SKILLBLOCK_ID + "`) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, skillblock.getDbId());

            int result = statement.executeUpdate();
            if (result > 0) {
                System.out.println("A new skillblock was linked to current user successfully!");
                return true;
            } else {
                System.out.println("Skillblock subscribe insert failed!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean unsubscribeSkillBlock(String u, String s) {
        System.out.println("Trying to unsubscribe to skillblock...");
        try {
            int userId = getUserId(u);
            if(userId == -1) {
                System.out.println("Error: User don't exist !");
                return false;
            }

            SkillBlock skillblock = getSkillBlock(s);
            if(skillblock == null) {
                System.out.println("Error: Skillblock don't exist !");
                return false;
            }

            String sql = "DELETE FROM " + USER_SKILLBLOCKS_TABLE_NAME + " WHERE `" + 
            USER_SKILLBLOCKS_USER_ID + "`='" + userId + "' AND `" +
            USER_SKILLBLOCKS_SKILLBLOCK_ID + "`='" + skillblock.getDbId() + "'";

            int result = statement.executeUpdate(sql);
            if (result > 0) {
                System.out.println("Unsubscribed skillblock to current user successfully!");
                return true;
            } else {
                System.out.println("Skillblock link delete failed!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * SkillBlock Interactions
     */

    public boolean insertSkillBlock(SkillBlock s) {
        System.out.println("Trying to insert new skillblock...");
        String check_existing_request = "SELECT * FROM skillblock WHERE `" + SKILLBLOCK_DB_NAME + "`='" + s.getBlockName() + "'";
        try(ResultSet rs = statement.executeQuery(check_existing_request);) {
            if(rs.next()) {
                System.out.println("Error: SkillBlock already exists !");
                return false;
            }
            String sql = "INSERT INTO " + SKILLBLOCK_TABLE_NAME + "(`" + SKILLBLOCK_DB_NAME + "`, `" + SKILLBLOCK_DB_DESC + "`) VALUES (?, ?)";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, s.getBlockName());
            statement.setString(2, s.getBlockDesc());

            int result = statement.executeUpdate();
            if (result > 0) {
                System.out.println("A new skillblock was inserted successfully!");
                return true;
            } else {
                System.out.println("Skillblock insert failed!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSkillBlock(SkillBlock s) {
        try {
            String sql = "UPDATE " + SKILLBLOCK_TABLE_NAME + " SET " + SKILLBLOCK_DB_NAME + "=?, "
            + SKILLBLOCK_DB_DESC + "=? WHERE `" + SKILLBLOCK_DB_NAME + "`='" + s.getBlockName() + "'";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, s.getBlockName());
            statement.setString(2, s.getBlockDesc());

            int result = statement.executeUpdate();
            if (result > 0) {
                System.out.println("The skillblock was updated successfully!");
                return true;
            } else {
                System.out.println("SkillBlock update failed!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSkillBlock(String blockname){
        String sql = "DELETE FROM " + SKILLBLOCK_TABLE_NAME + " WHERE `" + SKILLBLOCK_DB_NAME + "`='" + blockname + "'";
        try {
            int result = statement.executeUpdate(sql);
            if (result > 0) {
                System.out.println("The skillblock was deleted successfully!");
                return true;
            } else {
                System.out.println("SkillBlock delete failed!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public SkillBlock getSkillBlock(String name) {
        String sql = "SELECT * FROM " + SKILLBLOCK_TABLE_NAME + " WHERE `" + SKILLBLOCK_DB_NAME + "`='" + name + "'";
        try(ResultSet rs = statement.executeQuery(sql);) {
            if(rs.next()){
                return new SkillBlock(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
            System.out.println("Requested skillblock doesn't exist!");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public SkillBlock getSkillBlockById(int id) {
        String sql = "SELECT * FROM " + SKILLBLOCK_TABLE_NAME + " WHERE `id`='" + id + "'";
        try(ResultSet rs = statement.executeQuery(sql);) {
            if(rs.next()){
                return new SkillBlock(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
            System.out.println("Requested skillblock doesn't exist!");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public SkillBlock[] getAllSkillBlock() {
        String sql = "SELECT * FROM " + SKILLBLOCK_TABLE_NAME;
        ArrayList<SkillBlock> skillblocks = new ArrayList<SkillBlock>();
        try(ResultSet rs = statement.executeQuery(sql)) {
            while(rs.next()){
                skillblocks.add(new SkillBlock(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        SkillBlock[] output = skillblocks.toArray(new SkillBlock[skillblocks.size()]);
        return output;
    }

    public SkillBlock[] getSubscribedSkillBlock(int userId) {
        String sql = "SELECT * FROM " + USER_SKILLBLOCKS_TABLE_NAME + " WHERE `" + USER_SKILLBLOCKS_USER_ID + "`=" + userId;
        ArrayList<SkillBlock> skillblocks = new ArrayList<SkillBlock>();
        try(
            Statement st = conn.createStatement(); 
            ResultSet rs = st.executeQuery(sql)) 
        {
            while(rs.next()){
                skillblocks.add(getSkillBlockById(rs.getInt(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        SkillBlock[] output = skillblocks.toArray(new SkillBlock[skillblocks.size()]);
        return output;
    }

    /**
     * Skill Interactions
     */

    public boolean insertSkill(String skillblockName, Skill skill) {
        System.out.println("Trying to insert new skill...");
        try {
            SkillBlock associatedSkillBlock = getSkillBlock(skillblockName);
            if(associatedSkillBlock == null){
                System.out.println("Error: Associated SkillBlock not found !");
                return false;    
            }

            String check_existing_request = "SELECT * FROM skill WHERE `" + SKILL_DB_NAME + "`='" + skill.getSkillName() + "' AND `" + SKILL_SKILLBLOCK_ID + "`='" + associatedSkillBlock.getDbId() + "'";
            try(ResultSet rs = statement.executeQuery(check_existing_request)){
                if(rs.next()) {
                    System.out.println("Error: Skill already exists in this SkillBlock !");
                    return false;
                }
            }

            String sql = "INSERT INTO " + SKILL_TABLE_NAME + "(`" + SKILL_DB_NAME + "`, `" + SKILL_DB_DESC + "`, `" + SKILL_SKILLBLOCK_ID + "`, `" + SKILL_AUTO_VALIDATE + "`) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, skill.getSkillName());
            statement.setString(2, skill.getSkillDesc());
            statement.setInt(3, associatedSkillBlock.getDbId());
            statement.setBoolean(4, skill.getAutoValidate());

            int result = statement.executeUpdate();
            if (result > 0) {
                System.out.println("A new skill was inserted successfully!");
                return true;
            } else {
                System.out.println("Skill insert failed!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSkill(String sbName, Skill skill) {
        try {
            SkillBlock skillblock = getSkillBlock(sbName);

            String sql = "UPDATE " + SKILL_TABLE_NAME + " SET " + 
            SKILL_DB_NAME + "=?, " + 
            SKILL_DB_DESC + "=?, " + 
            SKILL_AUTO_VALIDATE + "=? WHERE `" + SKILL_DB_NAME + "`='" + skill.getSkillName() + "' AND `" + SKILL_SKILLBLOCK_ID + "`='" + skillblock.getDbId() + "'";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, skill.getSkillName());
            statement.setString(2, skill.getSkillDesc());
            statement.setBoolean(3, skill.getAutoValidate());

            int result = statement.executeUpdate();
            if (result > 0) {
                System.out.println("The skill was updated successfully!");
                return true;
            } else {
                System.out.println("Skill update failed!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSkill(String sbName, String skillname){
        SkillBlock skillblock = getSkillBlock(sbName);

        String sql = "DELETE FROM " + SKILL_TABLE_NAME + " WHERE `" + SKILL_DB_NAME + "`='" + skillname + "' AND `" + SKILL_SKILLBLOCK_ID + "`='" + skillblock.getDbId() + "'";
        try {
            int result = statement.executeUpdate(sql);
            if (result > 0) {
                System.out.println("The skill was deleted successfully!");
                return true;
            } else {
                System.out.println("Skill delete failed!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSkill(String sbName, Skill skill){
        SkillBlock skillblock = getSkillBlock(sbName);

        String sql = "DELETE FROM " + SKILL_TABLE_NAME + " WHERE `" + SKILL_DB_NAME + "`='" + skill.getSkillName() + "' AND `" + SKILL_SKILLBLOCK_ID + "`='" + skillblock.getDbId() + "'";
        try {
            int result = statement.executeUpdate(sql);
            if (result > 0) {
                System.out.println("The skill was deleted successfully!");
                return true;
            } else {
                System.out.println("Skill delete failed!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Skill[] getAllSkillFromSkillBlock(String skillblockname) {
        SkillBlock skillBlock = getSkillBlock(skillblockname);
        String sql = "SELECT * FROM " + SKILL_TABLE_NAME + " WHERE " + SKILL_SKILLBLOCK_ID + "=" + skillBlock.getDbId();
        ArrayList<Skill> skills = new ArrayList<Skill>();
        try(ResultSet rs = statement.executeQuery(sql)) {
            while(rs.next()){
                skills.add(new Skill(rs.getInt(1), rs.getInt(4), rs.getString(2), rs.getString(3), rs.getBoolean(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        Skill[] output = skills.toArray(new Skill[skills.size()]);
        return output;
    }

    public Skill getSkillByName(int sbId, String name) {
        String sql = "SELECT * FROM " + SKILL_TABLE_NAME + " WHERE `" + SKILL_DB_NAME + "`='" + name + "' AND `" + SKILL_SKILLBLOCK_ID + "`='" + sbId + "'";
        try(ResultSet rs = statement.executeQuery(sql);) {
            if(rs.next()){
                return new Skill(rs.getInt(1), rs.getInt(4), rs.getString(2), rs.getString(3), rs.getBoolean(5));
            }
            System.out.println("Requested skill doesn't exist!");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Skill getSkillById(int id) {
        String sql = "SELECT * FROM " + SKILL_TABLE_NAME + " WHERE `id`='" + id + "'";
        try(ResultSet rs = statement.executeQuery(sql);) {
            if(rs.next()){
                return new Skill(rs.getInt(1), rs.getInt(4), rs.getString(2), rs.getString(3), rs.getBoolean(5));
            }
            System.out.println("Requested skill doesn't exist!");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Skill[] getAllSkillOfUser(String username) {
        String sql = "SELECT " + USER_SKILLS_SKILL_ID + " FROM " + USER_SKILLS_TABLE_NAME + " WHERE `" + USER_SKILLS_USER_ID + "`='" + getUserId(username) + "'";
        ArrayList<Skill> userSkills = new ArrayList<Skill>();

        try(Statement st = conn.createStatement(); 
            ResultSet rs = st.executeQuery(sql)) {
            while(rs.next()){
                userSkills.add(getSkillById(rs.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        Skill[] output = userSkills.toArray(new Skill[userSkills.size()]);
        return output;
    }

    public Skill[] getAllSkillOfUserBySkillblock(String username, String skillblockName) {
        Skill[] skillblockSkills = getAllSkillFromSkillBlock(skillblockName);

        String sql = "SELECT " + USER_SKILLS_SKILL_ID + ", " + USER_SKILLS_SKILL_STATUS + " FROM " + USER_SKILLS_TABLE_NAME + " WHERE `" + USER_SKILLS_USER_ID + "`='" + getUserId(username) + "'";
        ArrayList<Skill> userSkills = new ArrayList<Skill>();
        try(Statement st = conn.createStatement(); 
            ResultSet rs = st.executeQuery(sql)) {
            while(rs.next()){
                Skill tmp = getSkillById(rs.getInt(1));
                tmp.setValidate(rs.getInt(2));
                userSkills.add(tmp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        ArrayList<Skill> outputSkills = new ArrayList<Skill>();
        for (Skill skillblockSkill : skillblockSkills) {
            for (Skill userSkill : userSkills) {
                if(skillblockSkill.equals(userSkill)) {
                    outputSkills.add(userSkill);
                    break;
                }
            }
        }

        Skill[] output = outputSkills.toArray(new Skill[outputSkills.size()]);
        return output;
    }

    public boolean checkSkillValidation(String username, Skill skill) {
        String sql = "SELECT " + USER_SKILLS_SKILL_STATUS + " FROM " + USER_SKILLS_TABLE_NAME + " WHERE `" + USER_SKILLS_SKILL_ID + "`='" + skill.getDbId() + "' AND `" + USER_SKILLS_USER_ID + "`='" + getUserId(username) + "'";
        try(Statement st = conn.createStatement(); 
            ResultSet rs = st.executeQuery(sql)) {
            if(rs.next()){
                return rs.getInt(1) == 1 ? true : false;
            } else {
                throw new IllegalArgumentException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public boolean requestSkill(String u, String s, String sb) {
        System.out.println("Trying to request new skill...");
        try {
            int userId = getUserId(u);
            if (userId == -1) {
                System.out.println("Error: User don't exist !");
                return false;
            }

            SkillBlock skillblock = getSkillBlock(sb);
            if (skillblock == null) {
                System.out.println("Error: Skillblock don't exist !");
                return false;
            }

            Skill skill = getSkillByName(skillblock.getDbId(), s);
            if(skill == null) {
                System.out.println("Error: Skill don't exist !");
                return false;
            }

            String check_existing_request = "SELECT * FROM " + USER_SKILLS_TABLE_NAME + " WHERE `" + USER_SKILLS_USER_ID + "`='" + userId + "' AND `" + USER_SKILLS_SKILL_ID + "`='" + skill.getDbId() + "'";
            try(ResultSet rs = statement.executeQuery(check_existing_request)){
                if(rs.next()) {
                    System.out.println("Error: Skill request already exists in user ones !");
                    return false;
                }
            }

            boolean autovalidate = skill.getAutoValidate() ? true : false;

            String sql = "INSERT INTO " + USER_SKILLS_TABLE_NAME + "(`" + USER_SKILLS_USER_ID + "`, `"
                    + USER_SKILLS_SKILL_ID + "`, `" + USER_SKILLS_SKILL_STATUS + "`) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, skill.getDbId());
            statement.setBoolean(3, autovalidate);

            int result = statement.executeUpdate();
            if (result > 0) {
                System.out.println("A new skillblock was linked to current user successfully!");
                return true;
            } else {
                System.out.println("Skillblock subscribe insert failed!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateSkill(String connectedUserName, String user, String skillblockName, String skillName) {
        System.out.println("Trying to validate a skill...");
        try {
            Role connectedUser = getUserRole(connectedUserName);
            if (connectedUser == null) {
                System.out.println("Error: User not exist or doesn't have role !");
                return false;
            } else if (!connectedUser.getCanValidate()) {
                System.out.println("Error: User role not allowed to validate skills !");
                return false;
            }

            int userId = getUserId(user);
            if (userId == -1) {
                System.out.println("Error: User don't exist !");
                return false;
            }

            SkillBlock skillblock = getSkillBlock(skillblockName);
            if (skillblock == null) {
                System.out.println("Error: Skillblock don't exist !");
                return false;
            }

            Skill skill = getSkillByName(skillblock.getDbId(), skillName);
            if(skill == null) {
                System.out.println("Error: Skill don't exist !");
                return false;
            }

            String check_existing_request = "SELECT * FROM " + USER_SKILLS_TABLE_NAME + " WHERE `" + USER_SKILLS_USER_ID + "`='" + userId + "' AND `" + USER_SKILLS_SKILL_ID + "`='" + skill.getDbId() + "'";
            try(ResultSet rs = statement.executeQuery(check_existing_request)){
                if(!rs.next()) {
                    System.out.println("Error: Skill request don't exist in user ones !");
                    return false;
                }
            }

            String sql = "UPDATE " + USER_SKILLS_TABLE_NAME + " SET " + 
            USER_SKILLS_SKILL_STATUS + "=? WHERE `" + USER_SKILLS_USER_ID + "`='" + userId + 
            "' AND `" + USER_SKILLS_SKILL_ID + "`='" + skill.getDbId() + "'";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setBoolean(1, true);

            int result = statement.executeUpdate();
            if (result > 0) {
                System.out.println("Skill validation successed!");
                return true;
            } else {
                System.out.println("Skill validation failed!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}