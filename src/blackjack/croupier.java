package blackjack;

public class croupier extends hand {

    croupier(deck Deck) {
        super(Deck);
    }

    boolean hit() {
        return (score() <= 16 || (score() == 17 && hasAs()));
    }

    int firstCard() {
        return card(0);
    }

    public void print() {
        System.out.print("Croupier: ");
        super.print();

    }

    //to simulate
    /*void start(int card) {
        clear();
        takeCard(card);
    }*/
}
