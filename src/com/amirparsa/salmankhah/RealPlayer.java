package com.amirparsa.salmankhah;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Represents a real player.
 * @author Amirparsa Salmankhah
 * @version 1.0.0
 */
public class RealPlayer extends Player {

    /**
     * Constructor with no parameter.
     */
    public RealPlayer() {
        super();
    }

    /**
     * Constructor with 1 parameter.
     * @param name Player's name
     */
    public RealPlayer(String name) {
        super(name);
    }

    /**
     * Chooses player's card to play.
     * @return The chosen card.
     */
    @Override
    public Card think() {
        Card lastCard = getGame().getLastCard();
        //show available moves
        Deck availableMoves = availableMoves(lastCard);
        for (int i = 0; i < availableMoves.getSize(); i++) {
            String color;
            Card card = availableMoves.getCards().get(i);
            char sign = card.getSign();
            String descreption = Card.signs.get(sign);;
            if (card instanceof WildCard)
                color = "Wild";
            else
                color = card.getColor();
            System.out.print((i + 1) + ")  " + color + "  " + descreption);
            System.out.println();
        }
        //getting move
        Scanner scanner = new Scanner(System.in);
        int choose = 0;
        while (choose < 1 || choose > availableMoves.getSize()) {
            try {
                System.out.println(getName() + " please enter the number of card:");
                choose = scanner.nextInt();
            }
            catch(InputMismatchException e){
                System.out.println("Please enter a valid number!");
                scanner.next();
            }
        }
        Card chosenCard = availableMoves.getCards().get(choose - 1);
        System.out.println();
        //remove card
        getDeck().removeCard(chosenCard);
        return chosenCard;
    }

    /**
     * Chooses player's color to be played after WildCards.
     * @return The chosen color
     */
    @Override
    public String chooseColor() {
        Scanner scanner = new Scanner(System.in);
        String color = "";
        while (!color.toLowerCase().equals("red") && !color.toLowerCase().equals("green") && !color.toLowerCase().equals("blue") && !color.toLowerCase().equals("yellow")) {
            System.out.println(getName() + " please enter the new color (example: Red):");
            color = scanner.next();
        }
        color = color.toLowerCase();
        color = color.substring(0, 1).toUpperCase() + color.substring(1);
        return color;
    }
}
