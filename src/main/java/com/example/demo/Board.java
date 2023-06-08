package com.example.demo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private Field[][] blackFields = new Field[8][8];
    private Field selectedField = null;
    private ArrayList<Field> markedFields = new ArrayList<>();
    private int clickedX = -1;
    private int  clickedY = -1;

    public void init() {
        for (int i = 0; i < 8; i++) { //kolumny
            for (int j = 0; j < 8; j++) { //wiersze
                if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0)) {
                    if (j < 3) {
                        blackFields[i][j] = new Field(i, j, Field.State.blackPiece);
                    } else if (j > 4) {
                        blackFields[i][j] = new Field(i, j, Field.State.whiteKing);
                    } else{
                        blackFields[i][j] = new Field(i, j, Field.State.empty);
                    }
                } else {
                    blackFields[i][j] = new Field(i, j, Field.State.whiteField);
                }
            }
        }
    }

    public Field foundField(int x, int y){ return blackFields[x][y]; }
    //public Rectangle searchedRectangle(int x, int y){ return blackFields[x][y].getRectangle(); }

    public int getClickedX() {
        return clickedX;
    }

    public void setClickedX(int clickedX) {
        this.clickedX = clickedX;
    }

    public int getClickedY() {
        return clickedY;
    }

    public void setClickedY(int clickedY) {
        this.clickedY = clickedY;
    }

    public void setSelectedField(Field selectedField) { this.selectedField = selectedField; }
    public Field getSelectedField() { return selectedField; }

    public void setMarkedFields(ArrayList<Field> markedFields) {
        this.markedFields = markedFields;
    }

    public void setMarkedField(Field field){
        markedFields.add(field);
    }
    public void exchangeStates(Field field1, Field field2){
        field2.setStateField(field1.getState());
        field1.setStateField(Field.State.empty);
    }
    public boolean markedFieldsIsEmpty(){
        return markedFields.isEmpty();
    }

    /*public Field getMarkedField(){

        return markedFields;
    }*/

    public void clearStoke(Rectangle field){
        field.setStroke(Color.GRAY);
    }
    public void clearMarked(){
        if(!markedFieldsIsEmpty()) {
            for (int i = 0; i < markedFields.size(); i++) {
                clearStoke(markedFields.get(i).getRectangle());
            }
            markedFields.clear();
        }
    }
    public void drawMarkedFields(){
        if(!markedFieldsIsEmpty()) {
            for (int i = 0; i < markedFields.size(); i++) {
                markedFields.get(i).getRectangle().setStroke(Color.GREEN);
            }
        }
    }

    public void clearSelected(){
        if(selectedField != null) {
            clearStoke(selectedField.getRectangle());
            selectedField = null;
        }
    }
    public boolean isMarkedFields(){
        if(!markedFieldsIsEmpty()) {
            for (int i = 0; i < markedFields.size(); i++) {
                if(foundField(clickedX, clickedY).equals(markedFields.get(i)))
                    return true;
            }
        }
        return false;
    }
    public void addMarkedFields(Field field){
        markedFields.add(field);
    }
    public Field getClickedField(){
        return foundField(clickedX, clickedY);
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
