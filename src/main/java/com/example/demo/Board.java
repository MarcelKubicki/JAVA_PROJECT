package com.example.demo;

public class Board {
    // int xz = 244, yz = 244;
    int marginX = 10, marginY = 10;
    Field[] boardWhite = new Field[32];
    Field[] boardBlack = new Field[32];

    public void Init() {
        int move = 60;
        int x = marginX + move, y = marginY;
        for (int i = 1; i < 33; i++) {
            boardWhite[i] = new Field(x, y);
            x += move;

            if (i % 4 == 0) {
                y += move;

                if (i % 8 == 0) {
                    x = marginX;
                } else {
                    x = marginX + 30;
                }
            }
        }
    }
}
