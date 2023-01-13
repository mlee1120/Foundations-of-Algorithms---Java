/*
 * This file illustrates Partitions.java from hw4.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program first receives a single value n, representing the number
 * of elements in the sequence, and also the sequence of integers.
 * It then has to calculate and print out two results:
 * 1. the number of unique ways the sequence can be partitioned into non-empty
 * consecutive subsequences such that the sum of each subsequence is even. O(n)
 * 2. the number of unique ways the sequence can be partitioned into consecutive
 * subsequences such that the sum of each subsequence is odd. O(n) or O(n^2)
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Partitions {
    /** the number of elements in the sequence */
    private int n;
    /** the result of the first part */
    private int resultEven;
    /** the result of the second part */
    private int resultOdd;
    /** the sequence of integers */
    private final List<Integer> X;
    private final Scanner in;

    /**
     * The constructor initializes some important fields.
     */
    public Partitions() {
        resultEven = 0;
        resultOdd = 0;
        X = new ArrayList<>();
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
            X.add(Integer.parseInt(aStringArray[i]));
        }
        in.close();
    }

    /**
     * This method calculates the result of the first part using greedy algorithm.
     */
    public void calculateEven() {
        int numberOfUnitEvenSubsequences = 0;
        boolean startWithOdd = false;
        for (int i = 0; i < n; i++) {
            if (X.get(i) % 2 == 0) {
                if (!startWithOdd) numberOfUnitEvenSubsequences++;
            } else {
                if (startWithOdd) numberOfUnitEvenSubsequences++;
                startWithOdd = !startWithOdd;
            }
        }
        if (!startWithOdd) resultEven = (int) Math.pow(2, numberOfUnitEvenSubsequences - 1);
    }

    /**
     * This method calculates the result of the second part using dynamic programming.
     */
    public void calculateOdd() {
        List<Integer> S = new ArrayList<>();
        // base case
        S.add(0);
        // the number of even integers between two odd integers
        int numberOfEvens = 0;
        boolean firstOdd = true;
        for (int i = 0; i < n; i++) {
            if (X.get(i) % 2 == 0) {
                S.add(S.get(i));
                numberOfEvens++;
            } else {
                if (!firstOdd) S.add(S.get(i) * (numberOfEvens + 1) + S.get(i - numberOfEvens - 1));
                    // first odd
                else {
                    S.add(1);
                    firstOdd = false;
                }
                numberOfEvens = 0;
            }
        }
        resultOdd = S.get(n);
    }

    /**
     * This method prints out the results of both parts
     */
    public void output() {
        System.out.println(resultEven);
        System.out.println(resultOdd);
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        Partitions hw4 = new Partitions();
        hw4.input();
        hw4.calculateEven();
        hw4.calculateOdd();
        hw4.output();
    }
}
