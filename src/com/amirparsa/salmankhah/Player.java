package com.amirparsa.salmankhah;

import java.util.ArrayList;

abstract public class Player {
    //Player's name
    private String name;
    //Player's deck
    private Deck deck;

    public Player(){
        this("");
    }
    public Player(String name){
        this.name = name;
        deck = new Deck();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public boolean equals(Object o){
        if(this==o)
            return true;
        if(o==null ||this.getClass()!=o.getClass())
            return false;
        Player tmp = (Player) o;
        return name.equals(tmp.name) &&  deck.equals(tmp.deck);
    }

    public Deck availableMoves(Card lastCard){
        Deck availableMoves = new Deck();
        for(Card card : deck.getCards())
            if(card.canComeAfter(lastCard))
                availableMoves.addCard(card);
        return availableMoves;
    }

    abstract public Card think(Card lastCard);
}
