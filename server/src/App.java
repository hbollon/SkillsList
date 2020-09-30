import data.User;
import database.DatabaseHandler;

public class App {
    public static void main(String[] args) throws Exception {
        DatabaseHandler db = new DatabaseHandler();
        User testUser = db.connectUser("alu", "password");
        System.out.println(testUser);
    }
}
