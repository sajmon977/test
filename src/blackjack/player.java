package blackjack;

import java.util.BitSet;

public class player extends hand {

    private croupier Croupier;
    private BitSet strategy;// (21-12) * 10 = 9 * 10
    private BitSet asStrategy;// 9 * 10 bez as i dziesiatka

    player(croupier _Croupier, deck Deck) {
        super(Deck);
        Croupier = _Croupier;
    }

    void set(BitSet[] _strategy) {
        strategy = _strategy[0];
        asStrategy = _strategy[1];
    }

    boolean hit() {
        if (score() >= 21) {
            return false;
        } else if (score() <= 11) {
            return true;
        } else {
            if (hasAs()) {
                return asStrategy.get(9 * Croupier.firstCard() + score() - 12);
            } else {
                return strategy.get(9 * Croupier.firstCard() + score() - 12);
            }
        }

    }

    public void printStrategy() {
        System.out.println("Croupier \\ Player AS");
        System.out.println("  a 2 3 4 5 6 7 8 9");
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                System.out.print("a ");
            } else if (i == 9) {
                System.out.print("10");
            } else {
                System.out.print(i + 1 + " ");
            }

            for (int j = 0; j < 9; j++) {
                if (asStrategy.get(10 * i + j)) {
                    System.out.print("H ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("Croupier \\ Player HIT");
        System.out.println("  12 13 14 15 16 17 18 19 20 ");
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                System.out.print("a ");
            } else if (i == 9) {
                System.out.print("10");
            } else {
                System.out.print(i + 1 + " ");
            }

            for (int j = 0; j < 9; j++) {
                if (strategy.get(9 * i + j)) {
                    System.out.print("H  ");
                } else {
                    System.out.print("-  ");
                }
            }
            System.out.println();
        }
    }

    public void print() {
        System.out.print("Player: ");
        super.print();
    }

    //to simulate
    /*void start(int card1, int card2) {
        clear();
        takeCard(card1);
        takeCard(card2);
    }*/
}
