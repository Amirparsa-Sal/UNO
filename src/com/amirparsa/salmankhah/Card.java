package com.amirparsa.salmankhah;

import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Card {
    //card signs
    public static HashMap <String,String> signs;
    //text colors
    public static HashMap<String, String> colorCodes;
    //Background colors
    public static HashMap<String, String> backgroundCodes;
    //Path of cards    size of each card: 35*19
    private final String path = "Assets\\";
    //Card color Red, Green, Blue, Yellow, White
    private String color;
    //Card sign  0, 1, 2, 3, 4, 5, 6, 7, 8, 9, D, R, S, C, W
    private char sign;
    //is Active?
    private boolean active;

    static {
        colorCodes = new HashMap<>();
        colorCodes.put("Red", "\u001B[31m");
        colorCodes.put("Green", "\u001B[92m");
        colorCodes.put("Blue", "\u001B[34m");
        colorCodes.put("Yellow", "\u001B[93m");
        colorCodes.put("White", "\u001B[37m");
        colorCodes.put("Reset", "\u001B[0m");
        backgroundCodes = new HashMap<>();
        backgroundCodes.put("Red", "\u001B[41m");
        backgroundCodes.put("Green", "\u001B[102m");
        backgroundCodes.put("Blue", "\u001B[44m");
        backgroundCodes.put("Yellow", "\u001B[103m");
        backgroundCodes.put("White", "\u001B[47m");
        backgroundCodes.put("Reset", "\u001B[0m");
        signs = new HashMap<>();
        signs.put("W","+4");
        signs.put("C","Color");
        signs.put("D","+2");
        signs.put("R","Reverse");
        signs.put("S","Skip");
    }
    public Card() {
        this("", '\0',false);
    }

    public Card(String color, char sign, boolean active) {
        this.color = color;
        this.sign = sign;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public String getPath() {
        return path;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public char getSign() {
        return sign;
    }

    public void setSign(char sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        String output = new String("");
        try {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return sign == card.sign && color.equals(card.color);
    }

    public boolean canComeAfter(Card anotherCard) {
        if(this instanceof WildCard) {
            if (this.getSign() == 'W')
                return true;
            if(anotherCard.isActive())
                return false;
            return true;
        }
        if(anotherCard instanceof WildCard)
            return !((WildCard) anotherCard).isActive() && this.getColor().equals(((WildCard) anotherCard).getRealColor());
        if(anotherCard.getSign()=='D' && anotherCard.isActive())
            return sign=='D';
        return sign == anotherCard.sign || color.equals(anotherCard.color);
    }

    public Card copy(){
        return new Card(color,sign,active);
    }
}