package banking;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection database = DatabaseConnection.getInstance(args[1]);
        Bank.menu(database);
    }
}
