package com.amirparsa.salmankhah;

import java.util.ArrayList;
import java.util.Iterator;

abstract public class Player {
    //Player's name
    private String name;
    //Player's deck
    private Deck deck;
    //Player's game
    private Game game;

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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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
        boolean wilds=true;
        Deck availableMoves = new Deck();
        for(Card card : deck.getCards()) {
            if (card.canComeAfter(lastCard)) {
                availableMoves.addCard(card);
                if(!(card instanceof WildCard))
                    wilds = false;
            }
        }
        if(!wilds){
            Iterator<Card> it = availableMoves.getCards().iterator();
            while(it.hasNext()){
                Card card = it.next();
                if(card.getSign()=='W')
                    it.remove();
            }
        }
        return availableMoves;
    }

    abstract public Card think();
}
