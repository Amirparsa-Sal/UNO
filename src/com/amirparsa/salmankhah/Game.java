package com.amirparsa.salmankhah;

import java.util.*;

public class Game {
    //seperator line
    private static final String line = Card.backgroundCodes.get("Green") + "                                             " +
            Card.backgroundCodes.get("Reset");
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
    //turn
    int turn;
    //direction of the game
    int direction;
    //Penalty
    int penalty;

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
            allCards.addCard(new Card(colors.get(i), '0',false));

        for (int count = 0; count < 2; count++) {
            //Adding 1-9
            for (int number = 1; number < 10; number++)
                for (int i = 1; i <= 4; i++)
                    allCards.addCard(new Card(colors.get(i), (char) ('0' + number),false));

            //Adding Reverse and Draw
            for (int i = 1; i <= 4; i++) {
                allCards.addCard(new Card(colors.get(i), 'R',false));
                allCards.addCard(new Card(colors.get(i), 'D',true));
                allCards.addCard(new Card(colors.get(i), 'S',false));
            }
        }
        //Adding Wilds
        for (int i = 1; i <= 4; i++) {
            allCards.addCard(new WildCard('C',false));
            allCards.addCard(new WildCard('W',true));
        }
    }

    public Game(int playerNumber) {
        players = new Player[playerNumber];
        queue = new LinkedList<>();
        turn = 0;
        direction = 1;
        penalty = 0;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayer(int playerNumber) {
        Scanner scanner = new Scanner(System.in);
        String type = "";
        while (!type.equals("bot") && !type.equals("player")) {
            System.out.println("Please enter player" + playerNumber + "'s type (bot or player):");
            type = scanner.next();
            type = type.toLowerCase();
        }
        String name;
        if (type.equals("player")) {
            System.out.println("Please enter player" + playerNumber + "'s name:");
            name = scanner.next();
            players[playerNumber - 1] = new RealPlayer(name);
            System.out.println("Player(" + name + ") added!");
        } else {
            name = "Bot" + playerNumber;
            players[playerNumber - 1] = new BotPlayer(name);
            System.out.println("Bot(" + name + ") added!");
        }
        System.out.println(line);
    }

    public int getNumberOfPlayers() {
        return players.length;
    }

//    public void lastCardAction(){
//        if (lastCard instanceof WildCard){
//            if(lastCard.getSign()=='W'){
//        }
//    }

    public void init() {
        Random random = new Random();
        int allCardsSize = 108;
        int cardIndex;
        //Players deck
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            for (int cardNumbers = 0; cardNumbers < 7; cardNumbers++) {
                cardIndex = random.nextInt(allCardsSize);
                players[i].getDeck().addCard(allCards.getCards().get(cardIndex));
                allCards.getCards().remove(cardIndex);
                allCardsSize--;
            }
        }
        //Last card
        lastCard = new WildCard();
        while(lastCard instanceof WildCard) {
            cardIndex = random.nextInt(allCardsSize);
            lastCard = allCards.getCards().get(cardIndex);
        }
        if(lastCard.getSign()=='D')
            penalty+=2;
        else if(lastCard.getSign()=='R')
            direction*=-1;
        allCardsSize--;
        //Queue
        for (int i = 0; i < allCardsSize; i++) {
            cardIndex = random.nextInt(allCardsSize);
            queue.add(allCards.getCards().get(cardIndex));
        }
    }

    public Deck chooseCardsFromQueue(int number, Player player){
        Deck givenCards = new Deck();
        for(int i=0;i<number;i++){
            Card card = queue.remove();
            player.getDeck().addCard(card);
            givenCards.addCard(card);
        }
        return givenCards;
    }

    public void penalizePlayer(){
        if(lastCard instanceof WildCard || lastCard.getSign()=='D'){
            Deck givenCards = chooseCardsFromQueue(penalty, players[turn]);
            lastCard.setActive(false);
            System.out.println(penalty + " cards added to " + players[turn].getName() + "'s deck!");
            System.out.println();
            System.out.println("Given Cards:");
            givenCards.print(4);
            penalty = 0;

        }
        else {
            Deck givenCards = chooseCardsFromQueue(1, players[turn]);
            System.out.println(1 + " card added to " + players[turn].getName() + "'s deck!");
            System.out.println();
            System.out.println("Given card:");
            givenCards.print(4);
            if(players[turn].availableMoves(lastCard).getSize()!=0)
                turn--;
        }
    }

    public void playTurn() {
        System.out.println("Game Players:");
        Player player = players[turn];
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            String color = "";
            if (i == turn)
                color = Card.backgroundCodes.get("Red");
            System.out.print(color + players[i].getName() + Card.backgroundCodes.get("Reset") + "  ");
        }
        System.out.println();
        System.out.println();
        System.out.println("It's " + player.getName() + "'s turn!");
        System.out.println(player.getName() + "'s Deck:");
        player.getDeck().print(4);
        System.out.println();
        System.out.println("Last Card:");
        System.out.println(lastCard);
        System.out.println();
        System.out.println();
        Card card;
        Deck availableMoves = player.availableMoves(lastCard);
        if(availableMoves.getSize()==0)
            penalizePlayer();
        else {
            card = player.think(lastCard);
            if(lastCard instanceof WildCard)
                ((WildCard) lastCard).setActive(true);
            queue.add(lastCard.copy());
            lastCard = card;
            if(lastCard instanceof WildCard){
                if(lastCard.getSign()=='W')
                    penalty+=4;
                lastCard.setColor(((WildCard) lastCard).getRealColor());
            }
            else{
                if(lastCard.getSign()=='R')
                    direction*=-1;
                else if(lastCard.getSign()=='D')
                    penalty+=2;
                else if(lastCard.getSign()=='S')
                    turn++;
            }
        }
        turn += direction;
        if(turn==-1)
            turn=getNumberOfPlayers()-1;
        turn %= getNumberOfPlayers();
        System.out.println(line);
    }
}

