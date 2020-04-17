package com.amirparsa.salmankhah;

/**
 * Represents a WildCard which is a kind of Card.
 * @author Amirparsa Salmankhah
 * @version 1.0.0
 */
public class WildCard extends Card {
    //WildCard real color
    private String realColor = null;

    /**
     * Constructor with no parameter.
     */
    public WildCard() {
        super();
    }

    /**
     * Constructor with 2 parameter.
     * @param sign Card's sign
     * @param active Card's activity
     */
    public WildCard(char sign, boolean active) {
        super("White", sign, active);
    }

    /**
     * Sets the real color of the WildCard.
     * @param realColor Real color as an String
     */
    public void setRealColor(String realColor) {
        this.realColor = realColor;
    }

    /**
     * Gets the real color of the WildCard.
     * @return Real color as an String
     */
    public String getRealColor() {
        return realColor;
    }

    /**
     * Copies the WildCard.
     * @return New copy of the WildCard
     */
    @Override
    public Card copy() {
        return new WildCard(getSign(), isActive());
    }
}
