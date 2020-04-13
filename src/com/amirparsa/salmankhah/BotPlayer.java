package com.amirparsa.salmankhah;

import java.util.ArrayList;
import java.util.HashMap;

public class BotPlayer extends Player {
    public static ArrayList<HashMap<String, Boolean>> cons;
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

    public BotPlayer() {
        super();
    }

    public BotPlayer(String name) {
        super(name);
    }

    @Override
    public Card think() {
        Card lastCard = getGame().getLastCard();
        HashMap<Integer, Integer> turns = getGame().getTurns();
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
        //choose best card
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
        String sign = "" + card.getSign();
        String cardColor;
        System.out.print("Chosen Card: ");
        if(card instanceof WildCard)
            System.out.print("Wild " + Card.signs.get(sign));
        else{
            if(sign.equals("R") || sign.equals("S") || sign.equals("D"))
                sign = Card.signs.get(sign);
            cardColor = card.getColor();
            System.out.println(cardColor + " " + sign);
        }
        System.out.println();
        if (card instanceof WildCard) {
            System.out.println("Choosing color...");
            ArrayList<Deck> filteredDeck = new ArrayList<>();
            int mx = 0;
            int chosenColor = 0;
            for (int i = 0; i < 4; i++) {
                if (getDeck().filter(BotPlayer.colors.get(i)).getSize() > mx) {
                    chosenColor = i;
                    mx = getDeck().filter(BotPlayer.colors.get(i)).getSize();
                }
            }
            ((WildCard) card).setRealColor(BotPlayer.colors.get(chosenColor));
            System.out.println("Chosen color: " + BotPlayer.colors.get(chosenColor));
        }
        //remove card
        getDeck().removeCard(card);
        return card;
    }
}
//remove
