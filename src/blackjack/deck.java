package blackjack;

import java.util.Random;

public class deck {

    private int[] deck = new int[10]; //0 as, 1 dwojka, ..., 8 dziewiatka, 9 dziesiatka, walet, dama, krol
    private int numberOfCards;
    private boolean countCards;
    private double[] cardsProb;
    private Random g = new Random();

    deck(int decks, int[] cards, boolean _countCards) {
        if (deck.length == cards.length) {
            numberOfCards = 0;
            for (int i = 0; i < deck.length; i++) {
                deck[i] = cards[i] * decks;
                numberOfCards += cards[i] * decks;
            }
            countCards = _countCards;

            if (!countCards) {
                cardsProb = new double[deck.length];
                for (int i = 0; i < deck.length; i++) {
                    cardsProb[i] = (double) deck[i] / numberOfCards;
                }
            }
        } else {
            throw new java.lang.Error("cards.length: " + cards.length);
        }
    }

    boolean takeCard(int card) {
        if (0 <= card && card <= 9) {
            if (countCards) {
                if (deck[card] > 0) {
                    deck[card]--;
                    numberOfCards--;
                    return true;
                }
                return false;
            } else {
                return true;
            }
        } else {
            throw new java.lang.Error("card: " + card);
        }
    }

    void giveCardBack(int card) {
        if (0 <= card && card <= 9) {
            if (countCards) {
                deck[card]++;
                numberOfCards++;
            }
        } else {
            throw new java.lang.Error("card: " + card);
        }
    }

    double[] prob() {
        double[] ans = new double[deck.length];
        if (countCards) {
            for (int i = 0; i < deck.length; i++) {
                ans[i] = (double) deck[i] / numberOfCards;
            }
        } else {
            ans = cardsProb.clone();
        }
        return ans;
    }

    //to simulate
    /*void restart(int decks) {
        if (countCards) {
            for (int i = 0; i < 9; i++) {
                deck[i] = 4 * decks;
            }
            deck[9] = 16 * decks;
            numberOfCards = 52 * decks;
        }
    }

    int randCard() {//random a card than have to be still taken from deck
        if (numberOfCards == 0) {
            return -1;
        }
        int ans = 0, sum = 0;
        int pick = g.nextInt(numberOfCards) + 1;

        for (int i = 0; i < 10; i++) {
            sum += deck[i];
            if (sum >= pick) {
                ans = i;
                break;
            }
        }

        return ans;
    }*/
}
