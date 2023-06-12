package com.example.demo;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class Main extends Application {
    Stage primaryStage = new Stage();
    Game game = new Game();
    Board board = new Board();
    Capture capture = new Capture();
    HBox gameBoard = new HBox();
    boolean isSelected;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        menu();

    }

    private void menu(){
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20));

        // Add button "Play"
        Button playButton = new Button("Play");
        playButton.setPrefSize(150, 50);
        vbox.getChildren().add(playButton);

        // Add button "Online"
        Button onlineButton = new Button("Online");
        onlineButton.setPrefSize(125, 50);

        // Add checbox
        CheckBox onlineCheckBox = new CheckBox("");
        onlineCheckBox.setDisable(true);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(onlineButton, onlineCheckBox);

        vbox.getChildren().add(buttonBox);

        // Add button "Quit"
        Button quitButton = new Button("Quit");
        quitButton.setPrefSize(150, 50);
        vbox.getChildren().add(quitButton);

        // Tworzenie kontenera StackPane i dodawanie VBox oraz obrazka w tle
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(vbox);

        playButton.setOnAction(event -> {
            Scene scene = gameBoard.getScene();
            if(scene != null) {
                gameBoard.getChildren().clear();
                AnchorPane emptyRoot = new AnchorPane();
                scene.setRoot(emptyRoot);
            }
            gameWindow();
        });

        onlineButton.setOnAction(event -> {
            isSelected = !isSelected;
            onlineCheckBox.setSelected(isSelected);
        });

        quitButton.setOnAction(event -> primaryStage.close());

        Scene menu = new Scene(stackPane, 350, 350);

        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("background.png", 350, 350, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );
        Background background = new Background(backgroundImage);
        vbox.setBackground(background);

        primaryStage.setScene(menu);
        primaryStage.setTitle("Menu");
        primaryStage.show();
    }

    private void gameWindow(){
        gameBoard.setStyle("-fx-background-color:rgb(0, 0, 0);");
        gameBoard.setPadding(new Insets(30, 30, 0, 30));

        game.init();
        capture.init();
        board.init();
        generateBoardLayout(); // generating gameboard
        drawPieces(); // drawing pieces on gameboard

        Scene wGame = new Scene(gameBoard, 720, 540);
        primaryStage.setScene(wGame);
        primaryStage.centerOnScreen();
    }

    /* LOGIKA GRY */

    //Draw empty board
    private void generateBoardLayout() {
        for (int i = 0; i < 8; i++) {
            VBox row = new VBox();
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    Rectangle whiteField = new Rectangle(60, 60, Color.WHITE);
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
            for (int j = 0; j < 8; j++) {
                Field gameField = board.foundField(i, j);
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

            if (board.getClickedField().getState() == Field.State.empty  || board.foundField(board.getClickedX(), board.getClickedY()).getState() == Field.State.whiteField) {
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

        if(!game.getWhitesTurn()) {
            if (board.foundField(board.getClickedX(), board.getClickedY()).state == Field.State.blackPiece && !game.getWhitesTurn()) {
                PieceClicked(Field.State.blackPiece, Field.State.blackKing, Field.State.whitePiece, Field.State.whiteKing, 1);
            } else if (board.foundField(board.getClickedX(), board.getClickedY()).state == Field.State.blackKing) {
                KingClicked(Field.State.blackPiece, Field.State.blackKing, Field.State.whitePiece, Field.State.whiteKing);
            }
        }
    }

    private void PieceClicked(Field.State alliedPiece, Field.State alliedKing, Field.State enemyPiece, Field.State enemyKing, int move){
        board.clearMarked();
        board.setSelectedField(board.foundField(board.getClickedX(), board.getClickedY()));
        board.getSelectedField().getRectangle().setStroke(Color.YELLOW);
        System.out.println("Położenie pionka: [" + board.getSelectedField().getX() + ", " + board.getSelectedField().getY() + "]");//

        if(!capture.isCaptureMove()) {
            game.setCapture(true);
            if(capture.isPieceWithCapture(board.foundField(board.getClickedX(), board.getClickedY()))) {
                possibleCapturePieceMoves();
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

        if(!capture.isCaptureMove()) {
            game.setCapture(true);
            if(capture.isPieceWithCapture(board.foundField(board.getClickedX(), board.getClickedY()))) {
                possibleCaptureKingMoves(enemyPiece, enemyKing);
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
        boolean leftTop = true, rightTop = true, leftBot = true, rightBot = true;

        for (int i = 1; i < 8; i++) {
            // top left
            if ((x - i) >= 0 && (y - i) >= 0 && leftTop) {
                Field field = board.foundField(x - i, y - i);
                if (field.getState() == Field.State.empty) {
                    board.setMarkedField(field);
                } else leftTop = false;

            }

            // top right
            if ((x + i) < 8 && (y - i) >= 0 && rightTop) {
                Field field = board.foundField(x + i, y - i);
                if (field.getState() == Field.State.empty) {
                    board.setMarkedField(field);
                } else rightTop = false;
            }

            // bot left
            if ((x - i) >= 0 && (y + i) < 8 && leftBot) {
                Field field = board.foundField(x - i, y + i);
                if (field.getState() == Field.State.empty) {
                    board.setMarkedField(field);
                } else leftBot = false;
            }

            // bot right
            if ((x + i) < 8 && (y + i) < 8 && rightBot) {
                Field field = board.foundField(x + i, y + i);
                if (field.getState() == Field.State.empty) {
                    board.setMarkedField(field);
                } else rightBot = false;
            }
        }
        board.drawMarkedFields();
    }

    private void possibleCapturePieceMoves() {
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

    private void possibleCaptureKingMoves(Field.State piece, Field.State king){
        int j = 0;
        int x = board.getSelectedField().getX();
        int y = board.getSelectedField().getY();

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
            } else if(board.getSelectedField().getState() == Field.State.whiteKing){
                drawMoveKing(board.getSelectedField().getX(), board.getSelectedField().getY(), x, y, Color.WHITE, Color.BLACK);
            }
            if(game.isCapture()){
                if(board.getClickedField().getState() == Field.State.whitePiece) {
                    removePieceCapturing();
                } else if (board.getClickedField().getState() == Field.State.whiteKing) {
                    removePieceCapturingKing();
                }

                capture.clearCapture();
                capture.setCapturingPiece(board, Field.State.whitePiece, Field.State.blackPiece, Field.State.blackKing);
                capture.setCapturingKing(board, Field.State.whitePiece, Field.State.whiteKing, Field.State.blackPiece, Field.State.blackKing);

                if(capture.isPieceWithCapture(board.getClickedField())){
                    board.clearSelected();
                    board.clearStoke(board.getClickedField().getRectangle());
                    return;
                }else{
                    game.setCapture(false);
                }


            }
            game.setWhitesTurn(false);

            capture.clearCapture();

            capture.setCapturingKing(board, Field.State.blackPiece, Field.State.blackKing, Field.State.whitePiece, Field.State.whiteKing);

            if(capture.isCaptureMove())
                capture.setCapturingPiece(board, Field.State.blackPiece, Field.State.whitePiece, Field.State.whiteKing);


            //check possible white wins
            if(capture.isCaptureMove() && !isMove(Field.State.blackPiece, Field.State.blackKing, 1))
                game.setWhiteWins(true);


            if(game.isWhiteWins())
                afterWin("BIAŁE WYGRYWAJĄ!!!");
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

            if(game.isCapture()) {
                if (board.getClickedField().getState() == Field.State.blackPiece) {
                    removePieceCapturing();
                } else if (board.getClickedField().getState() == Field.State.blackKing) {
                    removePieceCapturingKing();
                }

                capture.clearCapture();
                capture.setCapturingPiece(board, Field.State.blackPiece, Field.State.whitePiece, Field.State.whiteKing);
                capture.setCapturingKing(board, Field.State.blackPiece, Field.State.blackKing, Field.State.whitePiece, Field.State.whiteKing);

                if (capture.isPieceWithCapture(board.getClickedField())) {
                    board.clearSelected();
                    board.clearStoke(board.getClickedField().getRectangle());
                    return;
                } else {
                    game.setCapture(false);
                }
            }
            game.setWhitesTurn(true);

            capture.clearCapture();

            capture.setCapturingKing(board, Field.State.whitePiece, Field.State.whiteKing, Field.State.blackPiece, Field.State.blackKing);

            if(capture.isCaptureMove())
                capture.setCapturingPiece(board, Field.State.whitePiece, Field.State.blackPiece, Field.State.blackKing);

            if(capture.isCaptureMove() && !isMove(Field.State.whitePiece, Field.State.whiteKing, -1))
                game.setBlackWins(true);

            if(game.isBlackWins())
                afterWin("CZARNE WYGRYWAJĄ!!!");
        }

        board.clearMarked();
        board.clearSelected();
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

    private boolean isMove(Field.State piece, Field.State king, int move) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.foundField(i, j).getState() == piece || board.foundField(i, j).getState() == king) {
                    if (isValidMove(i, j, i - 1, j + move)) {
                        return true;
                    }
                    if (isValidMove(i, j, i + 1, j + move)) {
                        return true;
                    }
                    if (board.foundField(i, j).getState() == king) {
                        if (isValidMove(i, j, i - 1, j - move)) {
                            return true;
                        }
                        if (isValidMove(i, j, i + 1, j - move)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isValidMove(int startX, int startY, int endX, int endY) {
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false; // Sprawdzenie, czy docelowe pole mieści się w planszy
        }

        Field startField = board.foundField(startX, startY);
        Field endField = board.foundField(endX, endY);

        if (endField.getState() == Field.State.empty) {
            int deltaX = Math.abs(startX - endX);
            int deltaY = Math.abs(startY - endY);

            if (deltaX == 1 && deltaY == 1) {
                return true; // Dozwolony ruch o jedno pole na ukos
            }
        }
        return false;
    }


    private void afterWin(String string){
        Stage infoStage = new Stage();
        infoStage.initOwner(primaryStage);
        infoStage.initModality(Modality.APPLICATION_MODAL);
        infoStage.initStyle(StageStyle.UNDECORATED);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Label infoLabel = new Label(string);
        infoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        vbox.getChildren().add(infoLabel);

        Button menuButton = new Button("Return to menu");
        vbox.getChildren().add(menuButton);

        menuButton.setOnAction(event -> {
            infoStage.close();
            primaryStage.close();
            menu();
        });

        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("win.png", 400, 250, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );
        Background background = new Background(backgroundImage);
        vbox.setBackground(background);

        Scene infoScene = new Scene(vbox, 400, 250);
        infoStage.setScene(infoScene);
        infoStage.show();
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