package com.example.demo;

import javafx.scene.shape.Rectangle;

public class Field {
    private int x;
    private int y;
    private Rectangle rectangle;

    public enum State {
        empty, whiteField, whitePiece, blackPiece, whiteKing, blackKing;
    }
    public Field.State state;

    public Field(int x, int y, State state) {
        this.x = x;
        this.y = y;
        this.state = state;
    }

    public void setStateField(State state){
        this.state = state;
    }

    public State getState(){
        return state;
    }
    public void setNewPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setRectangle(Rectangle rectangle) { this.rectangle = rectangle; }
    public Rectangle getRectangle() { return rectangle; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Field otherField = (Field) obj;
        return x == otherField.x && y == otherField.y && state == otherField.state;
    }
}
