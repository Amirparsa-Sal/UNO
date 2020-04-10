package com.amirparsa.salmankhah;

import java.util.Calendar;

public class Main {

    public static void main(String[] args) {
        Game game = new Game(4);
        game.setPlayer(1);
        game.setPlayer(2);
        game.setPlayer(3);
        game.setPlayer(4);
        game.init();
        for(int i=0;i<4;i++) {
            Player player = game.getPlayers()[i];
            System.out.println(player.getName() + "'s Deck");
            player.getDeck().print(4);
        }
    }
}
