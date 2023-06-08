package com.example.demo;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class Main extends Application {
    Game game = new Game();
    Board board = new Board();
    Capture capture = new Capture();
    HBox gameBoard = new HBox();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane stackPane = new StackPane();
        Button start = new Button("START");
        stackPane.getChildren().add(start);

        start.setOnAction(event -> {
            gameBoard.setStyle("-fx-background-color:rgb(0, 0, 0);");
            gameBoard.setPadding(new Insets(30, 30, 0, 30));

            board.init();
            generateBoardLayout(); // Generowanie planszy
            drawPieces(); // Rysowanie pionków na planszy

            Scene game = new Scene(gameBoard, 720, 540);
            primaryStage.setScene(game);
            primaryStage.centerOnScreen();

        });

        Scene menu = new Scene(stackPane, 350, 350);

        primaryStage.setScene(menu);
        primaryStage.setTitle("Warcaby");
        primaryStage.show();
    }

    //Draw empty board
    private void generateBoardLayout() {
        for (int i = 0; i < 8; i++) {
            VBox row = new VBox();
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    Rectangle whiteField = new Rectangle(60, 60, Color.WHITE);
                    /*StackPane stackPane = new StackPane();
                    stackPane.getChildren().add(whiteField);
                    stackPane.setUserData(new int[]{i, j});*/
                    row.getChildren().add(whiteField);
                } else {
                    Rectangle blackField = new Rectangle(57, 57, Color.GRAY);
                    blackField.setStrokeWidth(3);
                    blackField.setStroke(Color.GRAY);
                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().add(0, blackField);
                    stackPane.setUserData(new int[]{i, j});
                    board.foundField(i,j).setRectangle(blackField);
                    addClickListener(stackPane);
                    row.getChildren().add(stackPane);
                }
            }
            gameBoard.getChildren().add(row);
        }
    }

    //Draw Pieces
    private void drawPieces() {
        for (int i = 0; i < 8; i++) {
            //VBox row = (VBox) gameBoard.getChildren().get(i);
            for (int j = 0; j < 8; j++) {
                Field gameField = board.foundField(i, j);
                //StackPane stackPane = (StackPane) row.getChildren().get(j);
                switch (gameField.state) {
                    case whitePiece -> drawPiece(i, j, Color.WHITE, Color.BLACK);
                    case blackPiece -> drawPiece(i, j, Color.BLACK, Color.WHITE);
                    case whiteKing -> drawKing(i, j, Color.WHITE, Color.BLACK);
                    case blackKing -> drawKing(i, j, Color.BLACK, Color.WHITE);
                    default -> {}
                }
            }
        }
    }


    //mark clicked field
    private void addClickListener(StackPane stackPane) {
        stackPane.setOnMouseClicked(event -> {

            int[] indexes = (int[]) stackPane.getUserData();
            board.setClickedX(indexes[0]);
            board.setClickedY(indexes[1]);

            System.out.println(board.getClickedX() + " " + board.getClickedY());//
            System.out.println(board.foundField(board.getClickedX(), board.getClickedY()).getState());//

            //do poprawki
            if (board.foundField(board.getClickedX(), board.getClickedY()).getState() == Field.State.empty || board.foundField(board.getClickedX(), board.getClickedY()).getState() == Field.State.whiteField) {
            ;
            }
            else {
                selectField();
            }

            if(board.getSelectedField() != null) {
                if(board.isMarkedFields()){
                    moveClick(board.getClickedX(), board.getClickedY());
                }
            }
        });
    }

    private void selectField(){
        if (board.getSelectedField() != null) {
            board.clearStoke(board.getSelectedField().getRectangle());
            board.clearMarked();
        }

        if(game.getWhitesTurn()) {
            if (board.foundField(board.getClickedX(), board.getClickedY()).state == Field.State.whitePiece) {
                PieceClicked(Field.State.whitePiece , Field.State.whiteKing, Field.State.blackPiece, Field.State.blackKing, -1);
            }
            else if(board.foundField(board.getClickedX(), board.getClickedY()).state == Field.State.whiteKing){
                KingClicked(Field.State.whitePiece , Field.State.whiteKing, Field.State.blackPiece, Field.State.blackKing);
            }
        }

        if (board.foundField(board.getClickedX(), board.getClickedY()).state == Field.State.blackPiece && !game.getWhitesTurn()) {
            PieceClicked(Field.State.blackPiece, Field.State.blackKing, Field.State.whitePiece , Field.State.whiteKing, 1);
        }
        else if(board.foundField(board.getClickedX(), board.getClickedY()).state == Field.State.blackKing){
            KingClicked(Field.State.whitePiece , Field.State.whiteKing, Field.State.blackPiece, Field.State.blackKing);
        }
    }

    private void PieceClicked(Field.State alliedPiece, Field.State alliedKing, Field.State enemyPiece, Field.State enemyKing, int move){
        board.clearMarked();
        board.setSelectedField(board.foundField(board.getClickedX(), board.getClickedY()));
        board.getSelectedField().getRectangle().setStroke(Color.YELLOW);
        System.out.println("Położenie pionka: [" + board.getSelectedField().getX() + ", " + board.getSelectedField().getY() + "]");//

        capture.setCapturingPiece(board, alliedPiece, enemyPiece, enemyKing);

        if(!capture.isCaptureMove()) {
            game.setCapture(true);
            if(capture.isPieceWithCapture(board.foundField(board.getClickedX(), board.getClickedY()))) {
                possibleCapturePieceMoves(capture);
               // capture.setCapturingPiece(board, alliedPiece, enemyPiece, enemyKing);
            }
            else{
                board.clearMarked();
            }
        }
        else {
            possiblePieceMoves(board.getClickedX(), board.getClickedY() + move);
        }
    }
    private void KingClicked(Field.State alliedPiece, Field.State alliedKing, Field.State enemyPiece, Field.State enemyKing){
        board.clearMarked();
        board.setSelectedField(board.foundField(board.getClickedX(), board.getClickedY()));
        board.getSelectedField().getRectangle().setStroke(Color.YELLOW);

        System.out.println("Położenie damki: [" + board.getSelectedField().getX() + ", " + board.getSelectedField().getY() + "]");//

        capture.setCapturingKing(board, alliedKing, enemyPiece, enemyKing);

        if(!capture.isCaptureMove()) {
            game.setCapture(true);
            if(capture.isPieceWithCapture(board.foundField(board.getClickedX(), board.getClickedY()))) {
                possibleCaptureKingMoves(capture, enemyPiece, enemyKing);
            }
            else{
                board.clearMarked();
            }
        }
        else {
            possibleKingMoves(board.getClickedX(), board.getClickedY());
        }
    }

    //Zaznaczanie pól ruchu dla pionka
    private void possiblePieceMoves(int x, int y) {
        if(y >= 0 && y <= 7) {
            if (x > 0) {
                if (board.foundField(x - 1, y).getState() == Field.State.empty) {
                    board.setMarkedField(board.foundField(x - 1, y));
                }
            }

            if (x < 7) {
                if (board.foundField(x + 1, y).getState() == Field.State.empty) {
                    board.setMarkedField(board.foundField(x + 1, y));
                }
            }
            board.drawMarkedFields();
        }
    }
    private void possibleKingMoves(int x, int y) {
        for (int i = 0; i < 8; i++) {
            // top left
            if ((x - i) >= 0 && (y - i) >= 0) {
                Field field = board.foundField(x - i, y - i);
                if (field.getState() == Field.State.empty) {
                    board.setMarkedField(field);
                }
            }

            // top right
            if ((x + i) < 8 && (y - i) >= 0) {
                Field field = board.foundField(x + i, y - i);
                if (field.getState() == Field.State.empty) {
                    board.setMarkedField(field);
                }
            }

            // bot left
            if ((x - i) >= 0 && (y + i) < 8) {
                Field field = board.foundField(x - i, y + i);
                if (field.getState() == Field.State.empty) {
                    board.setMarkedField(field);
                }
            }

            // bot right
            if ((x + i) < 8 && (y + i) < 8) {
                Field field = board.foundField(x + i, y + i);
                if (field.getState() == Field.State.empty) {
                    board.setMarkedField(field);
                }
            }
        }
        board.drawMarkedFields();
    }

    private void possibleCapturePieceMoves(Capture capture) {
        int x, y;
        x = board.getSelectedField().getX();
        y = board.getSelectedField().getY();

        if (x - 2 >= 0 && y - 2 >= 0) {
            if (capture.isPossibleCaptureMoves((board.foundField(x - 2, y - 2)))) {
                board.addMarkedFields(board.foundField(x - 2, y - 2));
            }
        }

        if(x - 2 >= 0 && y + 2 < 8) {
            if (capture.isPossibleCaptureMoves((board.foundField(x - 2, y + 2)))) {
                board.addMarkedFields(board.foundField(x - 2, y + 2));
            }
        }

        if(x + 2 < 8 && y - 2 >= 0) {
            if (capture.isPossibleCaptureMoves((board.foundField(x + 2, y - 2)))) {
                board.addMarkedFields(board.foundField(x + 2, y - 2));
            }
        }

        if(x + 2 < 8 && y+ 2 < 8) {
            if (capture.isPossibleCaptureMoves((board.foundField(x + 2, y + 2)))) {
                board.addMarkedFields(board.foundField(x + 2, y + 2));
            }
        }

        board.drawMarkedFields();
    }

    private void possibleCaptureKingMoves(Capture capture, Field.State piece, Field.State king){
        int x, y;
        int j = 0;
        Field capturingPiece1, capturingPiece2, capturingPiece3, capturingPiece4;
        x = board.getSelectedField().getX();
        y = board.getSelectedField().getY();

        for(int i = 0; i < 8; i++){

            //top left
            if((x - (i + 2)) >= 0 && (y - (i + 2)) >= 0)
                if(board.foundField(x - (i + 1), y - (i + 1)).getState() == piece || board.foundField(x - (i + 1), y - (i + 1)).getState() == king)
                    if (capture.isPossibleCaptureMoves(board.foundField(x - (i + 2), y - (i + 2)))) {
                        while (board.foundField(x - (i + 2 + j), y - (i + 2 + j)).getState() == Field.State.empty) {
                            board.addMarkedFields(board.foundField(x - (i + 2 + j), y - (i + 2 + j)));
                            j++;

                            if( x - (i + 2 + j) < 0 || y - (i + 2 + j) < 0)
                                break;
                        }
                        j = 0;
                    }


            //top right
            if ((x + (i + 2)) < 8 && (y - (i + 2)) >= 0)
                if (board.foundField(x + (i + 1), y - (i + 1)).getState() == piece || board.foundField(x + (i + 1), y - (i + 1)).getState() == king)
                    if (capture.isPossibleCaptureMoves((board.foundField(x + (i + 2), y - (i + 2))))) {
                        while (board.foundField(x + (i + 2 + j), y - (i + 2 + j)).getState() == Field.State.empty) {
                            board.addMarkedFields(board.foundField(x + (i + 2 + j), y - (i + 2 + j)));
                            j++;

                            if( x + (i + 2 + j) >= 8 || y - (i + 2 + j) < 0)
                                break;
                        }
                        j = 0;
                    }

            //bot left
            if ((x - (i + 2)) >= 0 && (y + (i + 2)) < 8)
                if (board.foundField(x - (i + 1), y + (i + 1)).getState() == piece || board.foundField(x - (i + 1), y + (i + 1)).getState() == king)
                   if (capture.isPossibleCaptureMoves((board.foundField(x - (i + 2), y + (i + 2))))) {
                       while (board.foundField(x - (i + 2 + j), y + (i + 2 + j)).getState() == Field.State.empty) {
                           board.addMarkedFields(board.foundField(x - (i + 2 + j), y + (i + 2 + j)));
                           j++;

                           if( x - (i + 2 + j) < 0 || y + (i + 2 + j) >= 8)
                               break;
                       }
                       j = 0;
                   }


            //bot right
            if ((x + (i + 2)) < 8 && (y + (i + 2)) < 8)
                if (board.foundField(x + (i + 1), y + (i + 1)).getState() == piece || board.foundField(x + (i + 1), y + (i + 1)).getState() == king)
                    if (capture.isPossibleCaptureMoves((board.foundField(x + (i + 2), y + (i + 2))))) {
                        while (board.foundField(x + (i + 2 + j), y + (i + 2 + j)).getState() == Field.State.empty) {
                            board.addMarkedFields(board.foundField(x + (i + 2 + j), y + (i + 2 + j)));
                            j++;

                            if( x + (i + 2 + j) >= 8 || y + (i + 2 + j) >= 8)
                                break;
                        }
                        j = 0;
                    }
        }
        board.drawMarkedFields();
    }
    private void removePieceCapturing(){
        if(board.getSelectedField().getX() - board.getClickedX() > 0 && board.getSelectedField().getY() - board.getClickedY() > 0)
            removePiece(board.getSelectedField().getX() - 1 , board.getSelectedField().getY() - 1);

        if(board.getSelectedField().getX() - board.getClickedX() < 0 && board.getSelectedField().getY() - board.getClickedY() > 0)
            removePiece(board.getSelectedField().getX() + 1, board.getSelectedField().getY() - 1);

        if(board.getSelectedField().getX() - board.getClickedX() > 0 && board.getSelectedField().getY() - board.getClickedY() < 0)
            removePiece(board.getSelectedField().getX() - 1, board.getSelectedField().getY() + 1);

        if(board.getSelectedField().getX() - board.getClickedX() < 0 && board.getSelectedField().getY() - board.getClickedY() < 0)
            removePiece(board.getSelectedField().getX() + 1, board.getSelectedField().getY() + 1);
    }
    private void removePieceCapturingKing(){
        int i = 0;

        /* top left */
        if(board.getSelectedField().getX() - board.getClickedX() > 0 && board.getSelectedField().getY() - board.getClickedY() > 0) {
            i = 1;
            while (i < board.getSelectedField().getY() - board.getClickedY()) {
                removePiece(board.getSelectedField().getX() - i, board.getSelectedField().getY() - i);
                i++;
            }
        }

        /* top right */
        if(board.getSelectedField().getX() - board.getClickedX() < 0 && board.getSelectedField().getY() - board.getClickedY() > 0) {
            i = 1;
            while (i < board.getSelectedField().getY() - board.getClickedY()) {
                removePiece(board.getSelectedField().getX() + i, board.getSelectedField().getY() - i);
                i++;
            }
        }

        /* bot left */
        if(board.getSelectedField().getX() - board.getClickedX() > 0 && board.getSelectedField().getY() - board.getClickedY() < 0) {
            i = 1;
            while (i < board.getClickedY() - board.getSelectedField().getY()) {
                removePiece(board.getSelectedField().getX() - i, board.getSelectedField().getY() + i);
                i++;
            }
        }

        /* bot right */
        if(board.getSelectedField().getX() - board.getClickedX() < 0 && board.getSelectedField().getY() - board.getClickedY() < 0) {
            i = 1;
            while (i < board.getClickedY() - board.getSelectedField().getY()) {
                removePiece(board.getSelectedField().getX() + i, board.getSelectedField().getY() + i);
                i++;
            }
        }
    }

    private void moveClick(int x, int y) {
        //white's movie
        if (game.getWhitesTurn()) {
            if(board.getSelectedField().getState() == Field.State.whitePiece) {
                if(board.getClickedY() == 0)
                    pieceIntoKing(Field.State.whiteKing, Color.WHITE, Color.BLACK);
                else
                    drawMovePiece(board.getSelectedField().getX(), board.getSelectedField().getY(), x, y, Color.WHITE, Color.BLACK);
            }
            else if(board.getSelectedField().getState() == Field.State.whiteKing){
                drawMoveKing(board.getSelectedField().getX(), board.getSelectedField().getY(), x, y, Color.WHITE, Color.BLACK);
            }
            if(game.isCapture()){
                if(board.getClickedField().getState() == Field.State.whitePiece) {
                    removePieceCapturing();
                }
                else if (board.getClickedField().getState() == Field.State.whiteKing) {
                    removePieceCapturingKing();
                }

                game.setCapture(false);
            }
            game.setWhitesTurn(false);
           // capture.setCapturingPieces();
        }

        //black's movie
        else if(!game.getWhitesTurn()) {
            if(board.getSelectedField().getState() == Field.State.blackPiece) {
                if(board.getClickedY() == 7)
                    pieceIntoKing(Field.State.blackKing, Color.BLACK, Color.WHITE);
                else
                    drawMovePiece(board.getSelectedField().getX(), board.getSelectedField().getY(), x, y, Color.BLACK, Color.WHITE);

            } else if (board.getSelectedField().getState() == Field.State.blackKing) {
                drawMoveKing(board.getSelectedField().getX(), board.getSelectedField().getY(), x, y, Color.BLACK, Color.WHITE);
            }

            if(game.isCapture()){
                if(board.getClickedField().getState() == Field.State.blackPiece) {
                    removePieceCapturing();
                }
                else if (board.getClickedField().getState() == Field.State.blackKing) {
                    removePieceCapturingKing();
                }

                game.setCapture(false);
            }
            game.setWhitesTurn(true);
        }
        board.clearMarked();
        board.clearSelected();


        //sprawdzania czy istenieje kolejne bicie
        //board.getClickedField()
    }


    private void drawMovePiece(int fromX, int fromY, int toX, int toY, Color color1, Color color2) {
        board.exchangeStates(board.getSelectedField(), board.getClickedField());
        removePiece(fromX, fromY);
        drawPiece(toX, toY, color1, color2);
    }
    private void drawMoveKing(int fromX, int fromY, int toX, int toY, Color color1, Color color2){
        board.exchangeStates(board.getSelectedField(), board.getClickedField());
        removePiece(fromX, fromY);
        drawKing(toX, toY, color1, color2);
    }
    private void pieceIntoKing(Field.State king, Color color1, Color color2){
        removePiece(board.getSelectedField().getX(), board.getSelectedField().getY());
        drawKing(board.getClickedX(), board.getClickedY(), color1, color2);
        board.getClickedField().setStateField(king);
    }
    private void removePiece(int x, int y){
        VBox row = (VBox) gameBoard.getChildren().get(x);
        StackPane stackPane = (StackPane) row.getChildren().get(y);

        if(board.foundField(x, y).getState() != Field.State.empty){
            board.foundField(x, y).setStateField(Field.State.empty);
        }

        if (stackPane.getChildren().size() > 2 && stackPane.getChildren().get(2) instanceof Circle)
            stackPane.getChildren().remove(2);

        if (stackPane.getChildren().size() > 1 && stackPane.getChildren().get(1) instanceof Circle) {
            stackPane.getChildren().remove(1);

            System.out.println("Usunięto");
        }
    }

    private void drawPiece(int x, int y, Color color1, Color color2){
        VBox row = (VBox) gameBoard.getChildren().get(x);
        StackPane stackPane = (StackPane) row.getChildren().get(y);
        Circle piece = new Circle(25, color1);
        piece.setStroke(color2);
        piece.setStrokeWidth(2);
        stackPane.getChildren().add(1, piece);
    }

    private void drawKing(int x, int y, Color color1, Color color2) {
        drawPiece(x, y, color1, color2);
        VBox row = (VBox) gameBoard.getChildren().get(x);
        StackPane stackPane = (StackPane) row.getChildren().get(y);
        Circle king = new Circle(12, color2);
        stackPane.getChildren().add(2, king);
    }

    private Rectangle getRectangle1(int x, int y) {
        VBox row = (VBox) gameBoard.getChildren().get(x);
        StackPane stackPane = (StackPane) row.getChildren().get(y);
        return (Rectangle) stackPane.getChildren().get(0);
    }

    private Rectangle getRectangle1(Field field) {
        VBox row = (VBox) gameBoard.getChildren().get(field.getX());
        StackPane stackPane = (StackPane) row.getChildren().get(field.getY());
        return (Rectangle) stackPane.getChildren().get(0);
    }

}