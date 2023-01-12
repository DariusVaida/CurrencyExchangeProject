package darius.proiectpebune;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/Cash_exchange";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";

    public static boolean isUserValid(String username, String password) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isUserAdmin(String username, String password){
        //TO-DO: ADMIN AUTH
        return Objects.equals(username, "admin") && Objects.equals(password, "admin");
    }


    public static void addUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addTransaction(String username,String from_currency, double value_from, String to_currency, double value_to, double amount) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO transactions (username,from_currency, value_from,to_currency, value_to, amount) VALUES (?, ?, ?, ?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, from_currency);
            statement.setDouble(3, value_from);
            statement.setString(4, to_currency);
            statement.setDouble(5, value_to);
            statement.setDouble(6, amount);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(String username) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "DELETE FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllUsers() {
        List<String> userList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT username FROM users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                userList.add(resultSet.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM transactions");

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String fromCurrency = rs.getString("from_currency");
                double valueFrom = rs.getDouble("value_from");
                String toCurrency = rs.getString("to_currency");
                double valueTo = rs.getDouble("value_to");
                double amount = rs.getDouble("amount");
                transactions.add(new Transaction(username,fromCurrency,valueFrom, toCurrency,valueTo, amount));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return transactions;
    }
}
