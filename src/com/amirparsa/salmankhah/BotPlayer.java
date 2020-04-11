package com.amirparsa.salmankhah;

public class BotPlayer extends Player{

    public BotPlayer(){
        super();
    }

    public BotPlayer(String name){
        super(name);
    }

    @Override
    public Card think(Card lastCard) {
        return null;
    }
}
