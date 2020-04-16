package com.amirparsa.salmankhah;

import java.util.*;

public class Game {
    //seperator line
    private static final String line = Card.backgroundCodes.get("Green") + "                                                                                      " +
            Card.backgroundCodes.get("Reset");
    //Colors set
    public static final HashMap<Integer, String> colors;
    //All of cards
    public static Deck allCards;
    //Players
    private Player[] players;
    //Cards Queue
    private Queue<Card> queue;
    //LAST Card
    private Card lastCard;
    //turns
    public HashMap<Integer, Integer> turns;
    //turn
    private int turn;
    //direction of the game
    private int direction;
    //Penalty
    private int penalty;

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
            allCards.addCard(new Card(colors.get(i), '0', false));

        for (int count = 0; count < 2; count++) {
            //Adding 1-9
            for (int number = 1; number < 10; number++)
                for (int i = 1; i <= 4; i++)
                    allCards.addCard(new Card(colors.get(i), (char) ('0' + number), false));

            //Adding Reverse and Draw
            for (int i = 1; i <= 4; i++) {
                allCards.addCard(new Card(colors.get(i), 'R', false));
                allCards.addCard(new Card(colors.get(i), 'D', true));
                allCards.addCard(new Card(colors.get(i), 'S', false));
            }
        }
        //Adding Wilds
        for (int i = 1; i <= 4; i++) {
            allCards.addCard(new WildCard('C', false));
            allCards.addCard(new WildCard('W', true));
        }
    }

    public Game(int playerNumber) {
        players = new Player[playerNumber];
        queue = new LinkedList<>();
        turns = new HashMap<>();
        turn = 0;
        direction = 1;
        penalty = 0;
        for (int i = 0; i < playerNumber - 1; i++)
            turns.put(i, i + 1);
        turns.put(playerNumber - 1, 0);

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
            players[playerNumber - 1].setGame(this);
            System.out.println("Player(" + name + ") added!");
        } else {
            name = "Bot" + playerNumber;
            players[playerNumber - 1] = new BotPlayer(name);
            players[playerNumber - 1].setGame(this);
            System.out.println("Bot(" + name + ") added!");
        }
        System.out.println(line);
    }

    public int getNumberOfPlayers() {
        return players.length;
    }

    public Card getLastCard() {
        return lastCard;
    }

    public int getTurn() {
        return turn;
    }

    public int getDirection() {
        return direction;
    }

    public HashMap<Integer, Integer> getTurns() {
        return turns;
    }

    public void changeDirection(){
        if(direction==1){
            for (int i = 0; i < getNumberOfPlayers() - 1; i++)
                turns.put(i, i + 1);
            turns.put(getNumberOfPlayers() - 1, 0);
        }
        else{
            for (int i = getNumberOfPlayers()-1; i > 0; i--)
                turns.put(i, i - 1);
            turns.put(0, getNumberOfPlayers()-1);
        }
    }

    public boolean isInProgress() {
        for (Player player : players)
            if (player.getDeck().getSize() == 0)
                return false;
        return true;
    }

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
        lastCard = new WildCard('W',true);
        while (lastCard.getSign()=='W') {
            cardIndex = random.nextInt(allCardsSize);
            lastCard = allCards.getCards().get(cardIndex);
        }
        checkCardAction(lastCard,0);
        allCardsSize--;
        //Queue
        for (int i = 0; i < allCardsSize; i++) {
            cardIndex = random.nextInt(allCardsSize);
            queue.add(allCards.getCards().get(cardIndex));
        }
    }

    private Deck chooseCardsFromQueue(int number, Player player) {
        Deck givenCards = new Deck();
        for (int i = 0; i < number; i++) {
            Card card = queue.remove();
            player.getDeck().addCard(card);
            givenCards.addCard(card);
        }
        return givenCards;
    }

    private void penalizePlayer() {
        if ((lastCard.getSign() == 'W' || lastCard.getSign() == 'D') && lastCard.isActive()) {
            Deck givenCards = chooseCardsFromQueue(penalty, players[turn]);
            lastCard.setActive(false);
            System.out.println(penalty + " cards added to " + players[turn].getName() + "'s deck!");
            System.out.println();
            System.out.println("Given Cards:");
            givenCards.print(4);
            penalty = 0;
            BotPlayer.cons.get(turn).replace("Red",false);
            BotPlayer.cons.get(turn).replace("Green",false);
            BotPlayer.cons.get(turn).replace("Blue",false);
            BotPlayer.cons.get(turn).replace("Yellow",false);
            BotPlayer.cons.get(turn).replace("White",false);

        } else {
            BotPlayer.cons.get(turn).replace(lastCard.getColor(),true);
            Deck givenCards = chooseCardsFromQueue(1, players[turn]);
            System.out.println(1 + " card added to " + players[turn].getName() + "'s deck!");
            System.out.println();
            System.out.println("Given card:");
            givenCards.print(4);
            if (players[turn].availableMoves(lastCard).getSize() != 0) {
                direction *= -1;
                changeDirection();
                turn = turns.get(turn);
                direction *=-1;
                changeDirection();
            }
        }
    }

    private void showNumberOfCards() {
        System.out.println("Number of cards:");
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            Player player = players[i];
            System.out.print(player.getName() + "(" + player.getDeck().getSize() + ")" + "  ");
        }
        System.out.println();
    }

    private void showDirection() {
        System.out.print("Direction: ");
        if (direction == 1)
            System.out.println(">>>>>>>>>>>>>>>>>");
        else
            System.out.println("<<<<<<<<<<<<<<<<<");
    }

    public void checkCardAction(Card card,int playerNumber){
        if(card instanceof WildCard) {
            if (card.getSign() == 'W') {
                penalty += 4;
                card.setActive(true);
            }
            String chosenColor = players[playerNumber].chooseColor();
            ((WildCard) card).setRealColor(chosenColor);
        }
        else if (card.getSign() == 'R') {
            direction *= -1;
            changeDirection();
        }else if (card.getSign() == 'D')
            penalty += 2;
        else if (card.getSign() == 'S')
            turn = turns.get(turn);
    }

    public void playTurn() {
        //print info
        System.out.println("Players:");
        Player player = players[turn];
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            String color = "";
            if (i == turn)
                color = Card.backgroundCodes.get("Red");
            System.out.print(color + players[i].getName() + Card.backgroundCodes.get("Reset") + "  ");
        }
        System.out.println();
        showDirection();
        System.out.println();
        showNumberOfCards();
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
        if (availableMoves.getSize() == 0)
            //if player doesnt have any choice
            penalizePlayer();
        else {
            card = player.think();
            queue.add(lastCard.copy().reset());
            lastCard = card;
            if(players[turn].getDeck().getSize()==0)
                return;
            checkCardAction(lastCard,turn);
        }
        turn = turns.get(turn);
        System.out.println(line);
    }
}
