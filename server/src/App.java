import data.User;
import database.DatabaseHandler;

public class App {
    public static void main(String[] args) throws Exception {
        DatabaseHandler db = new DatabaseHandler();
        User testUser = new User("alu", "password", "nom1", "prenom2");
        db.insertUser(testUser);
    }
}
