package com.example.demo;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class Main extends Application {
    Game game = new Game();
   // public Field selectedField = null;
    private VBox row;
   // private Rectangle[] markedFields = {null ,null ,null};
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
                HBox gameBoard = generateGameBoard(); // Wywołanie metody generującej planszę
                Scene game = new Scene(gameBoard, 720, 540);
                gameBoard.setStyle("-fx-background-color:rgb(0, 0, 0);");
                gameBoard.setPadding(new Insets(30, 30, 0, 30));
                primaryStage.setScene(game);
                primaryStage.centerOnScreen();


            }

        });

        Scene menu = new Scene(stackPane, 350, 350);

        primaryStage.setScene(menu);
        primaryStage.setTitle("Warcaby");
        primaryStage.show();
    }

    private HBox generateGameBoard() {
        HBox gameBoard = new HBox(); // Kontener na planszę
        Board board = new Board();
        board.Init();

        // Generowanie planszy
        for (int i = 0; i < 8; i++) {
            row = new VBox();
            for (int j = 0; j < 8; j++) {
                    Field gameField = board.blackFields[i][j];
                    switch (gameField.state) {
                        case whiteField:
                            Rectangle white = new javafx.scene.shape.Rectangle(60, 60, Color.WHITE);
                            row.getChildren().add(white);
                            break;
                        case whiteMan:
                            Rectangle whiteManField = new Rectangle(58, 58, Color.GRAY);
                            whiteManField.setStrokeWidth(2);
                            whiteManField.setStroke(Color.GRAY);
                            board.blackFields[i][j].setRectangle(whiteManField);
                            Circle whiteManPiece = new Circle(25, Color.WHITE);
                            whiteManPiece.setStroke(Color.BLACK);
                            whiteManPiece.setStrokeWidth(2);
                            StackPane stackPane = new StackPane(whiteManField, whiteManPiece);
                            stackPane.setUserData(new int[]{i,j}); // Przechowywanie indeksów x i y
                            addClickListener(board, whiteManPiece, whiteManField);
                            row.getChildren().add(stackPane);
                            break;
                        case blackMan:
                            Rectangle blackManField = new Rectangle(58, 58, Color.GRAY);
                            blackManField.setStrokeWidth(2);
                            blackManField.setStroke(Color.GRAY);
                            board.blackFields[i][j].setRectangle(blackManField);
                            Circle blackManPiece = new Circle(25, Color.BLACK);
                            blackManPiece.setStroke(Color.WHITE);
                            blackManPiece.setStrokeWidth(2);
                            StackPane stackpane = new StackPane(blackManField, blackManPiece);
                            stackpane.setUserData(new int[]{i,j}); // Przechowywanie indeksów x i y
                            addClickListener(board, blackManPiece, blackManField);
                            row.getChildren().add(stackpane);
                            break;
                        case empty:
                            javafx.scene.shape.Rectangle emptyField = new javafx.scene.shape.Rectangle(58, 58, Color.GRAY);
                            emptyField.setStrokeWidth(2);
                            emptyField.setStroke(Color.GRAY);;
                            board.blackFields[i][j].setRectangle(emptyField);
                            row.getChildren().add(emptyField);
                            break;
                        case whiteKing:
                            break;
                        case blackKing:
                            break;
                        default:
                            break;
                    }
                }
                gameBoard.getChildren().add(row);
            }
        return gameBoard;
    }


    //Zaznaczanie klikniętego pionka
    private void addClickListener(Board board, Circle circle, Rectangle rectangle) {
        circle.setOnMouseClicked(event -> {
            int[] indexes = (int[]) rectangle.getParent().getUserData();
            System.out.println(indexes[0] + " " + indexes[1]);

            if (game.getSelectedField() != null) {
                game.clearSelected();
                if(game.getSelectedField().equals(board.searchedField(indexes[0], indexes[1]))){
                    game.setSelectedField(null);
                    game.clearMarked();
                    return;
               }
            }

            game.setSelectedField(board.searchedField(indexes[0], indexes[1]));
            game.setMarkedField(1, rectangle);

            if (game.getSelectedField().state == Field.State.blackMan || game.getSelectedField().state == Field.State.whiteMan) {
                rectangle.setStroke(Color.YELLOW);
                System.out.println("Położenie pionka: [" + game.getSelectedField().getX() + ", " + game.getSelectedField().getY() + "]");

           }

            possibleMovies(board, indexes[0], indexes[1]);
        });
    }

    //Zaznaczanie pól ruchu dla whiteMan
    private void possibleMovies(Board board, int x, int y){
        game.clearMarked();

        if(x > 0) {
            if (board.getStateField(x - 1 , y - 1) == Field.State.empty)
                game.setMarkedField(0, board.searchedField(x -1, y-1).getRectangle());
        }

        if(x < 7) {
            if (board.getStateField(x + 1, y - 1) == Field.State.empty)
                game.setMarkedField(2, board.searchedField(x + 1, y-1).getRectangle());
        }



       if(game.getMarkedField(2) != null) {
           game.getMarkedField(2).setStroke(Color.GREEN);
       }
        if(game.getMarkedField(0) != null) {
            game.getMarkedField(0).setStroke(Color.GREEN);
        }
        moveClick(board, x, y);
    }

    private void moveClick(Board board, int x, int y){
        //Field move = board.searchedField(x, y);
        //board.searchedField(x, y).getRectangle().setStroke(Color.BLUE);

        game.getMarkedField(0).setOnMouseClicked(event ->{
            game.clearMarked();
            board.searchedField(x-1, y).getRectangle().setStroke(Color.BLUE);
            return;
           // move.setStateField(Field.State.whiteMan);
        });

        game.getMarkedField(2).setOnMouseClicked(event ->{
            game.clearMarked();
            board.searchedField(x+1, y).getRectangle().setStroke(Color.PURPLE);
            return;
        });
    }
}