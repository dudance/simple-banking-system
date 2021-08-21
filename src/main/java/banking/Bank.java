package banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Bank {

    private static String currentUsersNumber;
    private static int idCounter;
    private static int currentUsersId;

    static void closeAccount(DatabaseConnection database) {

        try (Connection con = database.connect()) {
            String deleteId = "DELETE FROM card where id = ?";
            PreparedStatement deleteFromDatabase = con.prepareStatement(deleteId);
            deleteFromDatabase.setInt(1, currentUsersId);
            deleteFromDatabase.executeUpdate();
            database.setLoggedIn(false);
            currentUsersId = -1;
            currentUsersNumber = "";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static void createAccount(DatabaseConnection database) {
        System.out.println("Your card has been created");

        String generatedNumber = "";
        boolean isCorrect = false;


        String selectCardNumber = "SELECT * FROM card WHERE number = ?";

        try (Connection con = database.connect()) {

            PreparedStatement isInDatabase = con.prepareStatement(selectCardNumber);

            while (!isCorrect) {
                isCorrect = true;
                generatedNumber = UtilityFunctions.generateCardNumber();
                isInDatabase.setString(1, generatedNumber);
                ResultSet resultSet = isInDatabase.executeQuery();
                if (resultSet.next())
                    isCorrect = false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String generatedPin =
                UtilityFunctions.generatePin();
        String insertCardNumber = "INSERT INTO card (id, number, pin) VALUES (?, ?, ?)";

        try (Connection con = database.connect();
             PreparedStatement preparedStatement = con.prepareStatement(insertCardNumber)) {
            idCounter++;
            preparedStatement.setInt(1, idCounter);
            preparedStatement.setString(2, generatedNumber);
            preparedStatement.setString(3, generatedPin);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Your card number:");
        System.out.println(generatedNumber);
        System.out.println("Your card PIN:");
        System.out.println(generatedPin);
    }

    static boolean logIn(DatabaseConnection database) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number: ");
        String accountNumber = scanner.next();
        System.out.println("Enter your PIN: ");
        String accountPin = scanner.next();
        String selectCard = "SELECT * FROM card WHERE number = ? AND pin = ?";
        try (Connection con = database.connect()) {

            PreparedStatement isInDatabase = con.prepareStatement(selectCard);
            isInDatabase.setString(1, accountNumber);
            isInDatabase.setString(2, accountPin);
            ResultSet resultSet = isInDatabase.executeQuery();
            if (resultSet.next()) {
                currentUsersId = resultSet.getInt("id");
                currentUsersNumber = resultSet.getString("number");
                System.out.println("You have successfully logged in!");
                return true;
            } else
                System.out.println("Wrong card number or PIN!");
            return false;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    static int balance(DatabaseConnection database) {

        try (Connection con = database.connect()) {
            String balanceQuery = "SELECT balance from card WHERE id = ?";
            PreparedStatement getAccountBalance = con.prepareStatement(balanceQuery);
            getAccountBalance.setInt(1, currentUsersId);
            ResultSet resultSet = getAccountBalance.executeQuery();
            return resultSet.getInt("balance");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    static boolean logOut() {
        currentUsersId = -1;
        currentUsersNumber = "";
        return false;
    }

    static void addIncome(DatabaseConnection database) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter income:");
        int income = scanner.nextInt();
        try (Connection con = database.connect()) {
            String incomeQuery = "UPDATE card SET balance = balance + ? WHERE id = ?";
            PreparedStatement updateIncome = con.prepareStatement(incomeQuery);
            updateIncome.setInt(1, income);
            updateIncome.setInt(2, currentUsersId);
            updateIncome.executeUpdate();
            System.out.println("Income was added!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    static void doTransfer(DatabaseConnection database) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter card number:");
        String transferReceiver = scanner.next();
        if (currentUsersNumber.equals(transferReceiver)) {
            System.out.println("You can't transfer money to the same account!");
        } else if (!UtilityFunctions.isLuhnAlgCorrect(transferReceiver)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        } else {
            try (Connection con = database.connect()) {
                String selectReceiver = "SELECT * FROM card WHERE number = ?";
                PreparedStatement isInDatabase = con.prepareStatement(selectReceiver);
                isInDatabase.setString(1, transferReceiver);
                ResultSet resultSet = isInDatabase.executeQuery();
                if (resultSet.next()) {
                    System.out.println("Enter how much money you want to transfer:");
                    int transferValue = scanner.nextInt();
                    if (balance(database) < transferValue) {
                        System.out.println("Not enough money!");
                    } else {
                        String withdrawQuery = "UPDATE card SET balance = balance - ? WHERE id = ?";
                        String depositQuery = "UPDATE card SET balance = balance + ? WHERE number = ?";
                        PreparedStatement withdrawCash = con.prepareStatement(withdrawQuery);
                        withdrawCash.setInt(1, transferValue);
                        withdrawCash.setInt(2, currentUsersId);
                        withdrawCash.executeUpdate();

                        PreparedStatement depositCash = con.prepareStatement(depositQuery);
                        depositCash.setInt(1, transferValue);
                        depositCash.setString(2, transferReceiver);
                        depositCash.executeUpdate();
                    }
                } else
                    System.out.println("Such a card does not exist.");

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static void menu(DatabaseConnection databaseConnection) {
        Scanner scanner = new Scanner(System.in);
        int chosenOption;

        while (true) {
            if (!databaseConnection.isLoggedIn()) {
                System.out.println("1. Create an account");
                System.out.println("2. Log into account");
                System.out.println("0. Exit");
                chosenOption = scanner.nextInt();
                switch (chosenOption) {
                    case 1 -> Bank.createAccount(databaseConnection);
                    case 2 -> databaseConnection.setLoggedIn(Bank.logIn(databaseConnection));
                    case 0 -> System.exit(0);
                }
            } else {
                System.out.println("1. Balance");
                System.out.println("2. Add income");
                System.out.println("3. Do transfer");
                System.out.println("4. Close account");
                System.out.println("5. Log out");
                System.out.println("0. Exit");
                chosenOption = scanner.nextInt();
                switch (chosenOption) {
                    case 1 -> System.out.println("Balance: " + Bank.balance(databaseConnection));
                    case 2 -> Bank.addIncome(databaseConnection);
                    case 3 -> Bank.doTransfer(databaseConnection);
                    case 4 -> Bank.closeAccount(databaseConnection);
                    case 5 -> databaseConnection.setLoggedIn(Bank.logOut());
                    case 0 -> System.exit(0);
                }
            }
        }
    }

}
