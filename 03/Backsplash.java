/*
 * This file illustrates Backsplash.java from hw3.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program first receives a single value n, representing a backslash of size 2 X n.
 * It then has to calculate how many overall patterns there are with which you can tile
 * the backslash with only 1 X 1 square tiles and 2 X 2 L-shaped tiles on O(n).
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Backsplash {
    /** the size of the backslash is 2 x n */
    private int n;
    /** S array for dynamic programming solution */
    private final List<Integer> S;
    Scanner in;

    /**
     * The constructor initiates some important fields.
     */
    public Backsplash() {
        in = new Scanner(System.in);
        S = new ArrayList<>();
    }

    /**
     * This method reads the input from the standard input and store it into the corresponding data structure.
     */
    public void input() {
        n = in.nextInt();
        in.close();
    }

    /**
     * This method performs establishing the S array in dynamic programming
     */
    public void dynamicProgramming() {
        for (int i = 0; i <= n; i++) {
            if (i >= 3) {
                S.add(S.get(i - 1) + 4 * S.get(i - 2) + 2 * S.get(i - 3));
            } else {
                // base cases
                if (i == 0) S.add(1);
                else if (i == 1) S.add(1);
                else S.add(5);
            }
        }
    }

    /**
     * This method prints out the final solution.
     */
    public void output() {
        // the solution is at the last element of the S array in this case
        System.out.println(S.get(n));
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        Backsplash hw3 = new Backsplash();
        hw3.input();
        hw3.dynamicProgramming();
        hw3.output();
    }
}
