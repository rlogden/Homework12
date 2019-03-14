
package edu.dmacc.codedsm.blackjack;


import java.util.*;

public class DeckRandomizer {

    public static List<Card> chooseRandomCards(Map<String, List<Integer>> deck, int numberOfCards) {
        List<Card> chosenCards = new ArrayList<>();
        Set<String> suits = deck.keySet();
        Random random = new Random();

        for (int i = 0; i < numberOfCards; i++) {
            Card chosenSuitAndCard = choseACard(deck, suits.toArray(new String[suits.size()])[random.nextInt(suits.size())], random);
            while (chosenCards.contains(chosenSuitAndCard)) {
                chosenSuitAndCard = choseACard(deck, suits.toArray(new String[suits.size()])[random.nextInt(suits.size())], random);
            }
            chosenCards.add(chosenSuitAndCard);
        }

        return chosenCards;
    }

    private static Card choseACard(Map<String, List<Integer>> deck, String chosenSuit, Random random) {
        List<Integer> suitCards = deck.get(chosenSuit);
        Integer chosenCard = suitCards.get(random.nextInt(suitCards.size()));
        return new Card(chosenSuit, chosenCard);
    }

}