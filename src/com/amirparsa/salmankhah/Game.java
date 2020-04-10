package com.amirparsa.salmankhah;

import java.util.*;

public class Game {
    //Colors set
    public static final HashMap<Integer, String> colors;
    //All of cards
    public static Deck allCards;
    //Players
    Player[] players;
    //Cards Queue
    Queue<Card> queue;
    //LAST Card
    Card lastCard;

    static {
        //init colors
        colors = new HashMap<>();
        colors.put(1, "Red");
        colors.put(2, "Green");
        colors.put(3, "Blue");
        colors.put(4, "Yellow");
        //init cards
        allCards = new Deck();
        //Adding zeros
        for (int i = 1; i <= 4; i++)
            allCards.addCard(new Card(colors.get(i), '0'));

        for (int count = 0; count < 2; count++) {
            //Adding 1-9
            for (int number = 1; number < 10; number++)
                for (int i = 1; i <= 4; i++)
                    allCards.addCard(new Card(colors.get(i), (char) ('0' + number)));

            //Adding Reverse and Draw
            for (int i = 1; i <= 4; i++) {
                allCards.addCard(new Card(colors.get(i), 'R'));
                allCards.addCard(new Card(colors.get(i), 'D'));
            }

            //Adding Wilds
            for (int i = 1; i <= 4; i++) {
                allCards.addCard(new WildCard('C'));
                allCards.addCard(new WildCard('W'));
            }
        }
    }

    public Game(int playerNumber) {
        players = new Player[playerNumber];
        queue = new LinkedList<>();
    }

    public Player[] getPlayers(){
        return players;
    }

    public void setPlayer(int playerNumber){
        Scanner scanner = new Scanner(System.in);
        String type = "";
        while(!type.equals("bot") && !type.equals("player")) {
            System.out.println("Please enter player" + playerNumber + "'s type (bot or player):");
            type = scanner.next();
            type = type.toLowerCase();
        }
        String name;
        if(type.equals("player")) {
            System.out.println("Please enter player" + playerNumber + "'s name:");
            name = scanner.next();
            players[playerNumber-1] = new RealPlayer(name);
            System.out.println("Player(" + name + ") added!");
        }
        else{
            name = "Bot" + playerNumber;
            players[playerNumber-1] = new BotPlayer(name);
            System.out.println("Bot(" + name + ") added!");
        }
    }

    public int getNumberOfPlayers() {
        return players.length;
    }

    public void init() {
        Random random = new Random();
        int cardIndex = random.nextInt(108);
        lastCard = allCards.getCards().get(cardIndex);
        int allCardsSize = 107;

        for (int i = 0; i < getNumberOfPlayers(); i++) {
            for (int cardNumbers = 0; cardNumbers < 7; cardNumbers++) {
                cardIndex = random.nextInt(allCardsSize);
                players[i].getDeck().addCard(allCards.getCards().get(cardIndex));
                allCards.getCards().remove(cardIndex);
                allCardsSize--;
            }
        }
        for (int i = 0; i < allCardsSize; i++) {
            cardIndex = random.nextInt(allCardsSize);
            queue.add(allCards.getCards().get(i));
        }
    }
}
