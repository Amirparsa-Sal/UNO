package com.amirparsa.salmankhah;

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Represents a card in UNO game.
 * @author Amirparsa Salmankhah
 * @version 1.0.0
 */
public class Card {
    //card values
    public static HashMap<Character, Integer> values;
    //card signs
    public static HashMap<Character, String> signs;
    //text colors
    public static HashMap<String, String> colorCodes;
    //Background colors
    public static HashMap<String, String> backgroundCodes;
    //Card color Red, Green, Blue, Yellow, White
    private String color;
    //Card sign  0, 1, 2, 3, 4, 5, 6, 7, 8, 9, D, R, S, C, W
    private char sign;
    //is Active?
    private boolean active;

    static {
        //foreground codes
        colorCodes = new HashMap<>();
        colorCodes.put("Red", "\u001B[31m");
        colorCodes.put("Green", "\u001B[92m");
        colorCodes.put("Blue", "\u001B[34m");
        colorCodes.put("Yellow", "\u001B[93m");
        colorCodes.put("White", "\u001B[37m");
        colorCodes.put("Reset", "\u001B[0m");
        //background colors
        backgroundCodes = new HashMap<>();
        backgroundCodes.put("Red", "\u001B[41m");
        backgroundCodes.put("Green", "\u001B[102m");
        backgroundCodes.put("Blue", "\u001B[44m");
        backgroundCodes.put("Yellow", "\u001B[103m");
        backgroundCodes.put("White", "\u001B[47m");
        backgroundCodes.put("Reset", "\u001B[0m");
        //signs descreption
        signs = new HashMap<>();
        signs.put('W', "+4");
        signs.put('C', "Color");
        signs.put('D', "+2");
        signs.put('R', "Reverse");
        signs.put('S', "Skip");
        for(int i=0;i<10;i++)
            signs.put((char)('0'+i),"" + (char)('0'+i));
        //values
        values = new HashMap<>();
        for (int i = 0; i < 10; i++)
            values.put((char) ('0' + i), i);
        values.put('R', 20);
        values.put('S', 20);
        values.put('D', 20);
        values.put('C', 50);
        values.put('W', 50);
    }

    /**
     * Constructor with no parameter.
     */
    public Card() {
        this("", '\0', false);
    }

    /**
     * Constructor with 3 parameters.
     * @param color Card's color
     * @param sign Card's sign
     * @param active Card's activity.(is actionable?)
     */
    public Card(String color, char sign, boolean active) {
        this.color = color;
        this.sign = sign;
        this.active = active;
    }

    /**
     * checks if card is actionable or not.
     * @return true if it is and false if not.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the activity of card.
     * @param active Card's activity
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the color of card.
     * @return Card's color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color of card.
     * @param color Card's color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets the sign of the card.
     * @return Card's sign
     */
    public char getSign() {
        return sign;
    }

    /**
     * Sets the sign of the card.
     * @param sign Card's sign
     */
    public void setSign(char sign) {
        this.sign = sign;
    }

    /**
     * Converts the card to an String.
     * @return The card as an String
     */
    @Override
    public String toString() {
        String output = new String("");
        try {
            //Path of cards    size of each card: 35*19
            String path = "Assets\\";
            File file = new File(path + sign + ".txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                output += line + "\n";
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error! Resources not found.");
            System.exit(-1);
        }
        return colorCodes.get(color) + output + colorCodes.get("Reset");
    }

    /**
     * Checks the equality between the card and other object.
     * @param o Other object
     * @return true if they are equal and false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return sign == card.sign && color.equals(card.color);
    }

    /**
     * Checks if the card can come after the another card.
     * @param anotherCard Another card
     * @return true if it can and false if not.
     */
    public boolean canComeAfter(Card anotherCard) {
        if (anotherCard.getSign() == 'D' && anotherCard.isActive()) return sign == 'D';
        if (this instanceof WildCard) {
            if (this.getSign() == 'W')
                return true;
            return !anotherCard.isActive();
        }
        if (anotherCard instanceof WildCard)
            return !((WildCard) anotherCard).isActive() && this.getColor().equals(((WildCard) anotherCard).getRealColor());
        return sign == anotherCard.sign || color.equals(anotherCard.color);
    }

    /**
     * Copies the card.
     * @return New copy of the card.
     */
    public Card copy() {
        return new Card(color, sign, active);
    }

    /**
     * Resets the card.
     * @return New copy of card with primitive situation.
     */
    public Card reset() {
        Card card = copy();
        if (card instanceof WildCard) {
            ((WildCard) card).setRealColor("");
            card.setColor("White");
        }
        if (card.getSign() == 'D' || card.getSign() == 'W')
            card.setActive(true);
        return card;
    }
}