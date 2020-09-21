import data.User;
import database.DatabaseHandler;

public class App {
    public static void main(String[] args) throws Exception {
        DatabaseHandler db = new DatabaseHandler();
        User testUser = new User("login", "password", "test1", "test2");
        db.insertUser(testUser);
    }
}
