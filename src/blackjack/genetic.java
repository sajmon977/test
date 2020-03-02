package blackjack;

import java.util.BitSet;
import java.util.Arrays;
import java.util.Random;

public class genetic {

    int[] size = {9 * 10, 9 * 10}; //HIT, AS
    int population = 200;
    int survive = 20;
    int mutation = 3;
    int mutationRate = 10;
    int bestRate = 10;

    game Game;
    int best;
    double[] score;
    BitSet[][] strategy;

    Random g = new Random();

    genetic(game _Game) {
        Game = _Game;
        score = new double[population];
        strategy = new BitSet[population][size.length];

        for (int i = 0; i < population; i++) {
            for (int j = 0; j < size.length; j++) {
                strategy[i][j] = new BitSet(size[j]);

                for (int k = 0; k < size[j]; k++) {
                    if (g.nextBoolean()) {
                        strategy[i][j].set(k);
                    }
                }
            }
        }
    }

    void run(int n, int m) {
        for (int i = 0; i < n; i++) {
            generation();
        }
        mutation = 2;
        mutationRate = 5;
        for (int i = 0; i < m; i++) {
            generation();
        }

        for (int i = 0; i < population; i++) {
            score[i] = Game.test(strategy[i]);
        }

        int max = 0;
        for (int i = 1; i < population; i++) {
            if (score[max] < score[i]) {
                max = i;
            }
        }
        print(max);
        Game.print();
    }

    void generation() {
        best = 0;

        for (int i = 0; i < population; i++) {
            score[i] = Game.test(strategy[i]);
        }

        double[] score2 = score.clone();
        Arrays.sort(score2);
        double max = score2[population - survive];

        int pos = 0;
        for (int i = 0; i < population; i++) {
            if (score[i] > max) {
                strategy[pos] = strategy[i].clone();
                pos++;
                best++;
            }
        }
        for (int i = 0; i < population; i++) {
            if (score[i] == max) {
                strategy[pos] = strategy[i].clone();
                pos++;
            }
            if (pos >= survive) {
                break;
            }
        }

        System.out.println(score2[population - 1]);
        mutate();
    }

    void mutate() {
        int[][] prob = new int[size.length][];
        int rand;
        int max = best * bestRate + survive - best;

        for (int i = 0; i < size.length; i++) {
            prob[i] = new int[size[i]];
            for (int j = 0; j < survive; j++) {
                if (j < best) {
                    for (int k = 0; k < size[i]; k++) {
                        if (strategy[j][i].get(k)) {
                            prob[i][k] += bestRate;
                        }
                    }
                } else {
                    for (int k = 0; k < size[i]; k++) {
                        if (strategy[j][i].get(k)) {
                            prob[i][k]++;
                        }
                    }
                }
            }
        }

        for (int i = survive; i < population; i++) {
            for (int j = 0; j < size.length; j++) {
                strategy[i][j].clear();

                for (int k = 0; k < size[j]; k++) {
                    rand = g.nextInt(max);
                    if (rand < prob[j][k]) {
                        strategy[i][j].set(k);
                    }
                }

                if (g.nextInt(mutation) == 0) {
                    rand = g.nextInt(mutationRate);
                    for (int k = 0; k < rand; k++) {
                        strategy[i][j].flip(g.nextInt(size[j]));
                    }
                }
            }
        }
    }

    void print(int pos) {
        System.out.print("{" + strategy[pos][0]);
        for (int i = 1; i < size.length; i++) {
            System.out.print("," + strategy[pos][i]);
        }
        System.out.println("};");
        System.out.println();
    }
}
