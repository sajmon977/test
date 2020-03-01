package blackjack;

public class probCalc {

    protected deck Deck;
    protected croupier Croupier;
    protected player Player;
    protected double BJrate;
    private double[][] CProbSave = new double[10][6];
    private double[] CroupierProb = new double[6];

    public void croupier() {
        for (int i = 0; i < 10; i++) {
            if (Croupier.takeCard(i)) {
                croupier(1);
                Croupier.returnCard();
            }
        }
    }

    private void croupier(double prob) {
        if (Croupier.hit()) {
            double[] cardProb = Deck.prob();
            for (int i = 0; i < 10; i++) {
                if (Croupier.takeCard(i)) {
                    croupier(cardProb[i] * prob);
                    Croupier.returnCard();
                }
            }
        } else {

            if (Croupier.score() > 21) {
                CProbSave[Croupier.firstCard()][5] += prob;
            } else {
                CProbSave[Croupier.firstCard()][Croupier.score() - 17] += prob;
            }
        }
    }

    double expVal() {
        double expVal = 0;
        double[][] cardProb = new double[2][];

        /* HIT */ /*
        System.out.println("Croupier \\ Player  HIT");
        System.out.println("  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19");//*/
 /* AS */ /*
        System.out.println("Croupier \\ Player  AS");
        System.out.println("  2 3 4 5 6 7 8 9 10");//*/
 /* SPLIT */ /*
        System.out.println("Croupier \\ Player  SPLIT");
        System.out.println("  a 2 3 4 5 6 7 8 9 10");//*/

        cardProb[0] = Deck.prob();
        for (int i = 0; i < 10; i++) {
            if (Croupier.takeCard(i)) {
                CroupierProb = CProbSave[i];

                /* SPLIT */ /* AS */ /* HIT */ /*
                if (i == 0) {
                    System.out.print("a ");
                } else if (i == 9) {
                    System.out.print("10");
                } else {
                    System.out.print(i + 1 + " ");
                }//*/

                cardProb[1] = Deck.prob();
                for (int j = 0; j < 10; j++) {
                    if (Player.takeCard(j)) {
                        expVal += cardProb[0][i] * cardProb[1][j] * playerTake();

                        Player.returnCard();
                    }
                }
                /* SPLIT */ /* AS */ /* HIT */ /*
                System.out.println();//*/
                Croupier.returnCard();
            }
        }

        return expVal;
    }

    double playerTake() {
        double playerTake = 0;
        double[] cardProb = Deck.prob();
        for (int i = 0; i < Player.card(0); i++) {
            if (Player.takeCard(i)) {
                playerTake += 2 * cardProb[i] * play(true);

                Player.returnCard();
            }
        }
        if (Player.takeCard(Player.card(0))) {
            playerTake += cardProb[Player.card(0)] * play(true);

            Player.returnCard();
        }

        return playerTake;
    }

    double play(boolean split) {
        if (!Player.isSize(2)) {
            System.out.println("ERROR size should be 2");
        }
        double max = -8;
        String choice = "";

        double temp = doubleDown();
        if (temp > max) {
            max = temp;
            choice = "D";
        }

        if (split && Player.card(0) == Player.card(1)) {
            temp = split();
            if (temp > max) {
                max = temp;
                choice = "S";
            }
        }

        temp = expReturn();//strategy hit-stand
        if (temp > max) {
            max = temp;
            /* SPLIT */ /* AS */ /* HIT */ /*
            if (Player.hit()) {
                choice = "H";
            } else {
                choice = "-";
            }//*/
        }
        /* HIT */ /*
        if (split && ((Player.card(0) > 1 && Player.card(1) == 1) || (Player.card(0) == 9 && Player.card(1) > 1 && Player.card(1) != 9))) {
            System.out.print(choice + "  ");
        }//*/

 /* AS */ /*
        if (split && Player.card(0) != 9 && Player.card(1) == 0) {
            System.out.print(choice + " ");
        }//*/

 /* SPLIT */ /*
        if (split && Player.card(0) == Player.card(1)) {
            System.out.print(choice + " ");
        }//*/

        return max;
    }

    private double doubleDown() {
        double doubleDown = 0;
        double[] cardProb = Deck.prob();
        for (int i = 0; i < 10; i++) {
            if (Player.takeCard(i)) {
                doubleDown += cardProb[i] * standWin();

                Player.returnCard();
            }
        }
        return 2 * doubleDown;
    }

    private double split() {
        Player.returnCard();
        player temp = new player(Croupier, Deck);
        temp.takeCard(Player.card(0));

        double split = 0;
        double[] cardProb = Deck.prob();
        for (int i = 0; i < 10; i++) {
            if (Player.takeCard(i)) {
                split += cardProb[i] * play(false);

                Player.returnCard();
            }
        }

        temp.returnCard();
        Player.takeCard(Player.card(0));
        return 2 * split;
    }

    private double expReturn() {
        double expReturn = 0;

        if (Player.hit()) {
            double[] cardProb = Deck.prob();

            for (int i = 0; i < 10; i++) {
                if (Player.takeCard(i)) {
                    expReturn += cardProb[i] * expReturn();
                    Player.returnCard();
                }
            }
        } else {
            expReturn = standWin();
        }
        return expReturn;
    }

    double standWin() {
        double standWin = 0;

        if (Player.score() > 21) {
            standWin = -1;
        } else if (Player.score() == 21 && Player.isSize(2)) {
            standWin = BJrate;
        } else {
            standWin += CroupierProb[5];
            for (int i = 0; i < 5; i++) {
                if (Player.score() > i + 17) {
                    standWin += CroupierProb[i];
                } else if (Player.score() < i + 17) {
                    standWin -= CroupierProb[i];
                }
            }
        }

        return standWin;
    }

    /*
    void simulation() {
        Croupier.takeCard(9);
        Croupier.takeCard(5);

        simulate(1);
        System.out.println(Arrays.toString(CroupierProb));
    }

    private void simulate(double prob) {
        if (Croupier.hit()) {
            double[] cardProb = Deck.prob();
            for (int i = 0; i < 10; i++) {
                if (Croupier.takeCard(i)) {
                    simulate(cardProb[i] * prob);
                    Croupier.returnCard();
                }
            }
        } else {
            System.out.print("prob = " + prob + "; ");
            Croupier.print();
            System.out.println();

            if (Croupier.score() > 21) {
                CroupierProb[5] += prob;
            } else {
                CroupierProb[Croupier.score() - 17] += prob;
            }
        }
    }

    void print(double prob) {
        System.out.print("prob: " + prob + "; ");
        Croupier.print();
        System.out.print("; ");
        Player.print();
        System.out.println();
    }
    
     */
}
