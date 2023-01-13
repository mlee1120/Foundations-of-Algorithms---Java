/*
 * This file illustrates Candy.java from hw3.
 */

import java.util.Scanner;

/**
 * This program first receives a single value n, representing the number
 * of points in a 2-d space, and also the coordinates of those n points.
 * It then has to calculate the number of right triangles that can formed
 * by choosing sets of three points of the n points.
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Candy {
    /** amount of allowance */
    private int t;
    /** the number of different types of candy */
    private int n;
    /** the maximum number of different types of candy you can buy with the allowance */
    private int result;
    /** a list of cost for each of the n types of candy */
    private int[] candies;
    private final Scanner in;

    /**
     * The constructor initiates some important fields.
     */
    public Candy() {
        result = 0;
        in = new Scanner(System.in);
    }

    /**
     * This method reads the input from the standard input and stores data into corresponding data structures.
     */
    public void input() {
        t = in.nextInt();
        n = in.nextInt();
        in.nextLine();
        String[] aStringArray;
        aStringArray = in.nextLine().split(" ");
        candies = new int[n];
        for (int i = 0; i < n; i++) {
            candies[i] = Integer.parseInt(aStringArray[i]);
        }
        in.close();
    }

    /**
     * This method determines the maximum number of different types of candy you can
     * buy with the allowance by choosing the candy having the lowest cost first.
     * (greedy approach)
     */
    public void lowestCostFirst() {
        // if the remaining allowance is enough to but the currently cheapest candy
        boolean enoughAllowanceLeft = true;
        // the index of the cheapest candy in the list of candies
        int minIndex;
        do {
            minIndex = 0;
            // find the cheapest candy by traversing O(n)
            for (int i = 1; i < n; i++) {
                if (candies[minIndex] > candies[i]) minIndex = i;
            }
            if (candies[minIndex] <= t) {
                result++;
                t = t - candies[minIndex];
                // set already-bought candy's cost to a new cost > allowance left to prevent from buying it again
                candies[minIndex] = t + 1;
            } else {
                enoughAllowanceLeft = false;
            }
        } while (enoughAllowanceLeft);
    }

    /**
     * This method prints out the maximum number of different types of candy you can buy with the allowance.
     */
    public void output() {
        System.out.println(result);
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        Candy hw3 = new Candy();
        hw3.input();
        hw3.lowestCostFirst();
        hw3.output();
    }
}
