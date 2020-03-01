package blackjack;

public class hand {

    private int[] cards = new int[21];//0 as, 1 dwojka, ... 8 dziewiatka, 9 dziesiatka, walet, dama, krol
    private int size;//ile kart w dloni
    private int asNumb;
    private boolean as;
    private int score;
    private deck Deck;

    hand(deck _Deck) {
        size = 0;
        asNumb = 0;
        as = false;
        score = 0;
        Deck = _Deck;
    }

    boolean takeCard(int card) {
        if (size < 21 && 0 <= card && card <= 9 && 0 <= score && score <= 21) {
            if (Deck.takeCard(card)) {
                cards[size] = card;
                score += card + 1;

                if (card == 0) {
                    asNumb++;
                    if (score <= 11 && !as) {//tylko jeden as moze byc na 11 bo dwa takie asy dawalyby 22 > 21
                        as = true;
                        score += 10;
                    }
                }

                if (score > 21 && as) {
                    as = false;
                    score -= 10;
                }
                size++;
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("ERROR hand.takeCard, card: " + card + ", size: " + size + ", score: " + score);
            return false;
        }
    }

    void returnCard() {
        if (size > 0) {
            int card = cards[size - 1];

            Deck.giveCardBack(card);
            score -= card + 1;

            if (card == 0) {
                asNumb--;
                if (asNumb == 0 && as) {
                    as = false;
                    score -= 10;
                }
            }

            if (asNumb > 0 && score <= 11 && !as) {
                as = true;
                score += 10;

            }
            size--;
        } else {
            System.out.println("ERROR hand.returnCard, size: " + size);
        }
    }

    int score() {
        return score;
    }

    boolean hasAs() {
        return as;
    }

    boolean isSize(int _size) {
        return size == _size;
    }

    protected int card(int pos) {
        if (0 <= pos && pos < size) {
            return cards[pos];
        }
        return -1;
    }

    protected void print() {
        System.out.print("score:");

        if (score > 21) {
            System.out.print("FF; ");
        } else if (score < 10) {
            System.out.print(" " + score + "; ");
        } else {
            System.out.print(score + "; ");
        }

        for (int i = 0; i < size; i++) {
            if (i > 0) {
                System.out.print(",");
            }
            if (cards[i] == 0) {
                System.out.print("as");
            } else if (cards[i] == 9) {
                System.out.print("10");
            } else {
                System.out.print(" " + (cards[i] + 1));
            }
        }
    }

    //to simulate
    /*protected void clear() {
        size = 0;
        asNumb = 0;
        as = false;
        score = 0;
    }*/
}
