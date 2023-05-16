package com.example.demo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Game {
    private boolean round = true;
    private Field selectedField = null;
    private Rectangle[] markedFields = {null , null, null};

    public void setSelectedField(Field selectedField) { this.selectedField = selectedField; }
    public Field getSelectedField() { return selectedField; }
    public void setMarkedField(int i, Rectangle rectangle){
        this.markedFields[i] = rectangle;
    }
    public Rectangle getMarkedField(int i){
        return markedFields[i];
    }

    public void clearMarked(){
        if(markedFields[0] != null){
            markedFields[0].setStroke(Color.GRAY);
            markedFields[0] = null;
        }

        if(markedFields[2] != null){
            markedFields[2].setStroke(Color.GRAY);
            markedFields[2] = null;
        }
    }

    public void clearSelected(){
            markedFields[1].setStroke((Color.GRAY));
    }
}

