package com.amirparsa.salmankhah;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a collection of cards.
 * @author Amirparsa Salmankhah
 * @version 1.0.0
 */
public class Deck {
    //List of cards
    private ArrayList<Card> listOfCards;

    /**
     * Constructor with no parameter.
     */
    public Deck() {
        listOfCards = new ArrayList<>();
    }

    /**
     * Gets the size of the Deck.
     * @return Size of the Deck
     */
    public int getSize() {
        return listOfCards.size();
    }

    /**
     * Gets the list of cards.
     * @return An ArrayList of cards.
     */
    public ArrayList<Card> getCards() {
        return listOfCards;
    }

    /**
     * Sets the list of cards.
     * @param listOfCards An ArrayList of cards
     */
    public void setCards(ArrayList<Card> listOfCards) {
        this.listOfCards = listOfCards;
    }

    /**
     * Add a card to Deck.
     * @param card The card
     */
    public void addCard(Card card) {
        listOfCards.add(card);
    }

    /**
     * Searchs for a card in Deck.
     * @param card The card
     * @return Index of the card if it exists and -1 if not.
     */
    private int searchForCard(Card card) {
        for (int i = 0; i < getSize(); i++)
            if (listOfCards.get(i).equals(card))
                return i;
        return -1;
    }

    /**
     * Removes a card from the deck just one time.
     * @param card The card
     * @return true if the removing process was successful and false if not.
     */
    public boolean removeCard(Card card) {
        int index = searchForCard(card);
        if (index == -1)
            return false;
        listOfCards.remove(index);
        return true;
    }

    /**
     * Filters the cards that have an specific color.
     * @param color The color
     * @return A Deck of filtered cards.
     */
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

    /**
     * Prints the deck.
     * @param maxNumberInRow Maximum number of cards in a row.
     */
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

    /**
     * Checks the equality between the Deck and other object.
     * @param o Other object.
     * @return true if they are equal and false if not.
     */
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

    /**
     * Copies the Deck.
     * @return New copy of Deck
     */
    public Deck copy() {
        ArrayList<Card> newList = new ArrayList<>(listOfCards);
        Deck newDeck = new Deck();
        newDeck.setCards(newList);
        return newDeck;
    }
}
