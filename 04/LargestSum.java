/*
 * This file illustrates LargestSum.java from hw4.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program first receives a single value n, representing the number
 * of elements in the sequence, and also the sequence of integers.
 * It then has to calculate and print out the the largest possible sum of
 * elements in an increasing subsequence with time complexity of O(n^2).
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class LargestSum {
    /** the number of elements in the sequence */
    private int n;
    /** the sequence of integers */
    private final List<Integer> aList;
    /**
     * the dynamic programming array of the largest possible sum of elements in an increasing
     * subsequence while given access to the first specific number of elements in the sequence
     */
    private final List<Integer> S;
    private final Scanner in;

    /**
     * The constructor initializes some important fields.
     */
    public LargestSum() {
        aList = new ArrayList<>();
        S = new ArrayList<>();
        in = new Scanner(System.in);
    }

    /**
     * This method reads the input from the standard input and stores data into corresponding data structures.
     */
    public void input() {
        n = in.nextInt();
        in.nextLine();
        String[] aStringArray = in.nextLine().split(" ");
        for (int i = 0; i < n; i++) {
            aList.add(Integer.parseInt(aStringArray[i]));
        }
        in.close();
    }

    /**
     * This method establishes the dynamic programming array by calculating the largest
     * possible sum in an increasing subsequence with given access to the first specific
     * number of elements in the sequence.
     */
    public void calculate() {
        // base case
        S.add(0);
        int max;
        for (int i = 0; i < n; i++) {
            max = 0;
            /* back tracks to find elements with smaller values than the value of the current
               element and find the largest possible sum in an increasing subsequence */
            for (int j = i - 1; j >= 0; j--) {
                if (aList.get(i) > aList.get(j)) {
                    if (S.get(j + 1) + aList.get(i) > max) max = S.get(j + 1) + aList.get(i);
                }
            }
            if (max == 0) S.add(aList.get(i));
            else S.add(max);
        }
    }

    /**
     * This method prints out the the largest possible sum of elements in an increasing subsequence.
     */
    public void output() {
        int max = 0;
        for (int i = 0; i <= n; i++) {
            if (S.get(i) >= max) max = S.get(i);
        }
        System.out.println(max);
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        LargestSum hw4 = new LargestSum();
        hw4.input();
        hw4.calculate();
        hw4.output();
    }
}

