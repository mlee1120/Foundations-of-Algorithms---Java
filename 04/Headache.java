/*
 * This file illustrates Headache.java from hw4.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program first receives:
 *  1. a single value m, representing the number of people in line 1 (line A)
 *  2. a single value n, representing the number of people in line 2 (line B)
 *  3. m employees from line 1 (listed from back to front)
 *  4. n employees from line 2 (listed from back to front)
 * It then has to calculate and print out the minimum units of headache incurred
 * to get all (m + n) employees on the ride with time complexity of O(mn).
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Headache {
    /** the number of employees in Line 1 (line A) */
    private int m;
    /** the number of employees in line 2 (line B) */
    private int n;
    /** m employees from line 1 (listed from back to front) */
    private String[] lineA;
    /** n employees from line 2 (listed from back to front) */
    private String[] lineB;
    /** 2D dynamic programming array */
    private final List<List<Integer>> S;
    private final Scanner in;

    /**
     * The constructor initializes some important fields.
     */
    public Headache() {
        S = new ArrayList<>();
        in = new Scanner(System.in);
    }

    /**
     * This method reads the input from the standard input and stores data into corresponding data structures.
     */
    public void input() {
        m = in.nextInt();
        n = in.nextInt();
        in.nextLine();
        lineA = in.nextLine().split(" ");
        lineB = in.nextLine().split(" ");
        in.close();
    }

    /**
     * This method calculates the minimum units of headache incurred to get specific numbers
     * of employees from both lines (j and i) on the ride, where 0 <= j <= m and 0 <= k <= n.
     */
    public void pairing() {
        // takes the person at the front of both lines
        int optionA;
        // takes only one person at the front of lineA
        int optionB;
        // takes only one person at the front of lineB
        int optionC;
        // takes two persons at the front of lineA
        int optionD;
        // takes two persons at the front of lineB
        int optionE;
        // checks EN or NE pairing when taking the person at the front of both lines
        boolean pairEN_A, pairNE_A;
        // checks EN or NE pairing when taking two persons at the front of lineA
        boolean pairEN_B, pairNE_B;
        // checks EN or NE pairing when taking two persons at the front of lineB
        boolean pairEN_C, pairNE_C;
        // rows
        for (int k = 0; k <= n; k++) {
            // columns
            for (int j = 0; j <= m; j++) {
                // creates 2D array
                if (k == 0) S.add(new ArrayList<>());
                // resets options and pairings
                optionA = 0;
                optionB = 0;
                optionC = 0;
                optionD = 0;
                optionE = 0;
                pairEN_A = false;
                pairNE_A = false;
                pairEN_B = false;
                pairNE_B = false;
                pairEN_C = false;
                pairNE_C = false;

                // base case at S[0][0]
                if (k == 0 && j == 0) S.get(j).add(0);
                    // cases for the first row
                else if (k == 0) {
                    // base cases at S[1][0]
                    if (j == 1) S.get(j).add(4);

                    else {
                        // takes two persons at the front of lineA
                        pairEN_B = (lineA[m - j + 1].equals("E") && lineA[m - j].equals("N"));
                        pairNE_B = (lineA[m - j + 1].equals("N") && lineA[m - j].equals("E"));
                        if (pairEN_B || pairNE_B) optionD = S.get(j - 2).get(k) + 5 + 3;
                        else optionD = S.get(j - 2).get(k) + 3;
                        // takes only one person at the front of lineA
                        if (j == m) optionB = S.get(j - 1).get(k);
                        else optionB = S.get(j - 1).get(k) + 4;
                        // picks the option incurring less units of headache
                        S.get(j).add(Math.min(optionB, optionD));
                    }
                }
                // cases for the first column
                else if (j == 0) {
                    // base cases at S[0][1]
                    if (k == 1) S.get(j).add(4);
                    else {
                        // takes two persons at the front of lineB
                        pairEN_C = lineB[n - k + 1].equals("E") && lineB[n - k].equals("N");
                        pairNE_C = lineB[n - k + 1].equals("N") && lineB[n - k].equals("E");
                        if (pairEN_C || pairNE_C) optionE = S.get(j).get(k - 2) + 5 + 3;
                        else optionE = S.get(j).get(k - 2) + 3;
                        // takes only one person at the front of lineB
                        if (k == n) optionC = S.get(j).get(k - 1);
                        else optionC = S.get(j).get(k - 1) + 4;
                        // picks the option incurring less units of headache
                        S.get(j).add(Math.min(optionC, optionE));
                    }
                } else {
                    // takes the person at the front of both lines
                    pairEN_A = lineA[m - j].equals("E") && lineB[n - k].equals("N");
                    pairNE_A = lineA[m - j].equals("N") && lineB[n - k].equals("E");
                    if (pairEN_A || pairNE_A) optionA = S.get(j - 1).get(k - 1) + 5;
                    else optionA = S.get(j - 1).get(k - 1);

                    // takes only one person at the front of lineA
                    // lineB is empty or one left in lineA
                    if (k == n || j == m) optionB = S.get(j - 1).get(k);
                    else optionB = S.get(j - 1).get(k) + 4;

                    // takes only one person at the front of lineB
                    // lineA is empty or one left in lineB
                    if (k == n || j == m) optionC = S.get(j).get(k - 1);
                    else optionC = S.get(j).get(k - 1) + 4;

                    // takes two persons at the front of lineA
                    if (j >= 2) {
                        pairEN_B = lineA[m - j + 1].equals("E") && lineA[m - j].equals("N");
                        pairNE_B = lineA[m - j + 1].equals("N") && lineA[m - j].equals("E");
                        // lineB is not empty
                        if (k != n || j != m) {
                            if (pairEN_B || pairNE_B) optionD = S.get(j - 2).get(k) + 5 + 3;
                            else optionD = S.get(j - 2).get(k) + 3;
                        }
                        // lineB is empty
                        else {
                            if (pairEN_B || pairNE_B) optionD = S.get(j - 2).get(k) + 5;
                            else optionD = S.get(j - 2).get(k);
                        }
                    }

                    // takes two persons at the front of lineB
                    if (k >= 2) {
                        pairEN_C = lineB[n - k + 1].equals("E") && lineB[n - k].equals("N");
                        pairNE_C = lineB[n - k + 1].equals("N") && lineB[n - k].equals("E");
                        // lineA is not empty
                        if (k != n || j != m) {
                            if (pairEN_C || pairNE_C) optionE = S.get(j).get(k - 2) + 5 + 3;
                            else optionE = S.get(j).get(k - 2) + 3;
                        }
                        // lineA is empty
                        else {
                            if (pairEN_C || pairNE_C) optionE = S.get(j).get(k - 2) + 5;
                            else optionE = S.get(j).get(k - 2);
                        }
                    }

                    // picks the option incurring least units of headache
                    if (k >= 2 && j >= 2) {
                        S.get(j).add(Math.min(optionA, Math.min(optionB, Math.min(optionC, Math.min(optionD, optionE)))));
                    } else if (j >= 2) {
                        S.get(j).add(Math.min(optionA, Math.min(optionB, Math.min(optionC, optionD))));
                    } else if (k >= 2) {
                        S.get(j).add(Math.min(optionA, Math.min(optionB, Math.min(optionC, optionE))));
                    } else {
                        S.get(j).add(Math.min(optionA, Math.min(optionB, optionC)));
                    }
                }
            }
        }
    }

    /**
     * This method prints out the minimum units of headache
     * incurred to get all (m + n) employees on the ride.
     */
    public void output() {
        System.out.println(S.get(m).get(n));
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        Headache hw4 = new Headache();
        hw4.input();
        hw4.pairing();
        hw4.output();
    }
}
