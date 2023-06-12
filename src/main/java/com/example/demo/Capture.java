package com.example.demo;

import java.util.ArrayList;

public class Capture {
    private ArrayList<Field> possibleCaptureMoves = new ArrayList<>();
    private ArrayList<Field> piecesWithCapture = new ArrayList<>();
    private ArrayList<Field> capturingPieces = new ArrayList<>();

    public Capture() {

    }

    public void init(){
        if(!possibleCaptureMoves.isEmpty())
            possibleCaptureMoves.clear();
        if(!piecesWithCapture.isEmpty())
            piecesWithCapture.clear();
        if(!capturingPieces.isEmpty())
            capturingPieces.clear();
    }

    public ArrayList<Field> getCapturingPieces() {
        return capturingPieces;
    }

    public void setCapturingPieces(ArrayList<Field> capturingPieces) {
        this.capturingPieces = capturingPieces;
    }
    public void clearCapture(){
        possibleCaptureMoves.clear();
        piecesWithCapture.clear();
    }

    public ArrayList<Field> getPossibleCaptureMoves() {
        return possibleCaptureMoves;
    }

    public ArrayList<Field> getPiecesWithCapture() {
        return piecesWithCapture;
    }

    public boolean isCaptureMove() {
        return possibleCaptureMoves.isEmpty();
    }

    public boolean isPieceWithCapture(Field field) {
        for (int i = 0; i < piecesWithCapture.size(); i++) {
            if (field.equals(piecesWithCapture.get(i))) {
                return true;
            }
        }
        return false;
    }
    public boolean isPossibleCaptureMoves(Field field) {
        for (int i = 0; i < possibleCaptureMoves.size(); i++) {
            if (field.equals(possibleCaptureMoves.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void setCapturingPiece(Board board, Field.State capturing, Field.State piece, Field.State king) {
        boolean pieceWithCapture = false;

        for (int i = 0; i < 8; i++) { //kolumny
            for (int j = 0; j < 8; j++) { //wiersze

                if (board.foundField(i, j).getState() == capturing) {

                    //top left
                    if (i - 2 >= 0 && j - 2 >= 0) {
                        if ((board.foundField(i - 1, j - 1).getState() == piece) || (board.foundField(i - 1, j - 1).getState() == king)) {
                            if (board.foundField(i - 2, j - 2).getState() == Field.State.empty) {
                                possibleCaptureMoves.add(board.foundField(i - 2, j - 2));
                                pieceWithCapture = true;
                            }
                        }
                    }

                    //top right
                    if(i - 2 >= 0 && j + 2 < 8) {
                        if ((board.foundField(i - 1, j + 1).getState() == piece) || (board.foundField(i - 1, j + 1).getState() == king)) {
                            if (board.foundField(i - 2, j + 2).getState() == Field.State.empty) {
                                possibleCaptureMoves.add(board.foundField(i - 2, j + 2));
                                pieceWithCapture = true;
                            }
                        }
                    }

                    //bot left
                    if(i + 2 < 8 && j - 2 >= 0) {
                        if ((board.foundField(i + 1, j - 1).getState() == piece) || (board.foundField(i + 1, j - 1).getState() == king)) {
                            if (board.foundField(i + 2, j - 2).getState() == Field.State.empty) {
                                possibleCaptureMoves.add(board.foundField(i + 2, j - 2));
                                pieceWithCapture = true;
                            }
                        }
                    }

                    //bot right
                    if(i + 2 < 8 && j + 2 < 8) {
                        if ((board.foundField(i + 1, j + 1).getState() == piece) || (board.foundField(i + 1, j + 1).getState() == king)) {
                            if (board.foundField(i + 2, j + 2).getState() == Field.State.empty) {
                                possibleCaptureMoves.add(board.foundField(i + 2, j + 2));
                                pieceWithCapture = true;
                            }
                        }
                    }
                }
                if (pieceWithCapture) {
                    Field field = board.foundField(i, j);
                    piecesWithCapture.add(field);
                    pieceWithCapture = false;
                }
            }
        }
    }

    public void setCapturingKing(Board board, Field.State alliedPiece, Field.State capturingKing, Field.State piece, Field.State king){
        boolean kingWithCapture = false;
        boolean leftTop, rightTop, leftBot, rightBot;

        for (int i = 0; i < 8; i++) { //kolumny
            for (int j = 0; j < 8; j++) { //wiersze
                if (board.foundField(i, j).getState() == capturingKing) {
                    leftTop = true;
                    rightTop = true;
                    leftBot = true;
                    rightBot = true;

                    for(int k = 1; k < 8; k++) {

                        //top left
                        if (i - (k+1) >= 0 && j - (k+1) >= 0 && leftTop) {
                            if ((board.foundField(i - k, j - k).getState() == piece) || (board.foundField(i - k, j - k).getState() == king)) {
                                if (board.foundField(i - (k+1), j - (k+1)).getState() == Field.State.empty) {
                                    capturingPieces.add(board.foundField(i - k, j - k));
                                    possibleCaptureMoves.add(board.foundField(i - (k + 1), j - (k + 1)));
                                    kingWithCapture = true;
                                }
                            } else if(board.foundField(i - k, j - k).getState() == alliedPiece || board.foundField(i - k, j - k).getState() == capturingKing){
                                leftTop = false;
                            }
                        }

                        //top right
                        if (i - (k+1) >= 0 && j + (k+1) < 8 && rightTop) {
                            if ((board.foundField(i - k, j + k).getState() == piece) || (board.foundField(i - k, j + k).getState() == king)) {
                                if (board.foundField(i - (k+1), j + (k+1)).getState() == Field.State.empty) {
                                    capturingPieces.add(board.foundField(i - k, j + k));
                                    possibleCaptureMoves.add(board.foundField(i - (k+1), j + (k+1)));
                                    kingWithCapture = true;
                                }
                            }else if(board.foundField(i - k, j + k).getState() == alliedPiece || board.foundField(i - k, j + k).getState() == capturingKing){
                                rightTop = false;
                            }
                        }

                        //bot left
                        if (i + (k+1) < 8 && j - (k+1) >= 0 && leftBot) {
                            if ((board.foundField(i + k, j - k).getState() == piece) || (board.foundField(i + k, j - k).getState() == king)) {
                                if (board.foundField(i + (k+1), j - (k+1)).getState() == Field.State.empty) {
                                    capturingPieces.add(board.foundField(i + k, j - k));
                                    possibleCaptureMoves.add(board.foundField(i + (k+1), j - (k+1)));
                                    kingWithCapture = true;
                                }
                            } else if(board.foundField(i + k, j - k).getState() == alliedPiece || board.foundField(i + k, j - k).getState() == capturingKing){
                                leftBot = false;
                            }
                        }

                        //bot right
                        if (i + (k+1) < 8 && j + (k+1) < 8 && rightBot) {
                            if ((board.foundField(i + k, j + k).getState() == piece) || (board.foundField(i + k, j + k).getState() == king)) {
                                if (board.foundField(i + (k+1), j + (k+1)).getState() == Field.State.empty) {
                                    capturingPieces.add(board.foundField(i + k, j + k));
                                    possibleCaptureMoves.add(board.foundField(i + (k+1), j + (k+1)));
                                    kingWithCapture = true;
                                }
                            }else if(board.foundField(i + k, j + k).getState() == alliedPiece || board.foundField(i + k, j + k).getState() == capturingKing){
                                rightBot = false;
                            }
                        }
                    }


                }
                if (kingWithCapture) {
                    Field field = board.foundField(i, j);
                    piecesWithCapture.add(field);
                    kingWithCapture = false;
                }
            }
        }
    }
}
