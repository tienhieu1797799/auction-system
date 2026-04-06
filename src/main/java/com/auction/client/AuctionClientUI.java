import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AuctionClientUI extends Application {

    private ListView<String> auctionListView = new ListView<>();
    private TextArea itemDetailsArea = new TextArea();
    private TextField bidField = new TextField();
    private Button bidButton = new Button("Place Bid");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Auction Client");

        // Login Screen
        VBox loginLayout = new VBox(10);
        Scene loginScene = new Scene(loginLayout, 300, 200);
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        loginLayout.getChildren().addAll(new Label("Username:"), usernameField, new Label("Password:"), passwordField, loginButton);

        // Auction Display
        VBox mainLayout = new VBox(10);
        auctionListView.setPrefHeight(400);
        itemDetailsArea.setPrefHeight(200);

        bidButton.setOnAction(e -> placeBid());

        mainLayout.getChildren().addAll(new Label("Auctions:"), auctionListView, new Label("Item Details:"), itemDetailsArea, new Label("Your Bid:"), bidField, bidButton);

        // Switch to main scene on successful login
        loginButton.setOnAction(e -> {
            // Perform login logic here
            primaryStage.setScene(new Scene(mainLayout, 600, 600));
        });

        primaryStage.setScene(loginScene);
        primaryStage.show();

        // Simulated auction data
        loadAuctionData();
    }

    private void loadAuctionData() {
        // Load auction data into auctionListView
        auctionListView.getItems().addAll("Item 1", "Item 2", "Item 3");
        // Set details when an item is selected
        auctionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            itemDetailsArea.setText("Details of " + newSelection);
        });
    }

    private void placeBid() {
        String bid = bidField.getText();
        // Logic to place bid goes here
        System.out.println("Bid placed: " + bid);
        bidField.clear();
    }
}