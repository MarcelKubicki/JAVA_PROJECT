package com.example.demo;

import javafx.scene.shape.Rectangle;

import java.util.Arrays;

public class Board {
    public Field[][] blackFields = new Field[8][8];

    public void Init() {
        for (int i = 0; i < 8; i++) { //kolumny
            for (int j = 0; j < 8; j++) { //wiersze
                if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0)) {
                    if (j < 3) {
                        blackFields[i][j] = new Field(i, j, Field.State.blackMan);
                    } else if (j > 4) {
                        blackFields[i][j] = new Field(i, j, Field.State.whiteMan);
                    } else{
                        blackFields[i][j] = new Field(i, j, Field.State.empty);
                    }
                } else {
                    blackFields[i][j] = new Field(i, j, Field.State.whiteField);
                }
            }
        }
    }

    public Field searchedField(int x, int y){ return blackFields[x][y]; }
    public Rectangle searchedRectangle(int x, int y){
        System.out.println("aaa");return blackFields[x][y].getRectangle(); }
    public Field.State getStateField(int x, int y){
        return blackFields[x][y].state;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Board otherBoard = (Board) obj;
        return Arrays.deepEquals(blackFields, otherBoard.blackFields);
    }

}
