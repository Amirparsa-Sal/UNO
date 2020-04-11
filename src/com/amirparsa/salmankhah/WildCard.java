package com.amirparsa.salmankhah;

public class WildCard extends Card {
    //WildCard real color
    private String realColor=null;

    public WildCard(){
        super();
    }

    public WildCard(char sign, boolean active){
        super("White",sign,active);
    }

    public void setRealColor(String realColor) {
        this.realColor = realColor;
    }

    public String getRealColor() {
        return realColor;
    }

    @Override
    public Card copy(){
        return new WildCard(getSign(),isActive());
    }
}
