package com.example.demo2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Cards extends Application {
    private static final String CARD_IMAGES_FOLDER = "file:/Users/karlinbarnes/Documents/RandomCards/";
    private static final String CARDS_DEALT_FILE = "CardsDealt.txt";

    private static Stage primaryStage;
    private HBox hbox = new HBox(10);
    private ArrayList<ImageView> cardImages = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws IOException {
        Cards.primaryStage = primaryStage;

        // Create controls
        Label label = new Label("Playing Cards!");
        Button dealCards = new Button("Deal Cards.");
        Button quit = new Button("Quit");

        // Set event handlers
        dealCards.setOnAction(new DealCards());
        quit.setOnAction(new Quit());

        // Create a container for buttons and label
        VBox controlsContainer = new VBox(10); // spacing between nodes
        controlsContainer.getChildren().addAll(dealCards, quit, label);
        controlsContainer.setPadding(new Insets(10));

        // Create a BorderPane
        BorderPane root = new BorderPane();

        // Place the HBox in the center of the BorderPane
        root.setCenter(hbox);

        // Place the controlsContainer at the bottom of the BorderPane
        root.setBottom(controlsContainer);

        // Create a scene and set it on the stage
        Scene scene = new Scene(root, 800, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cards!");
        primaryStage.show();
    }

    // Deal cards event handler
    private class DealCards implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            hbox.getChildren().clear(); // Clear previous content
            cardImages.clear(); // Clear previous card images

            // Generate random cards
            List<String> ranks = generateRanks();
            List<String> suits = generateSuits();
            List<String> dealtCards = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                String rank = getRandomElement(ranks);
                String suit = getRandomElement(suits);
                String cardName = rank + "_of_" + suit;
                Image cardImage = new Image(CARD_IMAGES_FOLDER + cardName + ".png");
                ImageView imageView = new ImageView(cardImage);
                cardImages.add(imageView);
                hbox.getChildren().add(imageView);
                dealtCards.add(rank + suit);
            }

            // Save dealt cards to file
            saveCardsDealtToFile(dealtCards);
        }
    }

    // Quit event handler
    private static class Quit implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            primaryStage.close();
        }
    }

    // Method to generate ranks
    private List<String> generateRanks() {
        List<String> ranks = new ArrayList<>();
        for (int i = 2; i <= 14; i++) {
            ranks.add(String.valueOf(i));
        }
        return ranks;
    }

    // Method to generate suits
    private List<String> generateSuits() {
        return List.of("S", "C", "D", "H");
    }

    // Method to get a random element from a list
    private <T> T getRandomElement(List<T> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    // Method to save dealt cards to file
    private void saveCardsDealtToFile(List<String> dealtCards) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CARDS_DEALT_FILE, true))) {
            LocalDate currentDate = LocalDate.now();
            writer.write(currentDate.toString() + "\n");
            for (String card : dealtCards) {
                writer.write(card + "\n");
            }
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
