package com.amirparsa.salmankhah;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a bot player.
 * @author Amirparsa Salmankhah
 * @version 1.0.0
 */
public class BotPlayer extends Player {
    //list of other players cons.
    public static ArrayList<HashMap<String, Boolean>> cons;
    //HashMap of corresponding numbers and colors.
    private static HashMap<Integer, String> colors;

    static {
        cons = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            HashMap<String, Boolean> colors = new HashMap<>();
            colors.put("Red", false);
            colors.put("Green", false);
            colors.put("Blue", false);
            colors.put("Yellow", false);
            colors.put("White", false);
            cons.add(colors);
        }
        colors = new HashMap<>();
        colors.put(0, "Red");
        colors.put(1, "Green");
        colors.put(2, "Blue");
        colors.put(3, "Yellow");
        colors.put(4, "White");
    }

    /**
     * Constructor with no parameter.
     */
    public BotPlayer() {
        super();
    }

    /**
     * Constructor with one parameter.
     * @param name Bot's name
     */
    public BotPlayer(String name) {
        super(name);
    }

    /**
     * Chooses player's card to play.
     * @return The chosen card.
     */
    @Override
    public Card think() {
        Card lastCard = getGame().getLastCard();
        HashMap<Integer, Integer> turns = getGame().getTurns();
        //determining next players
        int turn = getGame().getTurn();
        int nextTurn = turns.get(turn);
        int doubleNextTurn = turns.get(nextTurn);
        int prevTurn = 0;
        for (int i = 0; i < getGame().getNumberOfPlayers(); i++) {
            if (turns.get(i) == turn) {
                prevTurn = i;
                break;
            }
        }
        //searching for cons
        int opponent;
        Deck cons = new Deck();
        Deck availableMoves = availableMoves(lastCard);
        for (Card card : availableMoves.getCards()) {
            if (card.getSign() == 'S')
                opponent = doubleNextTurn;
            else if (card.getSign() == 'R')
                opponent = prevTurn;
            else
                opponent = nextTurn;

            if (BotPlayer.cons.get(opponent).get(card.getColor()))
                cons.addCard(card);
        }
        //if we dont have cons
        if (cons.getSize() == 0)
            cons = availableMoves;
        //choosing best color
        ArrayList<Deck> filteredCards = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            filteredCards.add(cons.filter(BotPlayer.colors.get(i)));
        int bestColor = 0;
        int max = 0;
        int min = 108;
        for (int i = 0; i < 5; i++) {
            if (filteredCards.get(i).getSize() > max) {
                bestColor = i;
                max = filteredCards.get(i).getSize();
            }
        }
        //choosing the best card
        int value = -1;
        Card card = null;
        for (int i = 0; i < max; i++) {
            char sign = filteredCards.get(bestColor).getCards().get(i).getSign();
            if (Card.values.get(sign) > value) {
                card = filteredCards.get(bestColor).getCards().get(i);
                value = Card.values.get(sign);
            }
        }
        //printing bot choice
        char sign = card.getSign();
        String cardColor;
        System.out.print("Chosen Card: ");
        String descreption =  Card.signs.get(sign);
        if (card instanceof WildCard)
            cardColor = "Wild";
        else {
            cardColor = card.getColor();
        }
        System.out.println(cardColor + " " + descreption);
        System.out.println();
        //remove card
        getDeck().removeCard(card);
        return card;
    }

    /**
     * Chooses bot's color to be played after WildCards.
     * @return The chosen color
     */
    @Override
    public String chooseColor() {
        System.out.println(getName() + " is choosing color...");
        ArrayList<Deck> filteredDeck = new ArrayList<>();
        int max = 0;
        int chosenColor = 0;
        for (int i = 0; i < 4; i++) {
            if (getDeck().filter(BotPlayer.colors.get(i)).getSize() > max) {
                chosenColor = i;
                max = getDeck().filter(BotPlayer.colors.get(i)).getSize();
            }
        }
        System.out.println("Chosen color: " + BotPlayer.colors.get(chosenColor));
        return BotPlayer.colors.get(chosenColor);
    }
}

