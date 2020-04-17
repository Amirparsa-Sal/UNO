package com.amirparsa.salmankhah;


import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int n = 0;
        while (n < 3 || n > 6) {
            try {
                System.out.println("Please enter the number of players (3 to 6):");
                n = scanner.nextInt();
            }
            catch (InputMismatchException e){
                System.out.println("Please enter a valid number!");
                scanner.next();
            }
        }
        Game game = new Game(n);
        for (int i = 1; i <= n; i++)
            game.setPlayer(i);
        game.init();
        while (game.isInProgress())
            game.playTurn();
        for (int i = 0; i < 3; i++) {
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
