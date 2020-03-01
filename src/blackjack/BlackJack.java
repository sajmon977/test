package blackjack;

import java.util.BitSet;

public class BlackJack {

    int[] cards = {4, 4, 4, 4, 4, 4, 4, 4, 4, 16};//0 as, 1 dwojka, ..., 8 dziewiatka, 9 dziesiatka, walet, dama, krol
    game Game = new game(6, cards, true, 1.5);//decks, cards, countCards, BJrate

    void test() {
        int[][] tab = {{0, 1, 2, 3, 4, 9, 18, 54, 55, 56, 57, 58, 63, 64, 65, 66, 67, 72, 73, 74, 75, 76, 81, 82, 83, 84, 85},{0, 1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14, 18, 19, 20, 21, 22, 23, 27, 28, 29, 30, 31, 32, 36, 37, 38, 39, 40, 41, 48, 49, 50, 54, 55, 56, 57, 58, 59, 63, 64, 65, 66, 67, 68, 72, 73, 74, 75, 76, 77, 78, 81, 82, 83, 84, 85, 86, 87}};

        BitSet[] strategy = new BitSet[2];

        for (int i = 0; i < strategy.length; i++) {
            strategy[i] = new BitSet(90);
            for (int j = 0; j < tab[i].length; j++) {
                strategy[i].set(tab[i][j]);
            }
        }
        Game.Player.set(strategy);
        Game.Player.printStrategy();
        System.out.println(Game.test(strategy));
    }

    void run() {
        ///*
        genetic a = new genetic(Game);
        a.run(20);//*/
/*
        test();//*/
    }

    public static void main(String[] args) {
        BlackJack a = new BlackJack();
        a.run();
    }

}
