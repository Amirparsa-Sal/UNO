package com.amirparsa.salmankhah;

public class Main {

    public static void main(String[] args) {
        for(int i=0;i<10;i++) {
            Card card = new Card("Yellow", (char)('0'+i));
            System.out.println(card);
        }
        Card card = new Card("Green",'D');
        System.out.println(card);
        card = new Card("Blue",'R');
        System.out.println(card);
        card = new Card("White",'C');
        System.out.println(card);
        card = new Card("Red",'W');
        System.out.println(card);
    }
}
