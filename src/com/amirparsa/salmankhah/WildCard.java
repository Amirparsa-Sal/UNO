package com.amirparsa.salmankhah;

public class WildCard extends Card {
    //WildCard real color
    private String realColor=null;

    public WildCard(){
        super();
    }

    public WildCard(char sign){
        this.setSign(sign);
        this.setColor("White");
    }

    public void setRealColor(String realColor) {
        this.realColor = realColor;
    }

    public String getRealColor() {
        return realColor;
    }

    @Override
    public Card copy(){
        return new WildCard(getSign());
    }
}
