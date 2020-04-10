package com.amirparsa.salmankhah;

import java.util.Calendar;

public class Main {

    public static void main(String[] args) {
        Game game = new Game(3);
        game.setPlayer(1);
        game.setPlayer(2);
        game.setPlayer(3);
        game.init();
        game.playTurn();
    }
}
