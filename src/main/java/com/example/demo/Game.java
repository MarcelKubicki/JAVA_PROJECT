package com.example.demo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Game {
    private boolean whitesTurn = true;
    private boolean isCapture = false;
    private boolean victory = false;

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
}

