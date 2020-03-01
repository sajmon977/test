package blackjack;

import java.util.BitSet;

public class game extends probCalc {

    game(int decks, int[] cards, boolean countCards, double _BJrate) {
        Deck = new deck(decks, cards, countCards);
        Croupier = new croupier(Deck);
        Player = new player(Croupier, Deck);
        BJrate = _BJrate;
        croupier();
    }

    double test(BitSet[] strategy) {
        Player.set(strategy);
        return expVal();
    }

    /*
    public double play() {
        int temp;
        int ans = 0;
        int count = 0;
        

        return (double) ans / count;
    }

    private int round() {
        
        Croupier.start(Deck.takeCard());
        Player.start(Deck.takeCard(), Deck.takeCard());
        while (Player.Hand.val() <= 21 && Player.takeCard()) {
            Player.getCard(Deck.takeCard());
        }
        if (Player.Hand.val() > 21) {
            return -1;
        }
        while (Croupier.Hand.val() <= 21 && Croupier.takeCard()) {
            Croupier.getCard(Deck.takeCard());
        }
        if (Croupier.Hand.val() > 21) {
            return 1;
        }

        if (Croupier.Hand.val() > Player.Hand.val()) {
            return -1;
        } else if (Croupier.Hand.val() == Player.Hand.val()) {
            return 0;
        } else {
            return 1;
        }
         
        return 0;
    }
     */
}
