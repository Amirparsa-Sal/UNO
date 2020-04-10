package com.amirparsa.salmankhah;

import java.util.ArrayList;

public class Deck {
    //List of cards
    private ArrayList<Card> listOfCards;
    //needs to add player

    public Deck() {
        listOfCards = new ArrayList<>();
    }

    public int getSize() {
        return listOfCards.size();
    }

    public ArrayList<Card> getCards() {
        return listOfCards;
    }

    public void setCards(ArrayList<Card> listOfCards) {
        this.listOfCards = listOfCards;
    }

    public void addCard(Card card) {
        listOfCards.add(card);
    }

    private int searchForCard(Card card) {
        for (int i = 0; i < getSize(); i++)
            if (listOfCards.get(i).equals(card))
                return i;
        return -1;
    }

    public boolean removeCard(Card card) {
        int index = searchForCard(card);
        if (index == -1)
            return false;
        listOfCards.remove(index);
        return true;
    }

    public void print() {
        int limit = getSize() / 5;
        if (getSize() % 5 == 0)
            limit--;
        for (int count = 0; count <= limit; count++) {
            int first = count * 5, end = Math.min(first + 5, getSize());
            for (int line = 0; line < 19; line++) {
                for (int i = first; i < end; i++) {
                    String card = listOfCards.get(i).toString().split("\n")[line];
                    System.out.print(Card.colorCodes.get(listOfCards.get(i).getColor()) + card + "  " + Card.colorCodes.get("Reset"));
                }
                System.out.println();
            }
        }
    }
}
