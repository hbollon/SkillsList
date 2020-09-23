package database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import data.User;

import java.sql.Connection;

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
    private final String USER_DB_FIRSTNAME = "first_name";
    private final String USER_DB_LASTNAME = "last_name";

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
                + " username VARCHAR(255), " + " password VARCHAR(255), " + " first_name VARCHAR(255), "
                + " last_name VARCHAR(255), " + " PRIMARY KEY ( id ))";

        statement.executeUpdate(sql);
    }

    public void insertUser(User u) {
        try {
            System.out.println("INSERT INTO " + USER_TABLE_NAME + "(`" + USER_DB_USERNAME + "`, `" + USER_DB_PASSWORD
                    + "`, `" + USER_DB_FIRSTNAME + "`, `" + USER_DB_LASTNAME + "`)" + " VALUES (`" + u.getUsername()
                    + "`, `" + u.getPassword() + "`, `" + u.getFirstName() + "`, `" + u.getLastName() + "`)");
            statement.executeUpdate(
                    "INSERT INTO " + USER_TABLE_NAME + "(`" + USER_DB_USERNAME + "`, `" + USER_DB_PASSWORD + "`, `"
                            + USER_DB_FIRSTNAME + "`, `" + USER_DB_LASTNAME + "`)" + " VALUES ('" + u.getUsername()
                            + "', '" + u.getPassword() + "', '" + u.getFirstName() + "', '" + u.getLastName() + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}