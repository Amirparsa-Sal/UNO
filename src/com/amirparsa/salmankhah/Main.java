package com.amirparsa.salmankhah;


public class Main {

    public static void main(String[] args) {
        Game game = new Game(3);
        game.setPlayer(1);
        game.setPlayer(2);
        game.setPlayer(3);
        game.init();
        while (game.isInProgress())
            game.playTurn();
        for(int i=0;i<3;i++) {
            if (game.getPlayers()[i].getDeck().getSize() == 0) {
                System.out.println(game.getLastCard());
                System.out.println();
                System.out.println(game.getPlayers()[i].getName() + " Won!");
                System.out.println();
                game.showScores();
            }
        }
    }
}
