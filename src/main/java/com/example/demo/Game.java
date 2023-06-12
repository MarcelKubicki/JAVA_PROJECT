package com.example.demo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Game {
    private boolean whitesTurn;
    private boolean isCapture;
    private boolean whiteWins;
    private boolean blackWins;

    public boolean isCapture() {
        return isCapture;
    }

    public void setCapture(boolean capturing) {
        isCapture = capturing;
    }

    public void setWhitesTurn(boolean whitesTurn){
        this.whitesTurn = whitesTurn;
    }
    public boolean getWhitesTurn(){return whitesTurn; }

    public boolean isWhiteWins() {
        return whiteWins;
    }

    public void setWhiteWins(boolean whiteWins) {
        this.whiteWins = whiteWins;
    }

    public boolean isBlackWins() {
        return blackWins;
    }

    public void setBlackWins(boolean blackWins) {
        this.blackWins = blackWins;
    }
    public void init(){
        whitesTurn = true;
        isCapture = false;
        whiteWins = false;
        blackWins = false;
    }
}

