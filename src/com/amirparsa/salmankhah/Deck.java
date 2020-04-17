package com.amirparsa.salmankhah;

import java.util.ArrayList;
import java.util.Iterator;

public class Deck {
    //List of cards
    private ArrayList<Card> listOfCards;

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

    public Deck filter(String color) {
        Deck deck = copy();
        Iterator<Card> it = deck.getCards().iterator();
        while (it.hasNext()) {
            String clr = it.next().getColor();
            if (!color.equals(clr))
                it.remove();
        }
        return deck;
    }

    public void print(int maxNumberInRow) {
        int limit = getSize() / maxNumberInRow;
        if (getSize() % maxNumberInRow == 0)
            limit--;
        for (int count = 0; count <= limit; count++) {
            int first = count * maxNumberInRow, end = Math.min(first + maxNumberInRow, getSize());
            for (int line = 0; line < 19; line++) {
                for (int i = first; i < end; i++) {
                    String card = listOfCards.get(i).toString().split("\n")[line];
                    System.out.print(Card.colorCodes.get(listOfCards.get(i).getColor()) + card + "  " + Card.colorCodes.get("Reset"));
                }
                System.out.println();
            }
        }
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        //checking equality of two decks
        Deck tmp = (Deck) o;
        if (this.getSize() != tmp.getSize())
            return false;
        for (int i = 0; i < tmp.getSize(); i++)
            if (searchForCard(tmp.getCards().get(i)) == -1)
                return false;
        for (int i = 0; i < this.getSize(); i++)
            if (tmp.searchForCard(this.getCards().get(i)) == -1)
                return false;
        return true;
    }

    public Deck copy() {
        ArrayList<Card> newList = new ArrayList<>(listOfCards);
        Deck newDeck = new Deck();
        newDeck.setCards(newList);
        return newDeck;
    }
}
