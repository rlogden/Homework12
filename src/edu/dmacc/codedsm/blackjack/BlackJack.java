package edu.dmacc.codedsm.blackjack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class BlackJack {

    public static HashMap<String, List<Integer>> myDeck = new HashMap<String, List<Integer>>();
    public static ArrayList<Card> playerHand = new ArrayList<>();
    public static ArrayList<Card> dealtCards = new ArrayList<>();
    public static String n;
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        fillDeck(myDeck);
        firstDeal(myDeck, 2);
        playBlackjack();
    }

    public static List<Integer> createCards() {
        List<Integer> cards = new ArrayList<Integer>();
        for (int i = 1; i < 14; i++) {
            cards.add(i);
        }
        return cards;
    }

    public static void fillDeck(HashMap<String, List<Integer>> x) {
        x.put("Hearts", createCards());
        x.put("Diamonds", createCards());
        x.put("Spades", createCards());
        x.put("Clubs", createCards());
    }

    public static Card dealCards(HashMap<String, List<Integer>> deck, int numOfCards) {
        Card chosenCards = DeckRandomizer.chooseRandomCards(deck, numOfCards).get(0);
        deck.get(chosenCards.suit).remove(chosenCards.value);
        return chosenCards;
    }

    public static void firstDeal(HashMap<String, List<Integer>> x, int y) {
        while (dealtCards.size() < 2) {
            dealtCards.add(dealCards(x, y));
        }
        System.out.print("You were dealt ");
        handPrinter(dealtCards);
        playerHand.addAll(dealtCards);
    }

    public static void playBlackjack() {
        boolean playing = true;
        while (playing) {
            System.out.println("Your hand total is " + getSum(playerHand) + ". Would you like to hit? Enter 1 for hit, 2 for stand.");
            n = input.next();
            if (n.equals("1")) {
                dealtCards.clear();
                dealtCards.add(dealCards(myDeck, 1));
                System.out.print("You were dealt ");
                handPrinter(dealtCards);
                playerHand.addAll(dealtCards);
            } else if (n.equals("2")) {
                System.out.print("Player\'s hand was " );
                handPrinter(playerHand);
                System.out.print("for a score of " + getSum(playerHand));
                playing = false;
            } else {
                System.out.print("Invalid input. Your hand is currently ");
                handPrinter(playerHand);
            }
        }
    }

    public static void handPrinter(ArrayList<Card> toBePrinted) {
        for (int i = 0; i < toBePrinted.size(); i++) {
            Card card = toBePrinted.get(i);
            System.out.print(card.suit + " - " + card.value);

            if (i + 1 < toBePrinted.size()) {
                System.out.print(", ");
            } else {
                System.out.println(" ");
            }
        }
    }

    public static int getSum(ArrayList<Card> toBeAdded) {
        int sum = 0;
        for (int i = 0; i < toBeAdded.size(); i++) {
            Card a = toBeAdded.get(i);
            sum = sum + a.value;
        }
        return sum;
    }
}