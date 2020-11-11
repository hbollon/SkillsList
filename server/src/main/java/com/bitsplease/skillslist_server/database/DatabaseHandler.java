package com.bitsplease.skillslist_server.database;

import com.bitsplease.skillslist_server.data.User;
import com.bitsplease.skillslist_server.utils.HashUtils;

import java.sql.*;

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

    private final String[] TABLES = {USER_TABLE_NAME};

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
        String sql = "CREATE TABLE IF NOT EXISTS user " + "(id INT NOT NULL AUTO_INCREMENT, "
                + " username VARCHAR(255), " + " password VARCHAR(255), " + "salt VARCHAR(255)," + " first_name VARCHAR(255), "
                + " last_name VARCHAR(255), " + " PRIMARY KEY ( id ))";

        statement.executeUpdate(sql);
    }

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

    /**
     * Clear all tables of the db
     */
    private void clearAllTables(){
        for (String table : TABLES) {
            try {
                statement.executeUpdate("TRUNCATE " + table);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetAll() {
        clearAllTables();
    }

}