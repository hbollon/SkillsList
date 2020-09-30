import data.User;
import database.DatabaseHandler;

public class App {
    public static void main(String[] args) throws Exception {
        DatabaseHandler db = new DatabaseHandler();
        User testUser = new User("alu", "password", "nom1", "prenom2");
        db.insertUser(testUser);
        testUser = new User("login", "password", "nom1", "prenom1");
        db.insertUser(testUser);
        db.updateUser(testUser);
    }
}
