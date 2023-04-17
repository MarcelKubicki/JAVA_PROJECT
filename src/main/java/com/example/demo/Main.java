package com.example.demo;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;


public class Main extends Application {

    public static int FirstX = 30, FirstY = 30;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane stackPane = new StackPane();
        Button start = new Button("START");
        stackPane.getChildren().add(start);

        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HBox gameBoard = new HBox(); // Kontener na planszę
                Scene game = new Scene(gameBoard, 720, 540);
                gameBoard.setStyle("-fx-background-color:rgb(0, 0, 0);");
                gameBoard.setPadding(new Insets(30, 30, 0, 30));
                primaryStage.setScene(game);


                // Generowanie planszy
                for (int i = 0; i < 8; i++) {
                    VBox row = new VBox();
                    for (int j = 0; j < 8; j++) {
                        Rectangle field = new Rectangle(60, 60, (i + j) % 2 == 0 ? Color.WHITE : Color.GREY);
                        row.getChildren().add(field);
                    }
                    gameBoard.getChildren().add(row);
                }

                primaryStage.centerOnScreen();
            }
        });

        Scene menu = new Scene(stackPane, 350, 350);

        primaryStage.setScene(menu);
        primaryStage.setTitle("Warcaby");
        primaryStage.show();
    }


}

