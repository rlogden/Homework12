package edu.dmacc.codedsm.blackjack;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class BlackJack {

    public static HashMap<String, List<Integer>> myDeck = new HashMap<String, List<Integer>>();
    public static ArrayList<Card> dealerHand = new ArrayList<>();
    public static ArrayList<Card> playerHand = new ArrayList<>();
    public static ArrayList<Card> dealtCards = new ArrayList<>();
    public static String n;
    public static Scanner input = new Scanner(System.in);
    public static String[] faceCardsHearts = {"Jack of Hearts", "Queen of Hearts", "King of Hearts", "Ace of Hearts"};
    public static String[] faceCardsDiamonds = {"Jack of Diamonds", "Queen of Diamonds", "King of Diamonds", "Ace of Diamonds"};
    public static String[] faceCardsClubs = {"Jack of Clubs", "Queen of Clubs", "King of Clubs", "Ace of Clubs"};
    public static String[] faceCardsSpades = {"Jack of Spades", "Queen of Spades", "King of Spades", "Ace of Spades"};
    public static ArrayList<Integer> faceCardsValues = new ArrayList<>();
    public static ArrayList<Integer> aceValues = new ArrayList<>();
    public static File file = new File("blackjack_log.txt");
    public static PrintWriter outFile;

    public static void main(String[] args) throws IOException {
        fillDeck(myDeck);
        firstDeal(myDeck, 2);
        firstDealToDealer(myDeck, 2);
        playBlackjack();
    }

    public static List<Integer> createCards() {
        List<Integer> cards = new ArrayList<Integer>();
        for (int i = 2; i <= 10; i++) {
            cards.add(i);
        }
        return cards;
    }

    public static void fillDeck(HashMap<String, List<Integer>> x) {
        faceCardsValues.add(10);
        x.put("Hearts", createCards());
        x.put("Diamonds", createCards());
        x.put("Spades", createCards());
        x.put("Clubs", createCards());
        for (int i = 0; i < 3; i++) {
            x.put(faceCardsHearts[i], faceCardsValues);
            x.put(faceCardsDiamonds[i], faceCardsValues);
            x.put(faceCardsClubs[i], faceCardsValues);
            x.put(faceCardsSpades[i], faceCardsValues);
        }
        aceValues.add(1);
        x.put("Ace of Hearts", aceValues);
        x.put("Ace of Diamonds", aceValues);
        x.put("Ace of Clubs", aceValues);
        x.put("Ace of Spades", aceValues);
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

    public static void firstDealToDealer(HashMap<String, List<Integer>> x, int y) {
        while (dealerHand.size() < 2) {
            dealerHand.add(dealCards(x, y));
        }
        System.out.print("Dealer was dealt ");
        dealerHandPrinter(dealerHand);
        System.out.println(" and a card face down.");
    }

    public static void playBlackjack() throws IOException {
        boolean playing = true;
        while (playing) {
            if (getSum(playerHand) < 21) {
                System.out.print("Your hand is ");
                handPrinter(playerHand);
                System.out.println("for a total of " + getSum(playerHand) + ". Would you like to hit? Enter 1 for hit, 2 for stand.");
                n = input.next();
                if (n.equals("1")) {
                    dealtCards.clear();
                    dealtCards.add(dealCards(myDeck, 1));
                    System.out.print("You were dealt ");
                    handPrinter(dealtCards);
                    playerHand.addAll(dealtCards);
                    if (getSum(dealerHand) < 17) {
                        dealerHand.add(dealCards(myDeck, 1));
                    }
                } else if (n.equals("2")) {
                    System.out.print("You decided to stay with ");
                    handPrinter(playerHand);
                    System.out.println(" for a total of " + getSum(playerHand) + ".");
                    while (getSum(dealerHand) <= 16) {
                        dealerHand.add(dealCards(myDeck, 1));
                        System.out.print("The dealer chooses to hit and now has ");
                        handPrinter(dealerHand);
                        System.out.println(" for a total of " + getSum(dealerHand) + ".");
                    }
                    if (getSum(playerHand) > 21) {
                        showHands();
                        System.out.printf("%s wins!", "Dealer");
                        writeHandsToFile(getSum(playerHand), getSum(dealerHand), playerHand, dealerHand);
                        playing = false;
                    } else if (getSum(playerHand) > getSum(dealerHand)) {
                        showHands();
                        System.out.printf("%s wins!", "Player");
                        writeHandsToFile(getSum(playerHand), getSum(dealerHand), playerHand, dealerHand);
                        playing = false;
                    } else if ((getSum(playerHand) < getSum(dealerHand)) && (getSum(dealerHand) <= 21)) {
                        showHands();
                        System.out.printf("%s wins!", "Dealer");
                        writeHandsToFile(getSum(playerHand), getSum(dealerHand), playerHand, dealerHand);
                        playing = false;
                    } else if ((getSum(playerHand) < getSum(dealerHand)) && (getSum(dealerHand) > 21)) {
                        showHands();
                        System.out.printf("%s wins!", "Player");
                        writeHandsToFile(getSum(playerHand), getSum(dealerHand), playerHand, dealerHand);
                        playing = false;
                    } else if (getSum(playerHand) == getSum(dealerHand)) {
                        showHands();
                        System.out.printf("It\'s a tie!");
                        writeHandsToFile(getSum(playerHand), getSum(dealerHand), playerHand, dealerHand);
                        playing = false;
                    }
                } else {
                    System.out.print("Invalid input. Your hand is currently ");
                    handPrinter(playerHand);
                }
            } else if (getSum(dealerHand) == 21 && getSum(playerHand) != 21) {
                showHands();
                System.out.printf("%s wins!", "Dealer");
                writeHandsToFile(getSum(playerHand), getSum(dealerHand), playerHand, dealerHand);
                playing = false;
            } else if (getSum(playerHand) > 21) {
                showHands();
                System.out.printf("%s wins!", "Dealer");
                writeHandsToFile(getSum(playerHand), getSum(dealerHand), playerHand, dealerHand);
                playing = false;
            } else if (getSum(playerHand) == 21 && getSum(dealerHand) != 21) {
                showHands();
                System.out.printf("%s wins!", "Player");
                writeHandsToFile(getSum(playerHand), getSum(dealerHand), playerHand, dealerHand);
                playing = false;
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

    public static void handWriter(ArrayList<Card> toBeWritten) {
        for (int i = 0; i < toBeWritten.size(); i++) {
            Card card = toBeWritten.get(i);
            outFile.print(card.suit + " - " + card.value);
            if (i + 1 < toBeWritten.size()) {
                outFile.print(", ");
            } else {
                outFile.println(" ");
            }
        }
    }

    public static void dealerHandPrinter(ArrayList<Card> toBePrinted) {
        Card card = toBePrinted.get(0);
        System.out.print(card.suit + " - " + card.value);
    }

    public static int getSum(ArrayList<Card> toBeAdded) {
        int sum = 0;
        for (int i = 0; i < toBeAdded.size(); i++) {
            Card a = toBeAdded.get(i);
            sum = sum + a.value;
        }
        return sum;
    }

    public static void writeHandsToFile(int playerSum, int dealerSum, ArrayList<Card> player, ArrayList<Card> dealer) throws IOException {
        outFile = new PrintWriter(file);
        outFile.print("Player hand: ");
        handWriter(player);
        outFile.print("Dealer hand: ");
        handWriter(dealer);
        outFile.println("Player total: " + playerSum);
        outFile.println("Dealer total: " + dealerSum);
        if (playerSum == 21 && dealerSum != 21) {
            outFile.printf("%s wins!", "Player");
        } else if (dealerSum == 21 && playerSum != 21) {
            outFile.printf("%s wins!", "Dealer");
        } else if (playerSum > 21 && dealerSum <= 21) {
            outFile.printf("%s wins!", "Dealer");
        } else if (playerSum <= 21 && dealerSum > 21) {
            outFile.printf("%s wins!", "Player");
        } else if (playerSum > dealerSum) {
            outFile.printf("%s wins!", "Player");
        } else if (dealerSum > playerSum) {
            outFile.printf("%s wins!", "Dealer");
        } else if (dealerSum == playerSum) {
            outFile.printf("It\'s a tie!");
        }
        outFile.close();
    }

    public static void showHands() {
        System.out.print("The dealer had ");
        handPrinter(dealerHand);
        System.out.print(" for a total of " + getSum(dealerHand) + ". ");
        System.out.print("You had ");
        handPrinter(playerHand);
        System.out.print(" for a total of " + getSum(playerHand) + ". ");
    }
}
