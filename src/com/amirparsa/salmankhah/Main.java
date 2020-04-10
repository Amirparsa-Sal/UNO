package com.amirparsa.salmankhah;

import java.util.Calendar;

public class Main {

    public static void main(String[] args) {
        Deck deck = new Deck();
        Deck deck2 = new Deck();
        for(int i=0;i<10;i++) {
            Card card1 = new Card("Red", (char)('0'+i));
            Card card2 = new Card("Green", (char)('0'+i));
            Card card3 = new Card("Blue", (char)('0'+i));
            Card card4 = new Card("Yellow", (char)('0'+i));
            deck.addCard(card1);
            deck.addCard(card2);
            deck.addCard(card3);
            deck.addCard(card4);
            deck2.addCard(card1);
            deck2.addCard(card2);
            deck2.addCard(card3);
            deck2.addCard(card4);
        }

        Card card1 = new Card("Red", 'R');
        Card card2 = new Card("Green",'R');
        Card card3 = new Card("Blue", 'R');
        Card card4 = new Card("Yellow", 'R');
        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);
        deck.addCard(card4);
        deck2.addCard(card1);
        deck2.addCard(card2);
        deck2.addCard(card3);
        deck2.addCard(card4);

        card1 = new Card("Red", 'D');
        card2 = new Card("Green",'D');
        card3 = new Card("Blue", 'D');
        card4 = new Card("Yellow", 'D');
        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);
        deck.addCard(card4);
        deck2.addCard(card1);
        deck2.addCard(card2);
        deck2.addCard(card3);
        deck2.addCard(card4);

        Card card5 = new WildCard('C');
        Card card6 = new WildCard('W');
        deck.addCard(card5);
        deck.addCard(card6);
        deck2.addCard(card5);
        deck2.addCard(card6);

        deck.print(4);
        System.out.println(deck.equals(deck2));
    }
}
