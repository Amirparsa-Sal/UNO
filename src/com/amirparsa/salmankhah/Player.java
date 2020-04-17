package com.amirparsa.salmankhah;

import java.util.Iterator;

/**
 * Represents a player.
 * @author Amirparsa Salmankhah
 * @version 1.0.0
 */
abstract public class Player {
    //Player's name
    private String name;
    //Player's deck
    private Deck deck;
    //Player's game
    private Game game;

    /**
     * Constructor with no parameter.
     */
    public Player() {
        this("");
    }

    /**
     * Constructor with 1 parameter.
     * @param name Player's name
     */
    public Player(String name) {
        this.name = name;
        deck = new Deck();
    }

    /**
     * Gets name of the player.
     * @return Player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of the player.
     * @param name Player's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets Deck of the player.
     * @return Player's Deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Sets Deck of the player.
     * @param deck Player's Deck
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    /**
     * Gets game of the player.
     * @return Player's active game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets game of the player.
     * @param game Player's active game
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Check the equality between the player and other object.
     * @param o The other object.
     * @return true if they are equal and false if not.
     */
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        Player tmp = (Player) o;
        return name.equals(tmp.name) && deck.equals(tmp.deck);
    }

    /**
     * Searchs for the cards that can be played.
     * @param lastCard last card that is played.
     * @return A Deck of available cards.
     */
    public Deck availableMoves(Card lastCard) {
        boolean wilds = true;
        Deck availableMoves = new Deck();
        for (Card card : deck.getCards()) {
            if (card.canComeAfter(lastCard)) {
                availableMoves.addCard(card);
                if (!(card instanceof WildCard))
                    wilds = false;
            }
        }
        if (!wilds) {
            Iterator<Card> it = availableMoves.getCards().iterator();
            while (it.hasNext()) {
                Card card = it.next();
                if (card.getSign() == 'W')
                    it.remove();
            }
        }
        return availableMoves;
    }

    /**
     * Calculates the score of the player's Deck.
     * @return The score
     */
    public int getScore() {
        int score = 0;
        for (Card card : deck.getCards())
            score += Card.values.get(card.getSign());
        return score;
    }

    /**
     * Chooses player's card to play.
     * @return The chosen card.
     */
    abstract public Card think();

    /**
     * Chooses player's color to be played after WildCards.
     * @return The chosen color
     */
    abstract public String chooseColor();
}
