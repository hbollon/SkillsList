package com.bitsplease.skillslist_server.database;

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

    private final String USER_TABLE_NAME = "user";
    private final String USER_DB_USERNAME = "username";
    private final String USER_DB_PASSWORD = "password";
    private final String USER_DB_PASSWORD_SALT = "salt";
    private final String USER_DB_FIRSTNAME = "first_name";
    private final String USER_DB_LASTNAME = "last_name";
    private final String USER_DB_SQL = "CREATE TABLE IF NOT EXISTS user " + "(id INT NOT NULL AUTO_INCREMENT, "
    + " username VARCHAR(255), " + " password VARCHAR(255), " + "salt VARCHAR(255)," + " first_name VARCHAR(255), "
    + " last_name VARCHAR(255), " + " PRIMARY KEY ( id ))";

    private final String SKILLBLOCK_TABLE_NAME = "skillblock";
    private final String SKILLBLOCK_DB_NAME = "name";
    private final String SKILLBLOCK_DB_DESC = "description";
    private final String SKILLBLOCK_DB_SQL = "CREATE TABLE IF NOT EXISTS skillblock " + "(id INT NOT NULL AUTO_INCREMENT, "
    + " name VARCHAR(255), " + " description VARCHAR(255), " + " PRIMARY KEY ( id ))";

    private final String SKILL_TABLE_NAME = "skill";
    private final String SKILL_DB_NAME = "name";
    private final String SKILL_DB_DESC = "description";
    private final String SKILL_SKILLBLOCK_ID = "skillblock_id";
    private final String SKILL_DB_SQL = 
        "CREATE TABLE IF NOT EXISTS " + SKILL_TABLE_NAME + " ( id INT NOT NULL AUTO_INCREMENT, " + 
        "name VARCHAR(255), " + 
        "description VARCHAR(255), " + 
        "skillblock_id INT, " + 
        "PRIMARY KEY ( id ), FOREIGN KEY(" + SKILL_SKILLBLOCK_ID + ") REFERENCES " + SKILLBLOCK_TABLE_NAME + "(id))";

    private final String USER_SKILLBLOCKS_TABLE_NAME = "user_skillblock";
    private final String USER_SKILLBLOCKS_USER_ID = "user_id";
    private final String USER_SKILLBLOCKS_SKILLBLOCK_ID = "skillblock_id";
    private final String USER_SKILLBLOCKS_DB_SQL = 
        "CREATE TABLE IF NOT EXISTS " + USER_SKILLBLOCKS_TABLE_NAME + " ( id INT NOT NULL AUTO_INCREMENT, " + 
        "user_id INT, " + 
        "skillblock_id INT, " + 
        "PRIMARY KEY ( id ), " + 
        "FOREIGN KEY(" + USER_SKILLBLOCKS_USER_ID + ") REFERENCES " + USER_TABLE_NAME + "(id), " + 
        "FOREIGN KEY(" + USER_SKILLBLOCKS_SKILLBLOCK_ID + ") REFERENCES " + SKILLBLOCK_TABLE_NAME + "(id))";

    private final String USER_SKILLS_TABLE_NAME = "user_skill";
    private final String USER_SKILLS_USER_ID = "user_id";
    private final String USER_SKILLS_SKILL_ID = "skill_id";
    private final String USER_SKILLS_SKILL_STATUS = "status";
    private final String USER_SKILLS_DB_SQL = 
        "CREATE TABLE IF NOT EXISTS " + USER_SKILLS_TABLE_NAME + " ( id INT NOT NULL AUTO_INCREMENT, " + 
        "user_id INT, " + 
        "skill_id INT, " + 
        "status INT, " + 
        "PRIMARY KEY ( id ), " + 
        "FOREIGN KEY(" + USER_SKILLS_USER_ID + ") REFERENCES " + USER_TABLE_NAME + "(id), " + 
        "FOREIGN KEY(" + USER_SKILLS_SKILL_ID + ") REFERENCES " + SKILL_TABLE_NAME + "(id))";

    private final String[] TABLES = {USER_TABLE_NAME, SKILLBLOCK_TABLE_NAME, SKILL_TABLE_NAME, USER_SKILLBLOCKS_TABLE_NAME, USER_SKILLS_TABLE_NAME};
    private final String[] TABLES_SQL = {USER_DB_SQL, SKILLBLOCK_DB_SQL, SKILL_DB_SQL, USER_SKILLBLOCKS_DB_SQL, USER_SKILLS_DB_SQL};

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
    private void clearAllTables(){
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

    public void resetAll() {
        clearAllTables();
    }

    /**
     * Users Interactions
     */

    public boolean insertUser(User u) {
        System.out.println("Trying to register new user...");
        System.out.println("Username : " + u.getUsername());
        System.out.println("Password : " + u.getPassword());
        System.out.println("First Name : " + u.getFirstName());
        System.out.println("Last Name : " + u.getLastName());
        try {
            String check_existing_request = "SELECT * FROM user WHERE `" + USER_DB_USERNAME + "`='" + u.getUsername() + "'";

            ResultSet rs = statement.executeQuery(check_existing_request);
            if(rs.next()) {
                System.out.println("Error: User already exists !");
                return false;
            }
            String sql = "INSERT INTO " + USER_TABLE_NAME + "(`" + USER_DB_USERNAME + "`, `" + USER_DB_PASSWORD + "`, `"
            + USER_DB_PASSWORD_SALT + "`, `" + USER_DB_FIRSTNAME + "`, `" + USER_DB_LASTNAME + "`) VALUES (?, ?, ?, ?, ?)";

            String salt = HashUtils.getSalt(30);

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, u.getUsername());
            statement.setString(2, HashUtils.generateSecurePassword(u.getPassword(), salt));
            statement.setString(3, salt);
            statement.setString(4, u.getFirstName());
            statement.setString(5, u.getLastName());

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
            String sql = "UPDATE " + USER_TABLE_NAME + " SET " + USER_DB_USERNAME + "=?, "
            + USER_DB_FIRSTNAME + "=?, " + USER_DB_LASTNAME + "=? WHERE " + USER_DB_USERNAME + "=?";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, u.getUsername());
            statement.setString(2, u.getFirstName());
            statement.setString(3, u.getLastName());
            statement.setString(4, u.getUsername());

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

    public boolean deleteUser(String username){
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
        try {
            ResultSet rs = statement.executeQuery(sql);
            if(rs.next()){
                return rs.getInt(1);
            }
            System.out.println("User not find!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public User connectUser(String login, String password) {
        String sql = "SELECT * FROM user WHERE `" + USER_DB_USERNAME + "`='" + login + "'";
        try {
            ResultSet rs = statement.executeQuery(sql);
            if(rs.next()){
                if(HashUtils.verifyUserPassword(password, rs.getString(3), rs.getString(4))){
                    System.out.println("User succesfully logged in!");
                    return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(5), rs.getString(6));
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
            if(userId == -1) {
                System.out.println("Error: User don't exist !");
                return false;
            }

            SkillBlock skillblock = getSkillBlock(s);
            if(skillblock == null) {
                System.out.println("Error: Skillblock don't exist !");
                return false;
            }

            String check_existing_request = "SELECT * FROM " + USER_SKILLBLOCKS_TABLE_NAME + " WHERE `" + USER_SKILLBLOCKS_USER_ID + "`='" + userId + "' AND `" + USER_SKILLBLOCKS_SKILLBLOCK_ID + "`='" + skillblock.getDbId() + "'";
            ResultSet rs = statement.executeQuery(check_existing_request);
            if(rs.next()) {
                System.out.println("Error: User already subscribed to this skillblock !");
                return false;
            }

            String sql = "INSERT INTO " + USER_SKILLBLOCKS_TABLE_NAME + "(`" + USER_SKILLBLOCKS_USER_ID + "`, `" + USER_SKILLBLOCKS_SKILLBLOCK_ID + "`) VALUES (?, ?)";
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
        try {
            String check_existing_request = "SELECT * FROM skillblock WHERE `" + SKILLBLOCK_DB_NAME + "`='" + s.getBlockName() + "'";

            ResultSet rs = statement.executeQuery(check_existing_request);
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
        try {
            ResultSet rs = statement.executeQuery(sql);
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
        try {
            ResultSet rs = statement.executeQuery(sql);
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

            String check_existing_request = "SELECT * FROM skill WHERE `" + SKILL_DB_NAME + "`='" + skill.getSkillName() + "'";
            ResultSet rs = statement.executeQuery(check_existing_request);
            if(rs.next()) {
                System.out.println("Error: Skill already exists in this SkillBlock !");
                return false;
            }

            String sql = "INSERT INTO " + SKILL_TABLE_NAME + "(`" + SKILLBLOCK_DB_NAME + "`, `" + SKILLBLOCK_DB_DESC + "`, `" + SKILL_SKILLBLOCK_ID + "`) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, skill.getSkillName());
            statement.setString(2, skill.getSkillDesc());
            statement.setInt(3, associatedSkillBlock.getDbId());

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

    public boolean updateSkill(Skill skill) {
        try {
            String sql = "UPDATE " + SKILL_TABLE_NAME + " SET " + 
            SKILL_DB_NAME + "=?, " + 
            SKILL_DB_DESC + "=? WHERE `" + SKILL_DB_NAME + "`='" + skill.getSkillName() + "'";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, skill.getSkillName());
            statement.setString(2, skill.getSkillDesc());

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

    public boolean deleteSkill(String skillname){
        String sql = "DELETE FROM " + SKILL_TABLE_NAME + " WHERE `" + SKILL_DB_NAME + "`='" + skillname + "'";
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
        try {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                skills.add(new Skill(rs.getInt(1), rs.getInt(4), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        Skill[] output = skills.toArray(new Skill[skills.size()]);
        return output;
    }

}