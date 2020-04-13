package com.amirparsa.salmankhah;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RealPlayer extends Player {

    public RealPlayer(){
        super();
    }

    public RealPlayer(String name){
        super(name);
    }

    @Override
    public Card think() {
        Card lastCard = getGame().getLastCard();
        //show available moves
        Deck availableMoves = availableMoves(lastCard);
        for(int i=0;i<availableMoves.getSize();i++){
            String color;
            String sign = "";
            Card card = availableMoves.getCards().get(i);
            sign+=card.getSign();
            if(card instanceof WildCard){
                sign = Card.signs.get(sign);
                color = "Wild";
            }
            else{
                if(sign.equals("R") || sign.equals("S") || sign.equals("D"))
                    sign = sign = Card.signs.get(sign);
                color = card.getColor();
            }
            System.out.print((i+1) + ")  " + color + "  " + sign);
            System.out.println();
        }
        //getting move
        Scanner scanner = new Scanner(System.in);
        int choose = 0;
        while(choose<1 || choose>availableMoves.getSize()) {
            System.out.println(getName() + " please enter the number of card:");
            choose = scanner.nextInt();
        }
        Card chosenCard = availableMoves.getCards().get(choose-1);
        //getting color if the card is wild
        if(chosenCard instanceof WildCard){
            if(chosenCard.getSign()=='W')
                chosenCard.setActive(true);
            String color = "";
            while(!color.toLowerCase().equals("red") && !color.toLowerCase().equals("green") && !color.toLowerCase().equals("blue") &&  !color.toLowerCase().equals("yellow")) {
                System.out.println(getName() + " please enter the new color (example: Red):");
                color = scanner.next();
            }
            color = color.toLowerCase();
            color = color.substring(0, 1).toUpperCase() + color.substring(1);
            ((WildCard) chosenCard).setRealColor(color);

        }
        //remove card
        getDeck().getCards().remove(chosenCard);
        return chosenCard;
    }
}
