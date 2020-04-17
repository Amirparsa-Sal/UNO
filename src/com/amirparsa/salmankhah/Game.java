package com.amirparsa.salmankhah;

import java.util.Queue;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Random;

/**
 * Represents an UNO game.
 * @author Amirparsa Salmankhah
 * @version 1.0.0
 */
public class Game {
    //a line for design
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
    //Last card
    private Card lastCard;
    //turns
    public HashMap<Integer, Integer> turns;
    //turn number
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
        //Adding 4 zeros
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

    /**
     * Constructor with 1 parameter.
     * @param playerNumber Number of the players
     */
    public Game(int playerNumber) {
        players = new Player[playerNumber];
        queue = new LinkedList<>();
        turns = new HashMap<>();
        turn = 0;
        direction = 1;
        penalty = 0;
        //setting turns
        for (int i = 0; i < playerNumber - 1; i++)
            turns.put(i, i + 1);
        turns.put(playerNumber - 1, 0);

    }

    /**
     * Gets game's players.
     * @return Game's players
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Gets number of the players.
     * @return Number of the players.
     */
    public int getNumberOfPlayers() {
        return players.length;
    }

    /**
     * Gets the last card.
     * @return Last card that is played.
     */
    public Card getLastCard() {
        return lastCard;
    }

    /**
     * Gets the turn of playing.
     * @return Turn of playing
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Gets direction of playing.
     * @return Direction of playing.
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Gets turns chain HashMap.
     * @return Turns HashMap.
     */
    public HashMap<Integer, Integer> getTurns() {
        return turns;
    }

    /**
     * Sets a player.
     * @param playerNumber Number of the player
     */
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

    /**
     * Changes the turns chain HashMap direction.
     */
    private void changeDirection() {
        if (direction == 1) {
            for (int i = 0; i < getNumberOfPlayers() - 1; i++)
                turns.put(i, i + 1);
            turns.put(getNumberOfPlayers() - 1, 0);
        } else {
            for (int i = getNumberOfPlayers() - 1; i > 0; i--)
                turns.put(i, i - 1);
            turns.put(0, getNumberOfPlayers() - 1);
        }
    }

    /**
     * Checks that the game is in progress or not.
     * @return true if it is and false if not.
     */
    public boolean isInProgress() {
        for (Player player : players)
            if (player.getDeck().getSize() == 0)
                return false;
        return true;
    }

    /**
     * initializes the game
     */
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
        lastCard = new WildCard('W', true);
        while (lastCard instanceof WildCard) {
            cardIndex = random.nextInt(allCardsSize);
            lastCard = allCards.getCards().get(cardIndex);
        }
        checkCardAction(lastCard, 0);
        allCardsSize--;
        //Queue
        for (int i = 0; i < allCardsSize; i++) {
            cardIndex = random.nextInt(allCardsSize);
            queue.add(allCards.getCards().get(cardIndex));
        }
    }

    /**
     * Chooses the cards for player's penalty.
     * @param number Number of penalty
     * @param player The player
     * @return Deck of chosen cards
     */
    private Deck chooseCardsFromQueue(int number, Player player) {
        Deck givenCards = new Deck();
        for (int i = 0; i < number; i++) {
            Card card = queue.remove();
            player.getDeck().addCard(card);
            givenCards.addCard(card);
        }
        return givenCards;
    }

    /**
     * Penalizes the player which is his turn.
     */
    private void penalizePlayer() {
        //if the last card is wild +4 or draw +2
        if ((lastCard.getSign() == 'W' || lastCard.getSign() == 'D') && lastCard.isActive()) {
            Deck givenCards = chooseCardsFromQueue(penalty, players[turn]);
            lastCard.setActive(false);
            System.out.println(penalty + " cards added to " + players[turn].getName() + "'s deck!");
            System.out.println();
            System.out.println("Given Cards:");
            givenCards.print(4);
            penalty = 0;
            BotPlayer.cons.get(turn).replace("Red", false);
            BotPlayer.cons.get(turn).replace("Green", false);
            BotPlayer.cons.get(turn).replace("Blue", false);
            BotPlayer.cons.get(turn).replace("Yellow", false);
            BotPlayer.cons.get(turn).replace("White", false);

        }
        // if the number of penalty is 1
        else {
            if(lastCard instanceof WildCard)
                BotPlayer.cons.get(turn).replace(((WildCard) lastCard).getRealColor(),true);
            else
                BotPlayer.cons.get(turn).replace(lastCard.getColor(), true);
            Deck givenCards = chooseCardsFromQueue(1, players[turn]);
            System.out.println(1 + " card added to " + players[turn].getName() + "'s deck!");
            System.out.println();
            System.out.println("Given card:");
            givenCards.print(4);
            if (players[turn].availableMoves(lastCard).getSize() != 0) {
                direction *= -1;
                changeDirection();
                turn = turns.get(turn);
                direction *= -1;
                changeDirection();
            }
        }
    }

    /**
     * Shows the number of cards of players.
     */
    public void showNumberOfCards() {
        System.out.println("Number of cards:");
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            Player player = players[i];
            System.out.print(player.getName() + "(" + player.getDeck().getSize() + ")" + "  ");
        }
        System.out.println();
    }

    /**
     * Shows the direction of playing.
     */
    public void showDirection() {
        System.out.print("Direction: ");
        if (direction == 1)
            System.out.println(">>>>>>>>>>>>>>>>>");
        else
            System.out.println("<<<<<<<<<<<<<<<<<");
    }

    /**
     * Checks and execute actionable cards action.
     * @param card The card
     * @param playerNumber Number of the player
     */
    public void checkCardAction(Card card, int playerNumber) {
        if (card instanceof WildCard) {
            if (card.getSign() == 'W') {
                penalty += 4;
                card.setActive(true);
            }
            String chosenColor = players[playerNumber].chooseColor();
            ((WildCard) card).setRealColor(chosenColor);
        } else if (card.getSign() == 'R') {
            direction *= -1;
            changeDirection();
        } else if (card.getSign() == 'D') {
            penalty += 2;
            card.setActive(true);
        } else if (card.getSign() == 'S')
            turn = turns.get(turn);
    }

    /**
     * Shows the score of the players.
     */
    public void showScores() {
        int[] scores = new int[getNumberOfPlayers()];
        int[] playerNumbers = new int[getNumberOfPlayers()];
        for (int i = 0; i < scores.length; i++) {
            scores[i] = players[i].getScore();
            playerNumbers[i] = i;
        }
        for (int i = 0; i < scores.length; i++) {
            for (int j = 1; j < scores.length; j++) {
                if (scores[j] < scores[j - 1]) {
                    int tmp = scores[j - 1];
                    scores[j - 1] = scores[j];
                    scores[j] = tmp;
                    tmp = playerNumbers[j - 1];
                    playerNumbers[j - 1] = playerNumbers[j];
                    playerNumbers[j] = tmp;
                }
            }
        }
        System.out.println("Final result:  (Player    Score)");
        for (int i = 0; i < scores.length; i++)
            System.out.println((i + 1) + ") " + players[playerNumbers[i]].getName() + "  " + scores[i]);
    }

    /**
     * executes one turn of playing.
     */
    public void playTurn(){
        //print players
        System.out.println("Players:");
        Player player = players[turn];
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            String color = "";
            if (i == turn)
                color = Card.backgroundCodes.get("Red");
            System.out.print(color + players[i].getName() + Card.backgroundCodes.get("Reset") + "  ");
        }
        System.out.println();
        //print direction of playing
        showDirection();
        System.out.println();
        //print number of cards of players
        showNumberOfCards();
        System.out.println();
        //print turn
        System.out.println("It's " + player.getName() + "'s turn!");
        System.out.println(player.getName() + "'s Deck:");
        //print deck of the player
        player.getDeck().print(4);
        System.out.println();
        System.out.println("Last Card:");
        System.out.println(lastCard);
        System.out.println("\n");
        Card card;
        Deck availableMoves = player.availableMoves(lastCard);
        //check if player can play card or not.
        if (availableMoves.getSize() == 0)
            //if player doesnt have any choice
            penalizePlayer();
        else {
            //if player can play a card
            card = player.think();
            queue.add(lastCard.reset());
            lastCard = card;
            if (players[turn].getDeck().getSize() == 0)
                return;
            checkCardAction(lastCard, turn);
        }
        //sleep
        if(players[turn] instanceof BotPlayer) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.exit(-1);
            }
        }
        turn = turns.get(turn);
        System.out.println(line);
    }
}
