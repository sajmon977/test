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

        cardProb[0] = Deck.prob();
        for (int i = 0; i < 10; i++) {
            if (Croupier.takeCard(i)) {
                CroupierProb = CProbSave[i];

                cardProb[1] = Deck.prob();
                for (int j = 0; j < 10; j++) {
                    if (Player.takeCard(j)) {
                        expVal += cardProb[0][i] * cardProb[1][j] * playerTake();

                        Player.returnCard();
                    }
                }
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
        if (Player.isSize(2)) {
            double max = -8;

            double temp = doubleDown();
            if (temp > max) {
                max = temp;
            }

            if (split && Player.card(0) == Player.card(1)) {
                temp = split();
                if (temp > max) {
                    max = temp;
                }
            }

            temp = expReturn();//strategy hit-stand
            if (temp > max) {
                max = temp;
            }

            return max;
        } else {
            throw new java.lang.Error("player hand size isn't 2 ");
        }
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

    void print() {//works only with all cards in deck
        System.out.println("Player \\ Croupier");
        System.out.println("HIT   as 2  3  4  5  6  7  8  9 10");

        Player.takeCard(1);
        for (int j = 1; j < 10; j++) {
            Player.takeCard(j);
            System.out.print("   " + (j + 3) + " ");
            if (j < 7) {
                System.out.print(" ");
            }
            for (int i = 0; i < 10; i++) {
                if (Croupier.takeCard(i)) {
                    CroupierProb = CProbSave[i];
                    System.out.print(playPrint(false));
                    Croupier.returnCard();
                }
            }
            System.out.println();
            Player.returnCard();
        }
        Player.returnCard();

        Player.takeCard(9);
        for (int j = 1; j < 10; j++) {
            Player.takeCard(j);
            System.out.print("   " + (j + 11) + " ");

            for (int i = 0; i < 10; i++) {
                if (Croupier.takeCard(i)) {
                    CroupierProb = CProbSave[i];
                    System.out.print(playPrint(false));
                    Croupier.returnCard();
                }
            }
            System.out.println();
            Player.returnCard();
        }
        Player.returnCard();

        System.out.println();
        System.out.println("AS    as 2  3  4  5  6  7  8  9 10");

        Player.takeCard(0);
        for (int j = 0; j < 10; j++) {
            Player.takeCard(j);
            System.out.print("   ");
            if (j == 0) {
                System.out.print("as ");
            } else if (j == 9) {
                System.out.print("10 ");
            } else {
                System.out.print(j + 1 + "  ");
            }
            for (int i = 0; i < 10; i++) {
                if (Croupier.takeCard(i)) {
                    CroupierProb = CProbSave[i];
                    System.out.print(playPrint(false));
                    Croupier.returnCard();
                }
            }
            System.out.println();
            Player.returnCard();
        }
        Player.returnCard();

        System.out.println();
        System.out.println("SPLIT as 2  3  4  5  6  7  8  9 10");

        for (int j = 0; j < 10; j++) {
            Player.takeCard(j);
            Player.takeCard(j);

            System.out.print("   ");
            if (j == 0) {
                System.out.print("as ");
            } else if (j == 9) {
                System.out.print("10 ");
            } else {
                System.out.print(j + 1 + "  ");
            }
            for (int i = 0; i < 10; i++) {
                if (Croupier.takeCard(i)) {
                    CroupierProb = CProbSave[i];
                    System.out.print(playPrint(true));
                    Croupier.returnCard();
                }
            }
            System.out.println();
            Player.returnCard();
            Player.returnCard();
        }
    }

    String playPrint(boolean split) {
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

        temp = expReturn();
        if (temp > max) {
            max = temp;
            if (Player.hit()) {
                choice = "H";
            } else {
                choice = "-";
            }
        }
        return choice + "  ";
    }
}
