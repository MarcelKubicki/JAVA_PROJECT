package com.example.demo;

public class FieldBlack {
    private enum State {
        empty, blackMan, whiteMan, blackKing, whiteKing;
    }

    public int id;
    public int x = Main.FirstX;
    public int y = Main.FirstY;
    public State state = State.empty;
    public boolean isSeleceted = false;
    public int[] movies;

}
