package darius.proiectpebune;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static darius.proiectpebune.DatabaseConnection.*;

public class CurrencyExchange extends Application {
    private Stage primaryStage;
    private Scene loginScene;
    private Scene currencyScene;
    private Scene adminScene;
    private Scene addUserScene;
    private Scene deleteUserScene;
    private Scene viewUsersScene;
    private Scene viewTransactionsScene;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        primaryStage.setTitle("Currency Exchange");

        GridPane loginGrid = new GridPane();
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);
        loginGrid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Currency Exchange Login");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        loginGrid.add(sceneTitle, 0, 0, 2, 1);
        Label userName = new Label("User Name:");
        loginGrid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        loginGrid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        loginGrid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        loginGrid.add(pwBox, 1, 2);
        Button createBtn = new Button("Create Account");
        HBox hbCreateBtn = new HBox(10);
        hbCreateBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbCreateBtn.getChildren().add(createBtn);
        loginGrid.add(hbCreateBtn, 0, 4);

        createBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String user = userTextField.getText();
                String pass = pwBox.getText();
                if (user.isEmpty() || pass.isEmpty()) {
                    //show error message
                    Label errorLabel = new Label("Empty fields are not allowed.");
                    loginGrid.add(errorLabel, 1, 5);
                } else {
                    addUser(user, pass);
                }
            }
        });

        final String[] username = {"abc"};
        Button btn = new Button("Sign in");
        HBox hbBtn1 = new HBox(10);
        hbBtn1.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn1.getChildren().add(btn);
        loginGrid.add(hbBtn1, 1, 4);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String user = userTextField.getText();
                String pass = pwBox.getText();
                username[0] = user;
                if (DatabaseConnection.isUserValid(user, pass)) {

                    primaryStage.setScene(currencyScene);

                } else if (DatabaseConnection.isUserAdmin(user, pass)) {
                    primaryStage.setScene(adminScene);
                } else {
                    // show error message
                    Label errorLabel = new Label("Incorrect user name or password.");
                    loginGrid.add(errorLabel, 1, 5);
                }
            }
        });

        loginScene = new Scene(loginGrid, 400, 375);
        primaryStage.setScene(loginScene);
        GridPane currencyGrid = new GridPane();
        currencyGrid.setAlignment(Pos.CENTER);
        currencyGrid.setHgap(10);
        currencyGrid.setVgap(10);
        currencyGrid.setPadding(new Insets(25, 25, 25, 25));


        Label amountLabel = new Label("Amount:");
        currencyGrid.add(amountLabel, 0, 1);

        TextField amountField = new TextField();
        currencyGrid.add(amountField, 1, 1);

        Label fromLabel = new Label("From:");
        currencyGrid.add(fromLabel, 0, 2);

        ComboBox<String> fromComboBox = new ComboBox<>();
        fromComboBox.getItems().addAll("USD", "EUR", "GBP", "RON");
        fromComboBox.setValue("USD");
        currencyGrid.add(fromComboBox, 1, 2);

        Label toLabel = new Label("To:");
        currencyGrid.add(toLabel, 0, 3);

        ComboBox<String> toComboBox = new ComboBox<>();
        toComboBox.getItems().addAll("USD", "EUR", "GBP", "RON");
        toComboBox.setValue("EUR");
        currencyGrid.add(toComboBox, 1, 3);

        Button convertButton = new Button("Convert");
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().add(convertButton);
        currencyGrid.add(hbBtn2, 1, 4);

        Button logoutButton = new Button("Logout");
        HBox hbBtn3 = new HBox(10);
        hbBtn3.setAlignment(Pos.BOTTOM_LEFT);
        hbBtn3.getChildren().add(logoutButton);
        currencyGrid.add(hbBtn3, 0, 4);
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
               navigateToLoginScene();
                userTextField.clear();
                pwBox.clear();

            }
        });

        Label resultLabel = new Label();
        currencyGrid.add(resultLabel, 1, 5);

        convertButton.setOnAction(event -> {
            double amount = Double.parseDouble(amountField.getText());
            String fromCurrency = fromComboBox.getValue();
            String toCurrency = toComboBox.getValue();

            double exchangeRate1 = getExchangeRate(fromCurrency);
            double exchangeRate2 = getExchangeRate(toCurrency);
            double exchangeRate = exchangeRate1 / exchangeRate2;
            double convertedAmount = exchangeRate * amount;
            resultLabel.setText(String.format("%.2f %s = %.2f %s", amount, fromCurrency, convertedAmount, toCurrency));
            DatabaseConnection.addTransaction(username[0], fromCurrency, exchangeRate1, toCurrency, exchangeRate2, amount);
        });
        currencyScene = new Scene(currencyGrid, 400, 375);
        Text sceneTitle2 = new Text("Currency Exchange");
        sceneTitle2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        currencyGrid.add(sceneTitle2, 0, 0, 2, 1);

        //ADMIN PAGE

        GridPane adminGrid = new GridPane();
        adminGrid.setAlignment(Pos.CENTER);
        adminGrid.setHgap(10);
        adminGrid.setVgap(10);
        adminGrid.setPadding(new Insets(25, 25, 25, 25));

        Button addUserButton = new Button("Add User");
        adminGrid.add(addUserButton, 0, 0);

        Button deleteUserButton = new Button("Delete User");
        adminGrid.add(deleteUserButton, 1, 0);

        Button viewUsersButton = new Button("View Users");
        adminGrid.add(viewUsersButton, 0, 1);

        Button viewTransactionsButton = new Button("View Transactions");
        adminGrid.add(viewTransactionsButton, 1, 1);

        Button backButton = new Button("Back");
        adminGrid.add(backButton, 2, 2);


        addUserButton.setOnAction(UserEvent -> {


            initializeAddUserScene();
            System.out.println("Add User");
            navigateToAddUserScene();
            });


        deleteUserButton.setOnAction(event -> {

                initializeDeleteUserScene();
                System.out.println("Delete User");
                navigateToDeleteUserScene();

            });

        viewUsersButton.setOnAction(viewUsersEvent -> {

            initializeViewUsersScene();
            navigateToViewUsersScene();

            });

        viewTransactionsButton.setOnAction(viewTransactionEvent -> {
                initializeViewTransactionsScene();
                navigateToViewTransactionsScene();
            });

        backButton.setOnAction(backEvent -> {
                navigateToLoginScene();
                userTextField.clear();
                pwBox.clear();
            });


        adminScene = new Scene(adminGrid, 400, 375);
        primaryStage.show();
    }


    private double getExchangeRate(String fromCurrency) {
        double exchangeRate = 0.0;
        try {

            Class.forName("org.postgresql.Driver");

            // Connect to the database
            String url = "jdbc:postgresql://localhost:5432/Cash_exchange";
            java.sql.Connection conn = DriverManager.getConnection(url, "postgres", "password");

            // Create a prepared statement
            String sql = "SELECT exchange_rate FROM currencies WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Set the value for the placeholder in the prepared statement
            stmt.setString(1, fromCurrency);

            // Execute the prepared statement and retrieve the result set
            ResultSet rs = stmt.executeQuery();

            // Extract the exchange rate from the result set
            if (rs.next()) {
                exchangeRate = rs.getDouble("exchange_rate");
            }

            // Close the connection
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exchangeRate;
    }


    public void navigateToAdminScene(){
        primaryStage.setScene(adminScene);
    }

    public void initializeAddUserScene() {
        // Create a GridPane to hold the UI components of the Add User page
        GridPane addUserGrid = new GridPane();
        // Add UI components to the addUserGrid
        TextField usernameField = new TextField();
        TextField passwordField = new TextField();

        // Create a button to submit the new user
        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");
        addUserGrid.add(backButton, 1,5);
        // Add the text fields and button to the adminGrid
        addUserGrid.add(new Label("Username:"), 0, 2);
        addUserGrid.add(usernameField, 1, 2);
        addUserGrid.add(new Label("Password:"), 0, 3);
        addUserGrid.add(passwordField, 1, 3);
        addUserGrid.add(submitButton, 1, 4);

        // Set the action for the submit button
        submitButton.setOnAction(submitEvent -> {
            String username1 = usernameField.getText();
            String password = passwordField.getText();

            // Validate the inputs, then add the user
            if (!username1.isEmpty() && !password.isEmpty()) {
                //  call addUser method, for now it will just print the input
                System.out.println("Added user: " + username1 + " - " + password);
                addUser(username1, password);
            } else {
                // Show an error message
                System.out.println("Error: Invalid username or password.");
            }
        });

        backButton.setOnAction(backEvent -> {
            primaryStage.setScene(adminScene);
            usernameField.clear();
            passwordField.clear();
        });
        addUserScene = new Scene(addUserGrid,400, 375);

    }

    public void navigateToAddUserScene(){
        primaryStage.setScene(addUserScene);
        primaryStage.show();
    }

    public void initializeDeleteUserScene() {

        GridPane deleteUserGrid = new GridPane();
        List<String> userList = getAllUsers();
        ListView<String> userListView = new ListView<>();
        userListView.getItems().addAll(userList);
        deleteUserGrid.add(userListView, 0, 5);

        Button deleteSelectedButton = new Button("Delete Selected User");
        deleteUserGrid.add(deleteSelectedButton, 0, 6);
        Button backButton = new Button("Back");
        deleteUserGrid.add(backButton, 1,5);

        // Set the action for the deleteSelected button
        deleteSelectedButton.setOnAction(deletionEvent -> {
            String selectedUser = userListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                // call the method to delete the selected user
                deleteUser(selectedUser);
                userListView.getItems().remove(selectedUser);
                System.out.println("Deleted user: " + selectedUser);
            } else {
                // Show an error message
                System.out.println("Error: Please select a user to delete.");
            }
        });
        backButton.setOnAction(backEvent -> {
            primaryStage.setScene(adminScene);
        });
        deleteUserScene = new Scene(deleteUserGrid,400, 375);
        
    }

    public void navigateToDeleteUserScene() {

        primaryStage.setScene(deleteUserScene);
        primaryStage.show();
    }


    public void initializeViewUsersScene() {
        GridPane viewUsersGrid = new GridPane();
        List<String> userList = getAllUsers();
        ListView<String> userListView = new ListView<>();
        userListView.getItems().addAll(userList);
        // Add the ListView to the viewUsersGrid
        viewUsersGrid.add(userListView, 0, 0);
        // Add a back button to viewUsersGrid
        Button backButton = new Button("Back");
        viewUsersGrid.add(backButton, 0, 1);
        backButton.setOnAction(backEvent -> {
            primaryStage.setScene(adminScene);
        });
        viewUsersScene = new Scene(viewUsersGrid,400, 375);
    }

    public void navigateToViewUsersScene(){
        primaryStage.setScene(viewUsersScene);
        primaryStage.show();
    }

    public void initializeViewTransactionsScene(){
        GridPane viewTransactionsGrid = new GridPane();
        List<Transaction> transactionList = DatabaseConnection.getTransactions();
        // Create a ListView to display the list of transactions
        ListView<Transaction> transactionListView = new ListView<>();
        transactionListView.getItems().addAll(transactionList);
        // Add the ListView to the adminGrid
        viewTransactionsGrid.add(transactionListView, 0, 5);
        // Add a back button to adminGrid
        Button backButton = new Button("Back");
        viewTransactionsGrid.add(backButton, 0, 6);
        backButton.setOnAction(backEvent -> {
            primaryStage.setScene(adminScene);
        });
        viewTransactionsScene = new Scene(viewTransactionsGrid,400, 375);

    }

    public void navigateToViewTransactionsScene(){
        primaryStage.setScene(viewTransactionsScene);
        primaryStage.show();
    }

    public void navigateToLoginScene(){
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
}
