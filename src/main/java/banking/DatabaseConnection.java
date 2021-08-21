package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class DatabaseConnection {

    private boolean loggedIn;
    private final String database;
    private static DatabaseConnection instance;

    private DatabaseConnection(String database) {
        this.loggedIn = false;
        this.database = database;
        createTable();
    }

    public static DatabaseConnection getInstance(String database) {
        if (DatabaseConnection.instance == null) {
            DatabaseConnection.instance = new DatabaseConnection(database);
        }
        return DatabaseConnection.instance;

    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    private void createTable() {
        String tableQuery = "CREATE TABLE IF NOT EXISTS card (" +
                "id INTEGER NOT NULL," +
                "number TEXT," +
                "pin TEXT," +
                "balance INTEGER DEFAULT 0)";
        try (Connection con = connect()) {
            try (Statement statement = con.createStatement()) {

                statement.execute(tableQuery);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    Connection connect() {
        String url = "jdbc:sqlite:" + this.database;
        Connection con = null;
        try {
            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl(url);
            con = dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }

}
